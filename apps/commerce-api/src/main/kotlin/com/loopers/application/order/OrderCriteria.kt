package com.loopers.application.order

import com.loopers.domain.payment.PaymentMethod

object OrderCriteria {
    data class Request(val orderId: Long, val methods: List<PaymentMethod>)
}
