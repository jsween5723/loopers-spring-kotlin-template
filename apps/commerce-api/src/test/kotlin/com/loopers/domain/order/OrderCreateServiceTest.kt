package com.loopers.domain.order

import com.loopers.domain.product.Product
import com.loopers.domain.shared.ProductAndQuantity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

class OrderCreateServiceTest {
    private val sut: OrderCreateService = OrderCreateService()

    @Test
    fun `주문을 생성할 수 있다`() {
        // arrange
        val request = listOf(
            ProductAndQuantity(product = Product(name = "Marci Hutchinson", brandId = 2845, displayedAt = ZonedDateTime.now(), maxQuantity = 7824, price = 2000.toBigDecimal(), stock = 5242), quantity = 2L),
        )
        // act
        val actual = sut.create(request)
        // assert
        assertThat(actual).isInstanceOf(Order::class.java)
    }
}
