package com.loopers.domain.product

import com.loopers.domain.shared.ProductAndQuantity

class ProductSelectService {
    fun select(items: List<ProductAndQuantity>): List<LineItem> = items.map { it.product.select(it.quantity) }
}
