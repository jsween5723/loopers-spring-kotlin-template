package com.loopers.domain.order

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

class NoneDiscountPolicy : DiscountPolicy {
    override fun discount(price: BigDecimal): BigDecimal = BigDecimal.ZERO
}
