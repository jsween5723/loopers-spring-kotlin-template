package com.loopers.domain.order

import com.loopers.domain.shared.ProductAndQuantity

class OrderCreateService {
    fun create(targets: List<ProductAndQuantity>): Order {
        val order = Order()
        order.changeTo(targets)
        return order
    }
}
