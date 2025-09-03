package com.loopers.event

import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.UUID

object OrderEvent {
    data class PaymentCreated(val orderId: UUID, val amount: BigDecimal, val paymentId: UUID, val payedAt: ZonedDateTime)
    data class PaymentRequested(val orderId: UUID, val amount: BigDecimal, val paymentId: UUID, val reqeustedAt: ZonedDateTime)
}
