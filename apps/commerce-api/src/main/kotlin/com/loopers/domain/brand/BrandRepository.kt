package com.loopers.domain.brand

import org.springframework.data.jpa.repository.JpaRepository

interface BrandRepository : JpaRepository<Brand, Long> {
    fun findByIdIn(brandIds: List<Long>): List<Brand>
}
