package com.loopers.domain.order

class OrderCreateService {
    fun create(lineItems: List<LineItem>): Order {
        val order = Order()
        order.changeTo(lineItems)
        return order
    }
}
