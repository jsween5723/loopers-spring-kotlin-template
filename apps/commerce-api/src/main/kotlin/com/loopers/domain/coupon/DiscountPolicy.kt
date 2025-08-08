package com.loopers.domain.coupon

import com.loopers.domain.coupon.Coupon.Type.FIXED
import com.loopers.domain.coupon.Coupon.Type.RATE
import java.math.BigDecimal

sealed interface DiscountPolicy {
    fun discount(price: BigDecimal): BigDecimal
}

class RateDiscountPolicy(val amount: BigDecimal) : DiscountPolicy {
    override fun discount(price: BigDecimal): BigDecimal = price * (BigDecimal.ONE - amount.divide(BigDecimal(100)))
}

class FixedDiscountPolicy(val amount: BigDecimal) : DiscountPolicy {
    override fun discount(price: BigDecimal): BigDecimal {
        if (price - amount <= BigDecimal.ZERO) return BigDecimal.ZERO
        return price - amount
    }
}

class DiscountPolicyAdaptor {
    fun createPolicy(coupon: Coupon): DiscountPolicy = when (coupon.type) {
        FIXED -> FixedDiscountPolicy(coupon.amount)
        RATE -> RateDiscountPolicy(coupon.amount)
    }
}
