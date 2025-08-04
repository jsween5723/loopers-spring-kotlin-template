package com.loopers.application.order

import com.loopers.domain.order.Order
import com.loopers.domain.payment.PaymentMethod
import com.loopers.domain.payment.PreviousPayments

class PaymentPriceStrategy {
    fun check(order: Order, previousPayments: PreviousPayments, methods: List<PaymentMethod>) {
        val remainingPrice = order.totalPrice - previousPayments.totalPrice
        check(remainingPrice >= methods.sumOf(PaymentMethod::amount)) { "남은 금액보다 결제 금액이 더 많습니다." }
    }
}
