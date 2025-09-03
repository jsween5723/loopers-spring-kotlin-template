package com.loopers.domain.order

import java.math.BigDecimal
import java.util.UUID

class Receipt(
    val orderId: UUID,
    val amount: BigDecimal,
    val card: Card,
)
