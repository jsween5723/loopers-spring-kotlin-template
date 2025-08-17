package com.loopers.domain.orderpayment

import com.loopers.application.order.OrderPaymentService
import com.loopers.application.order.UserPointPay
import com.loopers.domain.order.LineItem
import com.loopers.domain.order.Order
import com.loopers.domain.point.UserPoint
import com.loopers.domain.user.UserId
import org.assertj.core.api.Assertions.assertThat
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
        )
    }

    @Test
    fun `주문은 결제수단을 통해 결제를 진행하고 결제 인스턴스를 반환한다`() {
        // arrange
        val order = Order()
        order.changeTo(
            listOf(LineItem(productId = 6053, quantity = 1, productName = "Lakisha Fisher", brandId = 6293, price = 1500.toBigDecimal())),
        )
        val amount = 1500L.toBigDecimal()
        val method = method(amount)
        // act
        val actual = sut.pay(
            order = order,
            paymentMethod = method,
        )
        // assert
        assertThat(actual.info.amount).isEqualTo(1500L.toBigDecimal())
    }
}
