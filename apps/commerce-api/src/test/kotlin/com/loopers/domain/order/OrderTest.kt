package com.loopers.domain.order

import org.assertj.core.api.Assertions.assertThat
import org.instancio.Instancio
import org.instancio.Select.field
import org.junit.jupiter.api.Test

class OrderTest {
    @Test
    fun `주문은 총 가격을 계산할 수 있다`() {
        // arrange
        val order = Order()
        order.changeTo(
            listOf(
                Instancio.of(LineItem::class.java)
                    .set(field("price"), 2000.toBigDecimal())
                    .set(field("quantity"), 1)
                    .create(),
            ),
        )
        // act
        val actual = order.totalPrice
        // assert
        assertThat(actual).isEqualByComparingTo(2000.toBigDecimal())
    }
}
