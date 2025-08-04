package com.loopers.application.order

import com.loopers.concurrency
import com.loopers.domain.IntegrationTest
import com.loopers.domain.payment.Payment
import com.loopers.domain.payment.PaymentMethod
import com.loopers.domain.point.UserPoint
import com.loopers.domain.product.Product
import com.loopers.domain.shared.IdAndQuantity
import com.loopers.domain.user.UserId
import com.loopers.infrastructure.point.UserPointJpaRepository
import com.loopers.infrastructure.product.ProductJpaRepository
import jakarta.persistence.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import java.math.BigDecimal
import java.time.ZonedDateTime

@IntegrationTest
class OrderPayFacadeTest(
    private val sut: OrderPayFacade,
    private val orderCreateFacade: OrderCreateFacade,
) {

    @Autowired
    private lateinit var userPointJpaRepository: UserPointJpaRepository

    @Autowired
    private lateinit var orderPayFacade: OrderPayFacade

    @Autowired
    private lateinit var productJpaRepository: ProductJpaRepository

    @Test
    fun `존재하지 않는 주문이면 EntityNotFoundException을 던진다`() {
        // arrange
        val userId = UserId(1L)
        val criteria = OrderPayFacade.Criteria(
            orderId = -1L,
            targets = listOf(
                OrderPayFacade.Criteria.PaymentMethodTypeAndAmount(
                    type = PaymentMethod.Type.USER_POINT,
                    amount = 2000L.toBigDecimal(),
                ),
            ),
        )
        // act
        // assert
        assertThatThrownBy {
            sut.pay(userId, criteria = criteria)
        }
            .isInstanceOf(EntityNotFoundException::class.java)
    }

    @Test
    fun `주문 성공 시, 모든 처리는 정상 반영되어야 한다`() {
        // arrange
        val userId = UserId(3L)
        userPointJpaRepository.save(UserPoint(userId = userId, point = BigDecimal.valueOf(Long.MAX_VALUE)))
        val stock = 1L
        val product = productJpaRepository.save(Product(name = "Miranda Moore", brandId = 5968, displayedAt = ZonedDateTime.now(), maxQuantity = 2, price = 1000.toBigDecimal(), stock = stock))
        val create = orderCreateFacade.create(userId, listOf(IdAndQuantity(productId = product.id, quantity = 1)))
        val criteria = OrderPayFacade.Criteria(
            orderId = create.id,
            targets = listOf(
                OrderPayFacade.Criteria.PaymentMethodTypeAndAmount(
                    type = PaymentMethod.Type.USER_POINT,
                    amount = create.totalPrice,
                ),
            ),
        )
        // act
        val payResult = sut.pay(userId, criteria = criteria)
        // assert
        assertThat(payResult.orderId).isEqualTo(create.id)
        assertThat(payResult.payments[0].type).isEqualTo(Payment.Type.PAID)
        assertThat(payResult.payments[0].amount).isEqualTo(create.totalPrice)
        // 반영확인
        // TODO: 쿠폰 반영 확인
        val actualPoint = userPointJpaRepository.findForUpdateByUserId(userId = userId)!!
        assertThat(actualPoint.point).isEqualByComparingTo(Long.MAX_VALUE.toBigDecimal() - 1000.toBigDecimal())
        val productActual = productJpaRepository.findByIdOrNull(product.id)!!
        assertThat(productActual.stock).isEqualTo(0)
    }

    @Test
    fun `재고가 존재하지 않거나 부족할 경우 주문은 실패해야 한다`() {
        // arrange
        val userId = UserId(1L)
        userPointJpaRepository.save(UserPoint(userId = userId, point = BigDecimal.valueOf(Long.MAX_VALUE)))
        val product = productJpaRepository.save(Product(name = "Miranda Moore", brandId = 5968, displayedAt = ZonedDateTime.now(), maxQuantity = 2, price = 1000.toBigDecimal(), stock = 0))
        val create = orderCreateFacade.create(userId, listOf(IdAndQuantity(productId = product.id, quantity = 1)))
        val criteria = OrderPayFacade.Criteria(
            orderId = create.id,
            targets = listOf(
                OrderPayFacade.Criteria.PaymentMethodTypeAndAmount(
                    type = PaymentMethod.Type.USER_POINT,
                    amount = create.totalPrice,
                ),
            ),
        )
        // act
        // assert
        assertThatThrownBy { orderPayFacade.pay(userId, criteria = criteria) }.isInstanceOf(IllegalStateException::class.java)
        // 롤백처리 확인
        // TODO: 쿠폰 롤백처리 확인
        val actualPoint = userPointJpaRepository.findForUpdateByUserId(userId)!!
        assertThat(actualPoint.point).isEqualByComparingTo(Long.MAX_VALUE.toBigDecimal())
    }

    @Test
    fun `주문 시 유저의 포인트 잔액이 부족할 경우 주문은 실패해야 한다`() {
        // arrange
        val userId = UserId(2L)
        userPointJpaRepository.save(UserPoint(userId = userId))
        val stock = 1L
        val product = productJpaRepository.save(Product(name = "Miranda Moore", brandId = 5968, displayedAt = ZonedDateTime.now(), maxQuantity = 2, price = 1000.toBigDecimal(), stock = stock))
        val create = orderCreateFacade.create(userId, listOf(IdAndQuantity(productId = product.id, quantity = 1)))
        val criteria = OrderPayFacade.Criteria(
            orderId = create.id,
            targets = listOf(
                OrderPayFacade.Criteria.PaymentMethodTypeAndAmount(
                    type = PaymentMethod.Type.USER_POINT,
                    amount = create.totalPrice,
                ),
            ),
        )
        // act
        // assert
        assertThatThrownBy { orderPayFacade.pay(userId, criteria = criteria) }.isInstanceOf(IllegalStateException::class.java)
        // 롤백처리 확인
        // TODO: 쿠폰 롤백처리 확인
        val actualProduct = productJpaRepository.findByIdOrNull(product.id)!!
        assertThat(actualProduct.stock).isEqualTo(1)
    }

    @Test
    fun `동일한 유저가 서로 다른 주문을 동시에 수행해도, 포인트가 정상적으로 차감되어야 한다`() {
        // arrange
        val userId = UserId(2L)
        val userPoint = userPointJpaRepository.save(UserPoint(userId = userId, point = BigDecimal.valueOf(Long.MAX_VALUE)))
        val stock = 2L
        val price = 20000.toBigDecimal()
        val product = productJpaRepository.save(Product(name = "Miranda Moore", brandId = 5968, displayedAt = ZonedDateTime.now(), maxQuantity = 2, price = price, stock = stock))
        val criteriaList = IntRange(1, 2).map { orderCreateFacade.create(userId, listOf(IdAndQuantity(productId = product.id, quantity = 1))) }
            .map {
                OrderPayFacade.Criteria(
                    orderId = it.id,
                    targets = listOf(
                        OrderPayFacade.Criteria.PaymentMethodTypeAndAmount(
                            type = PaymentMethod.Type.USER_POINT,
                            amount = it.totalPrice,
                        ),
                    ),
                )
            }
        val acts = criteriaList.map { { sut.pay(userId, it) } }
        // act
        concurrency(acts)
        // assert
        val actual = userPointJpaRepository.findByIdOrNull(userPoint.id)!!
        assertThat(actual.point).isEqualByComparingTo(Long.MAX_VALUE.toBigDecimal() - 40000.toBigDecimal())
    }

    @Test
    fun `동일한 상품에 대해 여러 주문이 동시에 요청되어도, 재고가 정상적으로 차감되어야 한다`() {
        // arrange
        val userId = UserId(2L)
        val stock = 2L
        val price = 20000.toBigDecimal()
        val product = productJpaRepository.save(Product(name = "Miranda Moore", brandId = 5968, displayedAt = ZonedDateTime.now(), maxQuantity = 2, price = price, stock = stock))
        val criteriaList = IntRange(1, 2).map { orderCreateFacade.create(userId, listOf(IdAndQuantity(productId = product.id, quantity = 1))) }
            .map {
                OrderPayFacade.Criteria(
                    orderId = it.id,
                    targets = listOf(
                        OrderPayFacade.Criteria.PaymentMethodTypeAndAmount(
                            type = PaymentMethod.Type.USER_POINT,
                            amount = it.totalPrice,
                        ),
                    ),
                )
            }
        val acts = criteriaList.map { { sut.pay(userId, it) } }
        // act
        concurrency(acts)
        // assert
        val actual = productJpaRepository.findByIdOrNull(product.id)!!
        assertThat(actual.stock).isEqualTo(0)
    }
}
