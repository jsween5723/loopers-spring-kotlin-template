package com.loopers.domain.productlike

import com.loopers.domain.product.Product
import com.loopers.domain.user.UserId

interface ProductLikeRepository {
    fun save(like: ProductLike): ProductLike
    fun delete(like: ProductLike)
    fun findByUserIdAndProductIn(userId: UserId, products: List<Product>): List<ProductLike>
    fun findByUserIdAndProduct(userId: UserId, product: Product): ProductLike?
}
