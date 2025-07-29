package com.loopers.domain.product

import com.loopers.domain.IntegrationTest
import com.loopers.domain.brand.Brand
import com.loopers.domain.shared.ProductIdAndQuantity
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import java.time.ZonedDateTime
import kotlin.test.Test

@IntegrationTest
class ProductServiceTest {
    @Autowired
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Test
    fun `선택 시 상품정보와 수량을 반환하고 재고가 변화하지 않는다`() {
        // arrange
        val product = Product(brand = Brand(name = "Nathaniel Keith"), displayedAt = ZonedDateTime.now(), maxQuantity = 1367, price = 1000.toBigDecimal(), stock = 1367, name = "Jan Roberson")
        val request = productRepository.save(product)
            .let { ProductIdAndQuantity(productId = it.id, quantity = it.maxQuantity) }
        // act
        val actual = productService.selectProducts(listOf(request))
        // assert
        val expected = product.let {
            LineItem(
                productId = it.id,
                quantity = it.maxQuantity,
                productName = it.name,
                brandName = it.brand.name,
                brandId = it.brand.id,
                price = it.price,
            )
        }
        val deductedProduct = productRepository.findByIdOrNull(product.id)
        assertThat(actual).extracting("quantity").isEqualTo(listOf(expected.quantity))
        assertThat(requireNotNull(deductedProduct).stock).isEqualTo(product.stock)
    }
}
