package com.loopers.domain.product

import java.util.UUID

interface ProductRepository {
    fun getByIds(ids: List<Long>): List<Product>
    fun findByIdForUpdate(productId: Long): Product?
    fun save(product: Product): Product
    fun findByEventId(eventId: UUID): List<Product>
}
