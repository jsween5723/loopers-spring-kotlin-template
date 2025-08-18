package com.loopers.domain.product

import com.loopers.domain.shared.CacheKey
import java.time.Duration

sealed class ProductKey(override val ttl: Duration) : CacheKey {
    abstract override val key: String
    data class GetProduct(private val id: Long) : ProductKey(ttl = Duration.ofMinutes(60)) {
        override val key: String = "get-product-detail-v1:$id"
    }
}
