package com.loopers.domain.orderpayment

import com.loopers.domain.order.Order
import com.loopers.domain.payment.Paid
import com.loopers.domain.payment.PaymentInstrument
import com.loopers.domain.product.LineItem
import com.loopers.domain.shared.PayTarget
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
    private val sut = OrderPayService()
    private val userId = UserId(id = 5927)
    private val createTarget = { amount: BigDecimal ->
        PayTarget(mockk<PaymentInstrument>(), amount = amount).also {
            every {
                it.instrument.pay(
                    userId = userId,
                    amount = amount,
                )
            }.returns(
                Paid(
                    userId = userId,
                    amount = amount,
                    instrumentType = PaymentInstrument.Type.USER_POINT,
                ),
            )
        }
    }

    @Test
    fun `주문은 결제수단을 통해 결제를 진행하고 결제 인스턴스를 반환한다`() {
        // arrange
        val order = Order()
        order.updateLineItems(
            listOf(
                Instancio.of(LineItem::class.java)
                    .set(field("price"), 2000.toBigDecimal())
                    .create(),
            ),
        )
        val amount = 1500L.toBigDecimal()
        val target = createTarget(amount)
        // act
        val actual = sut.payOrder(
            OrderPayService.Command(
                userId = userId,
                order = order,
                targets = listOf(target),
                previousPayments = OrderPayments(),
            ),
        )
        // assert
        assertThat(actual[0].order).isEqualTo(order)
        assertThat(actual[0].payment.amount).isEqualTo(1500L.toBigDecimal())
    }

    @Test
    fun `주문은 결제수단을 통해 결제를 진행시 남은 금액보다 많다면 IllegalStateException을 던진다`() {
        // arrange
        val order = Order()
        order.updateLineItems(
            listOf(
                Instancio.of(LineItem::class.java)
                    .set(field("price"), 2000.toBigDecimal())
                    .create(),
            ),
        )
        val amount = 2500L.toBigDecimal()
        val target = createTarget(amount)
        // act
        // assert
        assertThatThrownBy {
            sut.payOrder(
                OrderPayService.Command(
                    userId = userId,
                    order = order,
                    targets = listOf(target),
                    previousPayments = OrderPayments(),
                ),
            )
        }
            .isInstanceOf(IllegalStateException::class.java)
    }
}
