package com.loopers.application.order

import com.loopers.application.order.OrderPayFacade.Result.PaymentInfo
import com.loopers.domain.payment.Payment

class PaymentInfoFactory {
    fun from(payment: Payment): PaymentInfo = with(payment) {
        return PaymentInfo(
            id = id,
            amount = amount,
            instrumentType = instrumentType,
            type = type,
        )
    }
}
