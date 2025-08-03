package com.loopers.domain.orderpayment

import com.loopers.application.order.OrderPaymentService
import com.loopers.domain.order.Order
import com.loopers.domain.payment.Paid
import com.loopers.domain.payment.PaymentMethod
import com.loopers.domain.payment.PreviousPayments
import com.loopers.domain.product.LineItem
import com.loopers.domain.user.UserId
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.instancio.Instancio
import org.instancio.Select.field
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class OrderPayServiceTest {
    private val sut = OrderPaymentService()
    private val userId = UserId(id = 5927)
    private val createMethod = { amount: BigDecimal ->
        mockk<PaymentMethod>().also {
            every {
                it.pay()
            }.returns(
                Paid(
                    userId = userId,
                    amount = amount,
                    methodType = PaymentMethod.Type.USER_POINT,
                ),
            )
            every { it.amount }.returns(amount)
        }
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
        val target = createMethod(amount)
        // act
        val actual = sut.pay(
                order = order,
                paymentMethods = listOf(target),
                previousPayments = PreviousPayments(),
        )
        // assert
        assertThat(actual.sumOf { it.amount }).isEqualTo(1500L.toBigDecimal())
    }

    @Test
    fun `주문은 결제수단을 통해 결제를 진행시 남은 금액보다 많다면 IllegalStateException을 던진다`() {
        // arrange
        val order = Order()
        order.changeTo(
            listOf(
                Instancio.of(LineItem::class.java)
                    .set(field("price"), 2000.toBigDecimal())
                    .create(),
            ),
        )
        val amount = 2500L.toBigDecimal()
        val target = createMethod(amount)
        // act
        // assert
        assertThatThrownBy {
            sut.pay(order = order, previousPayments = PreviousPayments(), paymentMethods = listOf(target))
        }
            .isInstanceOf(IllegalStateException::class.java)
    }
}
