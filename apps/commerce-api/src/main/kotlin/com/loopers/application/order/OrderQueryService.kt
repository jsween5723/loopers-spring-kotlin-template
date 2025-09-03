package com.loopers.application.order

import com.loopers.application.order.provided.OrderFinder
import com.loopers.application.order.required.OrderRepository
import com.loopers.domain.order.Order
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OrderQueryService(private val orderRepository: OrderRepository): OrderFinder {
    override fun find(id: UUID): Order = orderRepository.findById(id) ?: throw EntityNotFoundException()
}
