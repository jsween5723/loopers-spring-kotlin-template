package com.loopers.domain.product

import com.loopers.domain.IntegrationTest
import com.loopers.domain.brand.Brand
import com.loopers.domain.shared.ProductIdAndQuantity
import jakarta.persistence.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
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
        val product = Product(
            brand = Brand(name = "Nathaniel Keith"),
            displayedAt = ZonedDateTime.now(),
            maxQuantity = 1367,
            price = 1000.toBigDecimal(),
            stock = 1367,
            name = "Jan Roberson",
        )
        val saved = productRepository.save(product)
        val request = listOf(
            saved.let { ProductIdAndQuantity(productId = it.id, quantity = it.maxQuantity) },
        )
        // act
        val actual = productService.selectProducts(request)
        // assert
        val selectedProduct = productRepository.findByIdOrNull(product.id)
        assertThat(actual).extracting("quantity")
            .isEqualTo(listOf(1367L))
        assertThat(requireNotNull(selectedProduct).stock).isEqualTo(product.stock)
    }

    @Test
    fun `선택 시 존재하지 않는 상품이면 EntityNotFoundException을 던진다`() {
        val request = ProductIdAndQuantity(productId = -1L, quantity = 2000)
        // act
        assertThatThrownBy { productService.selectProducts(listOf(request)) }
            .isInstanceOf(EntityNotFoundException::class.java)
    }

    @Test
    fun `재고차감 시 상품정보와 수량을 반환하고 재고가 차감된다`() {
        // arrange
        val product = Product(
            brand = Brand(name = "Nathaniel Keith"),
            displayedAt = ZonedDateTime.now(),
            maxQuantity = 1367,
            price = 1000.toBigDecimal(),
            stock = 10,
            name = "Jan Roberson",
        )
        val saved = productRepository.save(product)
        val request = listOf(
            saved.let { ProductIdAndQuantity(productId = it.id, quantity = 5) },
        )
        // act
        val actual = productService.deductProducts(request)
        // assert
        val selectedProduct = productRepository.findByIdOrNull(product.id)
        assertThat(actual).extracting("quantity").isEqualTo(listOf(5L))
        assertThat(requireNotNull(selectedProduct).stock).isEqualTo(5)
    }

    @Test
    fun `재고차감 시 존재하지 않는 상품이면 EntityNotFoundException을 던진다`() {
        val request = ProductIdAndQuantity(productId = -1L, quantity = 2000)
        // act
        assertThatThrownBy { productService.deductProducts(listOf(request)) }
            .isInstanceOf(EntityNotFoundException::class.java)
    }
}
