package com.loopers.domain.shared

import com.loopers.domain.product.Product
import jakarta.persistence.EntityNotFoundException

data class ProductAndQuantity(val product: Product, val quantity: Long) {
    companion object {
        fun of(products: List<Product>, selects: List<ProductIdAndQuantity>): List<ProductAndQuantity> {
            val productMap = products.associateBy { it.id }
            return selects.map {
                ProductAndQuantity(
                    product = productMap[it.productId] ?: throw EntityNotFoundException("${it.productId} 상품이 존재하지 않습니다."),
                    quantity = it.quantity,
                )
            }
        }
    }
}
