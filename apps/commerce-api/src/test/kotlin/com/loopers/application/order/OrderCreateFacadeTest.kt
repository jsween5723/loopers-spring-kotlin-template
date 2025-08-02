package com.loopers.application.order

import com.loopers.domain.IntegrationTest
import com.loopers.domain.product.Product
import com.loopers.domain.product.ProductRepository
import com.loopers.domain.shared.ProductIdAndQuantity
import com.loopers.domain.user.UserId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

@IntegrationTest
class OrderCreateFacadeTest(
    private val sut: OrderCreateFacade,
    private val productRepository: ProductRepository,
) {
    @Test
    fun `주문 생성에 성공한다`() {
        // arrange
        val userId = UserId(id = 1L)
        val product = Product(name = "Benita Conrad", brandId = 1L, displayedAt = ZonedDateTime.now(), maxQuantity = 5910, price = 2000.toBigDecimal(), stock = 1584)
        val saved = productRepository.saveAndFlush(product)
        val listOf = listOf(ProductIdAndQuantity(productId = saved.id, quantity = 5L))
        // act
        val actual = sut.create(userId, listOf)
        // assert
        assertThat(actual.lineItems[0].quantity).isEqualTo(5)
        assertThat(actual.lineItems[0].product.id).isEqualTo(saved.id)
    }
}
