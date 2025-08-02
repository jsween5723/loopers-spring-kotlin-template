package com.loopers.domain.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductSignalRepository : JpaRepository<ProductSignal, Long> {
    fun findByProduct(product: Product): ProductSignal?

    @Query("select ps from ProductSignal ps join fetch ps.product where ps.product.id = :productId")
    fun findByProductId(productId: Long): ProductSignal?
}
