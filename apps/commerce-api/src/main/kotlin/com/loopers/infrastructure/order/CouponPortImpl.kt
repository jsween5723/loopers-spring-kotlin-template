package com.loopers.infrastructure.order

import com.loopers.application.order.required.CouponPort
import com.loopers.domain.order.ReservedIssuedCoupon
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CouponPortImpl: CouponPort {
    override fun reserve(
        orderId: UUID,
        userId: UUID,
        issuedCouponId: UUID?,
    ): ReservedIssuedCoupon {
        TODO("Not yet implemented")
    }
}
