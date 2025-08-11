package com.loopers.application.product

import com.loopers.concurrency
import com.loopers.domain.IntegrationTest
import com.loopers.domain.product.Product
import com.loopers.domain.product.ProductRepository
import com.loopers.domain.product.ProductSignal
import com.loopers.domain.product.ProductSignalRepository
import com.loopers.domain.productlike.ProductLike
import com.loopers.domain.productlike.ProductLikeRepository
import com.loopers.domain.user.UserId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import java.time.ZonedDateTime

@IntegrationTest
class ProductLikeFacadeTest {
    @Autowired
    private lateinit var sut: ProductLikeFacade

    @Autowired
    private lateinit var productLikeRepository: ProductLikeRepository

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var productSignalRepository: ProductSignalRepository

    val unsavedProduct: Product = Product(name = "Jerry Paul", brandId = 1L, displayedAt = ZonedDateTime.now(), maxQuantity = 7327, price = 2000.toBigDecimal(), stock = 2556)
    val userId: UserId = UserId(2L)

    @Test
    fun `좋아요 추가에 성공한다`() {
        // arrange
        val product = productRepository.save(unsavedProduct)
        productSignalRepository.save(ProductSignal(product, 0))
        // act
        // assert
        assertDoesNotThrow { sut.add(userId, product.id) }
        val actual = productSignalRepository.getByProductId(product.id)
        assertThat(actual.likeCount).isEqualTo(1)
    }

    @Test
    fun `이미 좋아요했어도 아무동작하지 않고 성공한다`() {
        // arrange
        val product = productRepository.save(unsavedProduct)
        productSignalRepository.save(ProductSignal(product, 0))
        productLikeRepository.save(ProductLike(productId = product.id, userId = userId))
        // act
        // assert
        assertDoesNotThrow { sut.add(userId, product.id) }
    }

    @Test
    fun `좋아요 삭제에 성공한다`() {
        // arrange
        val product = productRepository.save(unsavedProduct)
        productSignalRepository.save(ProductSignal(product, 0))
        sut.add(userId, product.id)
        // act
        // assert
        assertDoesNotThrow { sut.remove(userId, product.id) }
    }

    @Test
    fun `좋아요가 없어도 아무동작하지 않고 성공한다`() {
        // arrange
        val product = productRepository.save(unsavedProduct)
        productSignalRepository.save(ProductSignal(product, 0))
        // act
        // assert
        assertDoesNotThrow { sut.remove(userId, product.id) }
    }

    @Test
    fun `동일한 상품에 대해 여러명이 좋아요를 요청해도, 상품의 좋아요 개수가 정상 반영되어야 한다`() {
        // arrange
        val product = productRepository.save(unsavedProduct)
        productSignalRepository.save(ProductSignal(product, 0))
        val acts = LongRange(1, 4).map { UserId(it) }
            .map {
                return@map {
                    sut.add(it, product.id)
                }
            }
        // act
        concurrency(acts)
        // assert
        val actual = productSignalRepository.getByProductId(product.id)
        assertThat(actual.likeCount).isEqualTo(4)
    }

    @Test
    fun `동일한 상품에 대해 여러명이 싫어요를 요청해도, 상품의 좋아요 개수가 정상 반영되어야 한다`() {
        // arrange
        val product = productRepository.save(unsavedProduct)
        productSignalRepository.save(ProductSignal(product, 4))
        val acts = LongRange(5, 8).map { UserId(it) }
            .map {
                return@map {
                    productLikeRepository.save(ProductLike(productId = product.id, userId = it))
                    sut.remove(it, product.id)
                }
            }
        // act
        concurrency(acts)
        // assert
        val actual = productSignalRepository.getByProductId(product.id)
        assertThat(actual.likeCount).isEqualTo(0)
    }

    @Test
    fun `동일한 상품에 대해 여러명이 좋아요 싫어요를 요청해도, 상품의 좋아요 개수가 정상 반영되어야 한다`() {
        // arrange
        val product = productRepository.save(unsavedProduct)
        productSignalRepository.save(ProductSignal(product, 2))
        val acts = LongRange(1, 2).map { UserId(it) }
            .map {
                return@map {
                    productLikeRepository.save(ProductLike(productId = product.id, userId = it))
                    sut.remove(it, product.id)
                }
            }
        val acts2 = LongRange(3, 4).map { UserId(it) }
            .map {
                return@map {
                    sut.add(it, product.id)
                }
            }
        // act
        // act
        concurrency(acts + acts2)
        // assert
        val actual = productSignalRepository.getByProductId(product.id)
        assertThat(actual.likeCount).isEqualTo(2)
    }
}
