package com.loopers.application.order

import com.loopers.domain.IntegrationTest
import com.loopers.domain.payment.Payment
import com.loopers.domain.payment.PaymentMethod
import com.loopers.domain.user.UserId
import jakarta.persistence.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

@IntegrationTest
class OrderPayFacadeTest(
    private val sut: OrderPayFacade,
    private val orderCreateFacade: OrderCreateFacade,
) {

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
}
