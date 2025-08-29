package com.loopers.domain.order

import java.util.UUID

interface OrderRepository {
    fun save(order: Order): Order
    fun getById(id: Long): Order
    fun getByUuid(uuid: UUID): Order
}
