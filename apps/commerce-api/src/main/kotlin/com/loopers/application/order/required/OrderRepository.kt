package com.loopers.application.order.required

import com.loopers.domain.order.Order
import java.util.UUID

interface OrderRepository {
    fun save(order: Order): Order
    fun findById(id: UUID): Order?
}
