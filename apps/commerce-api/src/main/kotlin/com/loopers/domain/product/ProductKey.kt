package com.loopers.domain.product

import com.loopers.domain.shared.CacheKey
import java.time.Duration

sealed class ProductKey(override val ttl: Duration) : CacheKey {
    abstract override val key: String

    data class GetProducts(private val query: ProductQuery) : ProductKey(Duration.ofMinutes(1)) {
        override val key: String = "get-products-v1:$query"
    }
    data class GetProduct(private val id: Long) : ProductKey(ttl = Duration.ofMinutes(1)) {
        override val key: String = "get-product-detail-v1:$id"
    }
}
