package com.loopers.domain.product

import com.loopers.domain.shared.IdAndQuantity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.test.util.ReflectionTestUtils
import java.time.ZonedDateTime

class ProductDeductTest {
    private val sut = ProductDeductService()
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
        ReflectionTestUtils.setField(product, "id", 1L)
        val request = listOf(product)
        val qtys = listOf(IdAndQuantity(1L, product.stock))
        // act
        val actual = sut.deduct(request, qtys)
        // assert
        assertThat(actual[0].stock).isEqualTo(0)
    }
}
