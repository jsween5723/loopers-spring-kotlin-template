package com.loopers.domain.order

import com.loopers.domain.shared.ProductAndQuantity

class OrderCreateService {
    fun create(targets: List<ProductAndQuantity>): Order {
        val order = Order()
        val lineItems = targets.map { it.product.select(it.quantity) }
        order.changeTo(lineItems)
        return order
    }
}
