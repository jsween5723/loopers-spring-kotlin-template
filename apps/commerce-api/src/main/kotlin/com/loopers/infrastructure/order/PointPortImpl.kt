package com.loopers.infrastructure.order

import com.loopers.application.order.required.PointPort
import com.loopers.domain.order.ReservedPoint
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.UUID

@Component
class PointPortImpl: PointPort {
    override fun reserve(
        userId: UUID,
        amount: BigDecimal,
        orderId: UUID,
    ): ReservedPoint {
        TODO("Not yet implemented")
    }

    override fun cancelReserve(orderId: UUID) {
        TODO("Not yet implemented")
    }
}
