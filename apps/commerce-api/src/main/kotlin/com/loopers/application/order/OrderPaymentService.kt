package com.loopers.application.order

import com.loopers.domain.order.Order
import com.loopers.domain.payment.OrderPayment
import com.loopers.domain.payment.PaymentMethod

class OrderPaymentService {
    fun pay(
        order: Order,
        paymentMethods: List<PaymentMethod>,
    ): List<OrderPayment> = paymentMethods.map(PaymentMethod::pay).map { paymentInfo ->
        OrderPayment(orderId = order.id, paymentInfo = paymentInfo)
    }
}
