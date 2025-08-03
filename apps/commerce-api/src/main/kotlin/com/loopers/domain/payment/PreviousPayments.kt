package com.loopers.domain.payment

class PreviousPayments(private val orderPayments: List<OrderPayment> = listOf()) {
    private val payments
        get() = orderPayments.map { it.payment }.toMutableList()
    private val paidPrice
        get() = payments.filter { it.type == Payment.Type.PAID }
            .sumOf { it.amount }
    val totalPrice get() = paidPrice
}
