package com.loopers.domain.orderpayment

import com.loopers.domain.payment.Payment

class OrderPayments(private val orderPayments: MutableList<OrderPayment> = mutableListOf()) {
    private val payments
        get() = orderPayments.map { it.payment }
    private val paidPrice
        get() = payments.filter { it.type == Payment.Type.PAID }
            .sumOf { it.amount }
    val totalPrice get() = paidPrice
}
