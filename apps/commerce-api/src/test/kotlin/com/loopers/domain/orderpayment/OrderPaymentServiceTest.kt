package com.loopers.domain.orderpayment

import com.loopers.application.order.OrderPaymentService
import com.loopers.application.order.UserPointPay
import com.loopers.domain.order.Order
import com.loopers.domain.point.UserPoint
import com.loopers.domain.order.LineItem
import com.loopers.domain.user.UserId
import org.assertj.core.api.Assertions.assertThat
import org.instancio.Instancio
import org.instancio.Select.field
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class OrderPaymentServiceTest {
    private val sut = OrderPaymentService()
    private val userId = UserId(id = 5927)
    private val method = { amount: BigDecimal ->
        UserPointPay(
            userPoint = UserPoint(
                point = Long.MAX_VALUE.toBigDecimal(),
                userId = UserId(id = 4743),
            ),
            amount = amount,
        )
    }

    @Test
    fun `주문은 결제수단을 통해 결제를 진행하고 결제 인스턴스를 반환한다`() {
        // arrange
        val order = Order()
        order.changeTo(
            listOf(
                Instancio.of(LineItem::class.java)
                    .set(field("price"), 2000.toBigDecimal())
                    .create(),
            ),
        )
        val amount = 1500L.toBigDecimal()
        val method = method(amount)
        // act
        val actual = sut.pay(
            order = order,
            paymentMethods = listOf(method),
        )
        // assert
        assertThat(actual.sumOf { it.info.amount }).isEqualTo(1500L.toBigDecimal())
    }
}
