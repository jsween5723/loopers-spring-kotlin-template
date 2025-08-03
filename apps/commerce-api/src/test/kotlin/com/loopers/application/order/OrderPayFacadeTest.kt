package com.loopers.application.order

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
    fun `존재하는 주문이면 성공한다`() {
        // arrange
        val userId = UserId(1L)
        val create = orderCreateFacade.create(userId, listOf())
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
        val actualPoint = userPointJpaRepository.findByUserId(userId)!!
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
}
