package com.loopers.application.product

import com.loopers.domain.product.ProductSignalRepository
import com.loopers.domain.productlike.ProductLike
import com.loopers.domain.productlike.ProductLikeRepository
import com.loopers.domain.user.UserId
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ProductLikeFacade(
    private val productLikeRepository: ProductLikeRepository,
    private val productSignalRepository: ProductSignalRepository,
) {
    @Transactional
    fun add(userId: UserId, productId: Long) {
        val signal = productSignalRepository.findByProductId(productId)
            ?: throw EntityNotFoundException("$productId 는 존재하지 않는 상품입니다.")
        if (productLikeRepository.existsByUserIdAndProduct(userId, signal.product)) {
            return
        }
        productLikeRepository.save(ProductLike(signal.product, userId))
        signal.increaseLikeCount()
    }

    @Transactional
    fun remove(userId: UserId, productId: Long) {
        val signal = productSignalRepository.findByProductId(productId)
            ?: throw EntityNotFoundException("$productId 는 존재하지 않는 상품입니다.")
        if (!productLikeRepository.existsByUserIdAndProduct(userId, signal.product)) {
            return
        }
        productLikeRepository.delete(ProductLike(signal.product, userId))
        signal.decreaseLikeCount()
    }
}
