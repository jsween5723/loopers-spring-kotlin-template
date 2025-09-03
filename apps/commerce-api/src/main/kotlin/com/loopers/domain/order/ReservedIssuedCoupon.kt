package com.loopers.domain.order

import java.util.UUID

data class ReservedIssuedCoupon(val issuedCouponId: UUID = UUID.randomUUID(), val orderId: UUID, val discountPolicy: DiscountPolicy)
