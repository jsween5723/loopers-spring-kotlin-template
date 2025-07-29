package com.loopers.domain.product

import com.loopers.domain.shared.ProductAndQuantity
import org.assertj.core.api.Assertions.assertThat
import java.time.ZonedDateTime
import kotlin.test.Test

class ProductSelectServiceTest {
    private val sut: ProductSelectService = ProductSelectService()
    private val maxQuantity: Long = 1367
    private val product = Product(
        brandId = 1L,
        displayedAt = ZonedDateTime.now(),
        maxQuantity = maxQuantity,
        price = 1000.toBigDecimal(),
        stock = maxQuantity,
        name = "Jan Roberson",
    )

    @Test
    fun `선택 시 상품정보와 수량을 반환하고 재고가 변화하지 않는다`() {
        // arrange
        val request = listOf(ProductAndQuantity(product = product, quantity = product.maxQuantity))
        // act
        val actual = sut.select(request)
        // assert
        assertThat(actual[0]).extracting("quantity")
            .isEqualTo(maxQuantity)
        assertThat(actual[0].product.stock).isEqualTo(maxQuantity)
    }
}
