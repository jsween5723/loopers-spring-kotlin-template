package com.loopers.domain.payment

import org.springframework.data.jpa.repository.JpaRepository

interface OrderPaymentRepository : JpaRepository<OrderPayment, Long> {
    fun findByOrderId(orderId: Long): MutableList<OrderPayment>
}
