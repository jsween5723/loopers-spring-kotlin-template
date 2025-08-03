package com.loopers.application.order

import com.loopers.domain.order.Order
import com.loopers.domain.payment.Payment
import com.loopers.domain.payment.PaymentMethod

class OrderPaymentService {
    fun pay(
        order: Order,
        paymentMethods: List<PaymentMethod>,
    ): List<Payment> = paymentMethods.map(PaymentMethod::pay).map { Payment(it, orderId = order.id) }
}
