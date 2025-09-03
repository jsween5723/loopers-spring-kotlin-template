package com.loopers.application.order.required

import com.loopers.domain.order.ReservedPoint
import java.math.BigDecimal
import java.util.UUID

interface PointPort {
    fun reserve(userId: UUID, amount: BigDecimal, orderId: UUID): ReservedPoint
    fun cancelReserve(orderId: UUID)
}
