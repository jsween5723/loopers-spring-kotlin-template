package com.loopers.domain.product

import java.math.BigDecimal

data class LineItem(
    val productId: Long,
    val quantity: Long,
    val productName: String,
    val brandName: String,
    val brandId: Long,
    val price: BigDecimal,
) {
    companion object {
        fun from(product: Product, quantity: Long): LineItem = LineItem(
            productId = product.id,
                quantity = quantity,
                productName = product.name,
                brandName = product.brand.name,
                brandId = product.brand.id,
                price = product.price,
        )
    }
}
