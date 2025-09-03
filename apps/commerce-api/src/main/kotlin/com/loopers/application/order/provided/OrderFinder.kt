package com.loopers.application.order.provided

import com.loopers.domain.order.Order
import java.util.UUID

interface OrderFinder {
    fun find(id: UUID): Order
}
