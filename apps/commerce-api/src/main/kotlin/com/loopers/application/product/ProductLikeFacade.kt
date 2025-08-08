package com.loopers.application.product

import com.loopers.domain.product.ProductSignalRepository
import com.loopers.domain.productlike.ProductLike
import com.loopers.domain.productlike.ProductLikeRepository
import com.loopers.domain.user.UserId
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ProductLikeFacade(
    private val productLikeRepository: ProductLikeRepository,
    private val productSignalRepository: ProductSignalRepository,
) {
    @Transactional
    fun add(userId: UserId, productId: Long) {
        val signal = productSignalRepository.getForUpdateByProductId(productId)
        val like = productLikeRepository.findByUserIdAndProduct(userId, signal.product)
        if (like != null) {
            return
        }
        productLikeRepository.save(ProductLike(signal.product.id, userId))
        signal.increaseLikeCount()
        productSignalRepository.save(signal)
    }

    @Transactional
    fun remove(userId: UserId, productId: Long) {
        val signal = productSignalRepository.getForUpdateByProductId(productId)
        val like = productLikeRepository.findByUserIdAndProduct(userId, signal.product) ?: return
        productLikeRepository.delete(like)
        signal.decreaseLikeCount()
        productSignalRepository.save(signal)
    }
}
