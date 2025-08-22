package com.loopers.application.order

import com.loopers.domain.coupon.Coupon
import com.loopers.domain.coupon.Coupon.Type.FIXED
import com.loopers.domain.coupon.Coupon.Type.RATE
import com.loopers.domain.order.DiscountPolicy
import com.loopers.domain.order.FixedDiscountPolicy
import com.loopers.domain.order.NoneDiscountPolicy
import com.loopers.domain.order.RateDiscountPolicy

class DiscountPolicyAdaptor {
    fun createPolicy(coupon: Coupon?): DiscountPolicy = when (coupon?.type) {
        null -> NoneDiscountPolicy()
        FIXED -> FixedDiscountPolicy(coupon.amount)
        RATE -> RateDiscountPolicy(coupon.amount)
    }
}
