package com.loopers.domain.shared

import com.loopers.domain.product.ProductInfo

interface CacheTemplate<T> {
    fun get(key: CacheKey): T?
    fun save(value: T): T
    fun saveAll(values: List<T>): List<T>
    fun findAll(keys: Collection<CacheKey>): List<ProductInfo>
}
