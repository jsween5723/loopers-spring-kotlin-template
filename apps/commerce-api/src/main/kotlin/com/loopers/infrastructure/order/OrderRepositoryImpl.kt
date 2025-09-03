package com.loopers.infrastructure.order

import com.loopers.application.order.required.OrderRepository
import com.loopers.domain.order.Order
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class OrderRepositoryImpl: OrderRepository {
    override fun save(order: Order): Order {
        TODO("Not yet implemented")
    }

    override fun findById(id: UUID): Order? {
        TODO("Not yet implemented")
    }
}
