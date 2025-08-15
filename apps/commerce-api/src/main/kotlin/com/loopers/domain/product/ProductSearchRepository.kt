package com.loopers.domain.product

import java.util.stream.Stream

interface ProductSearchRepository {
    fun search(query: ProductQuery): List<ProductSignal>
    fun searchForIds(query: ProductQuery): List<Long>
    fun findAllWithStream(): Stream<ProductSignal>
}
