package com.loopers.domain.product

import jakarta.persistence.Embeddable
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.math.BigDecimal

@Embeddable
data class LineItem(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: Product,
    val quantity: Long,
    val productName: String,
    val brandName: String,
    val brandId: Long,
    val price: BigDecimal,
) {
    companion object {
        fun from(product: Product, quantity: Long): LineItem = LineItem(
            product = product,
                quantity = quantity,
                productName = product.name,
                brandName = product.brand.name,
                brandId = product.brand.id,
                price = product.price,
        )
    }
}
