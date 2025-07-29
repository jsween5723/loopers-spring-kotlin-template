package com.loopers.domain.order

import com.loopers.domain.product.LineItem

class OrderCreateService {
    fun create(lineItems: List<LineItem>): Order {
        val order = Order()
        order.updateLineItems(lineItems)
        return order
    }
}
