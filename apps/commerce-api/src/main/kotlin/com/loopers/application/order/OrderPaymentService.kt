package com.loopers.application.order

import com.loopers.domain.order.Order
import com.loopers.domain.payment.PreviousPayments
import com.loopers.domain.payment.Paid
import com.loopers.domain.payment.PaymentMethod

class OrderPaymentService {
    fun pay(order: Order, previousPayments: PreviousPayments, paymentMethods: List<PaymentMethod>): List<Paid> {
        val remainingPrice = order.totalPrice - previousPayments.totalPrice
        check(remainingPrice >= paymentMethods.sumOf { it.amount }) { "남은 금액보다 결제 금액이 더 많습니다." }
        return paymentMethods.map { it.pay() }
    }
}
