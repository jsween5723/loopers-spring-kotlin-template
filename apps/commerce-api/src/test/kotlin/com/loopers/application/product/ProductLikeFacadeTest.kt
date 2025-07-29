package com.loopers.application.product

import com.loopers.domain.IntegrationTest
import com.loopers.domain.product.Product
import com.loopers.domain.product.ProductRepository
import com.loopers.domain.productlike.ProductLike
import com.loopers.domain.productlike.ProductLikeRepository
import com.loopers.domain.user.UserId
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

    val product: Product = Product(name = "Jerry Paul", brandId = 1L, displayedAt = ZonedDateTime.now(), maxQuantity = 7327, price = 2000.toBigDecimal(), stock = 2556)
    val userId: UserId = UserId(2L)

    @Test
    fun `좋아요 추가에 성공한다`() {
        // arrange
        val product = productRepository.save(product)
        // act
        // assert
        assertDoesNotThrow { sut.add(userId, product.id) }
    }

    @Test
    fun `이미 좋아요했어도 아무동작하지 않고 성공한다`() {
        // arrange
        val product = productRepository.save(product)
        productLikeRepository.save(ProductLike(product = product, userId = userId))
        // act
        // assert
        assertDoesNotThrow { sut.add(userId, product.id) }
    }

    @Test
    fun `좋아요 삭제에 성공한다`() {
        // arrange
        val product = productRepository.save(product)
        sut.add(userId, product.id)
        // act
        // assert
        assertDoesNotThrow { sut.remove(userId, product.id) }
    }

    @Test
    fun `좋아요가 없어도 아무동작하지 않고 성공한다`() {
        // arrange
        val product = productRepository.save(product)
        // act
        // assert
        assertDoesNotThrow { sut.remove(userId, product.id) }
    }
}
