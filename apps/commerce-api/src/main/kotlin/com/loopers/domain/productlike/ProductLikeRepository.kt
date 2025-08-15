package com.loopers.domain.productlike

import com.loopers.domain.product.Product
import com.loopers.domain.user.UserId

interface ProductLikeRepository {
    fun existsByUserIdAndProduct(userId: UserId, product: Product): Boolean
    fun findLikedProductIds(userId: UserId, productIds: List<Long>): Set<Long>
    fun save(like: ProductLike): ProductLike
    fun delete(like: ProductLike)
}
