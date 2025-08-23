package com.loopers.application.order

import com.loopers.domain.payment.PaymentInfo
import java.util.UUID

object OrderResult {
    data class Create(val orderId: UUID)
    data class PayRequest(val payments: List<PaymentInfo>)
}
