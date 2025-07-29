package com.loopers.domain.order

import com.loopers.domain.brand.Brand
import com.loopers.domain.product.LineItem
import com.loopers.domain.product.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

class OrderCreateServiceTest {
    private val sut: OrderCreateService = OrderCreateService()

    @Test
    fun `주문을 생성할 수 있다`() {
        // arrange
        val request = listOf(
            LineItem(
                quantity = 5953,
                productName = "Josie Flynn",
                brandName = "Nanette Summers",
                brandId = 7258,
                price = 2000.toBigDecimal(),
            product = Product(
                name = "Tonia Bryant",
                brand = Brand(name = "Anne McKay"),
                displayedAt = ZonedDateTime.now(),
                maxQuantity = 4627,
                price = 2000.toBigDecimal(),
                stock = 8332,
            ),
        ),
        )
        // act
        val actual = sut.create(request)
        // assert
        assertThat(actual).isInstanceOf(Order::class.java)
    }
}
