package com.loopers.application.order

import com.loopers.domain.order.LineItem
import com.loopers.domain.product.Product
import com.loopers.domain.shared.IdAndQuantity
import jakarta.persistence.EntityNotFoundException

class LineItemService {
    fun toLineItem(products: List<Product>, productIdWithQty: List<IdAndQuantity>): List<LineItem> {
        val productMap = products.associateBy(Product::id)
        return productIdWithQty.map {
            val product = productMap[it.productId] ?: throw EntityNotFoundException("${it.productId}는 존재하지 않는 상품입니다.")
            LineItem(
                productId = product.id,
                quantity = it.quantity,
                productName = product.name,
                brandId = product.brandId,
                price = product.price,
            )
        }
    }
}
