package com.loopers.domain.product

import org.springframework.data.jpa.repository.JpaRepository

interface ProductSignalRepository : JpaRepository<ProductSignal, Long> {
    fun findByProduct(product: Product): ProductSignal?
}
