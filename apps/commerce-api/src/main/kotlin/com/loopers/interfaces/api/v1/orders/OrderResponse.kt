package com.loopers.interfaces.api.v1.orders

import java.math.BigDecimal
import java.util.UUID

object OrderResponse {
    data class Place(val orderId: UUID, val totalAmount: BigDecimal)
    data class Pay(val orderId: UUID, val payedAmount: BigDecimal, val paymentId: UUID)
}
