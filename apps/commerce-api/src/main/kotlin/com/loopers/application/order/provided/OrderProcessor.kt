package com.loopers.application.order.provided

import com.loopers.domain.order.Card
import java.math.BigDecimal
import java.util.UUID

interface OrderProcessor {
    fun place(command: PlaceCommand): PlaceResult
    fun pay(command: PayCommand): PayResult
    fun cancel(command: CancelCommand)
    fun complete(command: CompleteCommand)
    data class PayCommand(val userId: UUID, val orderId: UUID, val issuedCouponId: UUID?, val pointAmount: BigDecimal, val card: Card)
    data class PayResult(val orderId: UUID, val payedAmount: BigDecimal, val paymentId: UUID)
    data class PlaceCommand(val userId: UUID, val qtys: List<Pair<UUID, Long>>)
    data class PlaceResult(val orderId: UUID, val totalPrice: BigDecimal)
    data class CancelCommand(val orderId: UUID)
    data class CompleteCommand(val orderId: UUID)
}
