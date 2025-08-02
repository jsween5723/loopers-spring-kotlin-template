package com.loopers.domain.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductSignalRepository : JpaRepository<ProductSignal, Long> {
    @Query("select ps from ProductSignal ps where ps.product.id = :productId")
    fun findByProductId(productId: Long): ProductSignal?
}
