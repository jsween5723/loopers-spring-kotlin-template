package com.loopers.domain.order

import java.math.BigDecimal
import java.util.UUID

data class ReservedPoint(val userId: UUID = UUID.randomUUID(), val amount: BigDecimal, val orderId: UUID) {
}
