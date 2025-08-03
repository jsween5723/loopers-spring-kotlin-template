package com.loopers.application.product

import com.loopers.domain.IntegrationTest
import com.loopers.domain.brand.Brand
import com.loopers.domain.brand.BrandRepository
import com.loopers.domain.product.Product
import com.loopers.domain.product.ProductQuery
import com.loopers.domain.product.ProductRepository
import com.loopers.domain.product.ProductSignal
import com.loopers.domain.product.ProductSignalRepository
import com.loopers.domain.product.SortFor
import com.loopers.domain.productlike.ProductLikeRepository
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
    private val productLikeRepository: ProductLikeRepository,
    private val brandRepository: BrandRepository,
    private val productSignalRepository: ProductSignalRepository,
) {
    private val userId = UserId(2L)

    @BeforeEach
    fun prepare() {
        productLikeRepository.deleteAllInBatch()
        productSignalRepository.deleteAllInBatch()
        productRepository.deleteAllInBatch()
        brandRepository.deleteAllInBatch()
    }

    @Test
    fun `상품 id로 상세정보를 조회할 수 있다`() {
        // arrange
        brandRepository.save(Brand(name = "Nanette Warner"))
        val one = insertProduct(createProduct(brandId = 1L))
        // act
        val actual = sut.getDetail(userId, one.id)
        // assert
        assertThat(actual.id).isEqualTo(one.id)
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
        assertThat(actual.products.map { it.likeCount }).isEqualTo(listOf(1L, 2L, 3L))
    }

    private fun insertProduct(product: Product, likeCount: Long = 1): Product {
        val saved = productRepository.save(product)
        productSignalRepository.save(ProductSignal(saved, likeCount))
        return saved
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
