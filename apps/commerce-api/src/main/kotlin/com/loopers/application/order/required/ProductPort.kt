package com.loopers.application.order.required

import com.loopers.domain.order.OrderLine
import java.util.UUID

interface ProductPort {
    fun reserve(orderId: UUID, qtys: List<Pair<UUID, Long>>): List<OrderLine>
}
