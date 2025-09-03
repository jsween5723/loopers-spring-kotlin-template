package com.loopers.application.order.required

import com.loopers.domain.order.ReservedIssuedCoupon
import java.util.UUID

interface CouponPort {
    fun reserve(orderId: UUID, userId: UUID, issuedCouponId: UUID?): ReservedIssuedCoupon
}
