package com.loopers.domain.product

interface ProductRepository {
    fun getByIdsForUpdate(ids: List<Long>): List<Product>
    fun getByIds(ids: List<Long>): List<Product>
    fun save(product: Product): Product
}
