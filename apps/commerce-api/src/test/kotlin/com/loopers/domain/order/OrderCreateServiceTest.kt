package com.loopers.domain.order

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OrderCreateServiceTest {
    private val sut: OrderCreateService = OrderCreateService()

    @Test
    fun `주문을 생성할 수 있다`() {
        // arrange
        val request = listOf(
            LineItem(productId = 9533, quantity = 8854, productName = "Winfred McIntyre", brandId = 1576, price = 2000.toBigDecimal()),
        )
        // act
        val actual = sut.create(request)
        // assert
        assertThat(actual).isInstanceOf(Order::class.java)
    }
}
