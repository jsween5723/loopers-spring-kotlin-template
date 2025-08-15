package com.loopers.domain.productlike

import com.loopers.domain.shared.CacheKey
import com.loopers.domain.user.UserId
import java.time.Duration

sealed class ProductLikeKey(override val ttl: Duration) : CacheKey {
    abstract override val key: String

    data class GetProductLikes(private val userId: UserId) : ProductLikeKey(Duration.ofMinutes(120)) {
        override val key: String = "get-user-product-like-v1:${userId.id}"
    }
}
