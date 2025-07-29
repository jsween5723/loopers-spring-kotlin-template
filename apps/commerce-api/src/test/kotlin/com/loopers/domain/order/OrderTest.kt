package com.loopers.domain.order

import com.loopers.domain.payment.Paid
import com.loopers.domain.payment.PaymentInstrument
import com.loopers.domain.product.LineItem
import com.loopers.domain.user.UserId
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.instancio.Instancio
import org.instancio.Select.field
import org.junit.jupiter.api.Test

class OrderTest {
    @Test
    fun `주문은 총 가격을 계산할 수 있다`() {
        // arrange
        val order = Instancio.of(Order::class.java)
            .create()
        order.updateLineItems(
            listOf(
                Instancio.of(LineItem::class.java)
                    .set(field("price"), 2000.toBigDecimal())
                    .create(),
            ),
        )
        // act
        val actual = order.totalPrice
        // assert
        assertThat(actual).isEqualByComparingTo(2000.toBigDecimal())
    }

    @Test
    fun `주문은 결제수단을 통해 결제를 진행하고 남은 금액을 확인할 수 있다`() {
        // arrange
        val order = Order()
        order.updateLineItems(
            listOf(
                Instancio.of(LineItem::class.java)
            .set(field("price"), 2000.toBigDecimal())
            .create(),
            ),
        )
        val instrument = mockk<PaymentInstrument>()
        val amount = 1500L.toBigDecimal()
        every { instrument.pay(amount) }.returns(Paid(userId = UserId(id = 5927), amount = amount, instrumentType = PaymentInstrument.Type.USER_POINT))
        // act
        order.payWith(instrument, amount)
        // assert
        assertThat(order.remainPrice).isEqualTo(500.toBigDecimal())
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
        val instrument = mockk<PaymentInstrument>()
        val amount = 2500L.toBigDecimal()
        every { instrument.pay(amount) }.returns(Paid(userId = UserId(id = 5927), amount = amount, instrumentType = PaymentInstrument.Type.USER_POINT))
        // act
        // assert
        assertThatThrownBy { order.payWith(instrument, amount) }
            .isInstanceOf(IllegalStateException::class.java)
    }
}
