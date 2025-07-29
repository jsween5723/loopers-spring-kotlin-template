package com.loopers.application.product

import com.loopers.domain.product.ProductRepository
import com.loopers.domain.product.ProductSignalService
import com.loopers.domain.productlike.ProductLike
import com.loopers.domain.productlike.ProductLikeRepository
import com.loopers.domain.user.UserId
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ProductLikeFacade(
    private val productRepository: ProductRepository,
    private val productLikeRepository: ProductLikeRepository,
    private val productSignalService: ProductSignalService,
) {
    @Transactional
    fun add(userId: UserId, productId: Long) {
        val product = productRepository.findByIdOrNull(productId)
            ?: throw EntityNotFoundException("$productId 는 존재하지 않는 상품입니다.")
        if (productLikeRepository.existsByUserIdAndProduct(userId, product)) {
            return
        }
        productLikeRepository.save(ProductLike(product, userId))
        productSignalService.increaseLikeCount(product)
    }

    @Transactional
    fun remove(userId: UserId, productId: Long) {
        val product = productRepository.findByIdOrNull(productId)
            ?: throw EntityNotFoundException("$productId 는 존재하지 않는 상품입니다.")
        if (!productLikeRepository.existsByUserIdAndProduct(userId, product)) {
            return
        }
        productLikeRepository.delete(ProductLike(product, userId))
        productSignalService.decreaseLikeCount(product)
    }
}
