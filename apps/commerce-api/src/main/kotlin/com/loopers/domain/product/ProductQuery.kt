package com.loopers.domain.product

import org.springframework.data.domain.Pageable

data class ProductQuery(
    val brandId: Long? = null,
    val sort: SortFor = SortFor.LATEST,
    val pageable: Pageable = Pageable.ofSize(20),
)
