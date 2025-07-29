package com.loopers.application.product

import com.loopers.domain.IntegrationTest
import com.loopers.domain.brand.BrandRepository
import com.loopers.domain.product.Product
import com.loopers.domain.product.ProductQuery
import com.loopers.domain.product.ProductRepository
import com.loopers.domain.product.SortFor
import com.loopers.domain.user.UserId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.ZonedDateTime

@IntegrationTest
class ProductSearchTest(
    private val sut: ProductFacade,
    private val productRepository: ProductRepository,
    private val productLikeFacade: ProductLikeFacade,
    private val brandRepository: BrandRepository,
) {
    private val userId = UserId(2L)

    @BeforeEach
    fun prepare() {
        productRepository.deleteAllInBatch()
        brandRepository.deleteAllInBatch()
    }

    @Test
    fun `브랜드 id로 필터링 할 수 있다`() {
        // arrange
        val one = insertProduct(createProduct(brandId = 1L))
        val two = insertProduct(createProduct(brandId = 1L))
        val three = insertProduct(createProduct(brandId = 2L))
        // act
        val actual = sut.search(userId = userId, ProductQuery(brandId = 1L))
        // assert
        assertThat(actual.products.size).isEqualTo(2)
    }

    @Test
    fun `가격순으로 정렬할 수 있다`() {
        // arrange
        val one = insertProduct(createProduct(price = 1000.toBigDecimal()))
        val two = insertProduct(createProduct(price = 2000.toBigDecimal()))
        val three = insertProduct(createProduct(price = 3000.toBigDecimal()))
        // act
        val actual = sut.search(userId = userId, ProductQuery(sort = SortFor.PRICE_ASC))
        // assert
        assertThat(actual.products.map { it.id }).isEqualTo(listOf(one, two, three).map { it.id })
    }

    @Test
    fun `좋아요 순으로 정렬할 수 있다`() {
        // arrange
        val one = insertProduct(createProduct(price = 1000.toBigDecimal()), 3)
        val two = insertProduct(createProduct(price = 2000.toBigDecimal()), 2)
        val three = insertProduct(createProduct(price = 3000.toBigDecimal()), 1)
        // act
        val actual = sut.search(userId = userId, ProductQuery(sort = SortFor.LIKES_ASC))
        // assert
        assertThat(actual.products.map { it.id }).isEqualTo(listOf(three, two, one).map { it.id })
    }

    private fun insertProduct(product: Product, likeCount: Int = 1): Product =
        productRepository.save(product).also {
            increaseLike(userId = userId, product = it, number = likeCount)
        }
    private fun increaseLike(userId: UserId, product: Product, number: Int) = repeat(number) { productLikeFacade.add(userId = userId, productId = product.id) }

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
