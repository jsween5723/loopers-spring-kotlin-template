package com.loopers.infrastructure.order

import com.loopers.application.order.required.ProductPort
import com.loopers.domain.order.OrderLine
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ProductPortImpl: ProductPort {
    override fun reserve(orderId: UUID, qtys: List<Pair<UUID, Long>>): List<OrderLine> {
        TODO("Not yet implemented")
    }
}
