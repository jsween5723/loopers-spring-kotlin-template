package com.loopers.domain.product

import com.loopers.domain.shared.ProductAndQuantity

class ProductDeductService {
    fun deduct(items: List<ProductAndQuantity>): List<LineItem> = items.map { it.product.deduct(it.quantity) }
}
