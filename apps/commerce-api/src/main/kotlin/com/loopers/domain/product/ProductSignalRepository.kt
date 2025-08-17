package com.loopers.domain.product

interface ProductSignalRepository {
    fun save(productSignal: ProductSignal): ProductSignal
    fun getByProductId(productId: Long): ProductSignal
    fun getForUpdateByProductId(productId: Long): ProductSignal
}
