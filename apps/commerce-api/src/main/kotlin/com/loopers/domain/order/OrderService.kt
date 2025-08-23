package com.loopers.domain.order

import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OrderService(private val orderRepository: OrderRepository) {
    fun getById(id: Long): Order = orderRepository.getById(id)
    fun getByUuid(uuid: UUID): Order = orderRepository.getByUuid(uuid)
    fun create(lineItems: List<LineItem>, issuedCouponId: Long = 0L): Order {
        val order = Order(issuedCouponId = issuedCouponId)
        order.changeTo(lineItems)
        return orderRepository.save(order)
    }
}
