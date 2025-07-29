package com.loopers.domain.productlike

import com.loopers.domain.product.Product
import com.loopers.domain.user.UserId
import org.springframework.data.jpa.repository.JpaRepository

interface ProductLikeRepository : JpaRepository<ProductLike, Long> {
    fun existsByUserIdAndProduct(userId: UserId, product: Product): Boolean
}
