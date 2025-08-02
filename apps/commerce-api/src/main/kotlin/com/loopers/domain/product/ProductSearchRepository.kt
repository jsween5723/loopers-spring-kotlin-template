package com.loopers.domain.product

interface ProductSearchRepository {
    fun search(query: ProductQuery): List<ProductSignal>
}
