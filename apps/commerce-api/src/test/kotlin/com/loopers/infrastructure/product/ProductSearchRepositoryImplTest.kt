package com.loopers.infrastructure.product

import com.loopers.domain.IntegrationTest
import com.loopers.domain.product.Product
import com.loopers.domain.product.ProductQuery
import com.loopers.domain.product.ProductRepository
import com.loopers.domain.product.ProductSearchRepository
import com.loopers.domain.product.ProductSignal
import com.loopers.domain.product.ProductSignalRepository
import com.loopers.domain.product.SortFor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.ZonedDateTime

@IntegrationTest
class ProductSearchRepositoryImplTest(
    private val sut: ProductSearchRepository,
    private val productRepository: ProductRepository,
    private val productSignalRepository: ProductSignalRepository,
) {

    @Test
    fun `브랜드 id로 필터링 할 수 있다`() {
        // arrange

        val one = insertProduct(createProduct(brandId = 1L))
        val two = insertProduct(createProduct(brandId = 1L))
        val three = insertProduct(createProduct(brandId = 2L))
        // act
        val actual = sut.search(ProductQuery(brandId = 1L))
        // assert
        assertThat(actual.size).isEqualTo(2)
    }

    @Test
    fun `가격순으로 정렬할 수 있다`() {
        // arrange
        val one = insertProduct(createProduct(price = 1000.toBigDecimal()))
        val two = insertProduct(createProduct(price = 2000.toBigDecimal()))
        val three = insertProduct(createProduct(price = 3000.toBigDecimal()))
        // act
        val actual = sut.search(ProductQuery(sort = SortFor.PRICE_ASC))
        // assert
        assertThat(actual.map { it.product.id }).isEqualTo(listOf(one, two, three).map { it.id })
    }

    @Test
    fun `좋아요 순으로 정렬할 수 있다`() {
        // arrange
        val one = insertProduct(createProduct(price = 1000.toBigDecimal()), 3L)
        val two = insertProduct(createProduct(price = 2000.toBigDecimal()), 2L)
        val three = insertProduct(createProduct(price = 3000.toBigDecimal()), 1L)
        // act
        val actual = sut.search(ProductQuery(sort = SortFor.LIKES_ASC))
        // assert
        assertThat(actual.map { it.product.id }).isEqualTo(listOf(three, two, one).map { it.id })
    }

    private fun insertProduct(product: Product, likeCount: Long = 0L): Product = productRepository.save(product)
        .also {
            productSignalRepository.save(
                ProductSignal(
                    product = product,
                    likeCount = likeCount,
                ),
            )
        }

    private fun createProduct(
        name: String = "Ismael Flowers",
        price: BigDecimal = 2000.toBigDecimal(),
        displayedAtBeforeDay: Long = 0,
        brandId: Long = 1L,
    ): Product = Product(
        name = name,
        brandId = brandId,
        displayedAt = ZonedDateTime.now()
            .minusDays(displayedAtBeforeDay),
        maxQuantity = 7357,
        price = price,
        stock = 9706,
    )
}
