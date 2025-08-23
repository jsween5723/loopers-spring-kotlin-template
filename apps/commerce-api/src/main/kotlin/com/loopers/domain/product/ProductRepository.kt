package com.loopers.domain.product

interface ProductRepository {
    fun getByIds(ids: List<Long>): List<Product>
    fun findByIdForUpdate(productId: Long): Product?
    fun save(product: Product): Product
}
