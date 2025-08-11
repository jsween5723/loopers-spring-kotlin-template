package com.loopers.application.order

import com.loopers.domain.coupon.IssuedCoupon
import com.loopers.domain.order.Order
import com.loopers.domain.payment.Payment
import com.loopers.domain.payment.PaymentMethod

class OrderPaymentService {
    fun pay(
        order: Order,
        paymentMethod: PaymentMethod,
        issuedCoupon: IssuedCoupon? = null,
    ): Payment {
        val discountedPrice = issuedCoupon?.discount(order.totalPrice) ?: order.totalPrice
        val info = paymentMethod.pay(discountedPrice)
        return Payment(info, orderId = order.id)
    }
}
