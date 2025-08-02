package com.loopers.domain.orderpayment

import com.loopers.domain.order.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderPaymentRepository : JpaRepository<OrderPayment, Long> {
    fun findByOrder(order: Order): MutableList<OrderPayment>
}
