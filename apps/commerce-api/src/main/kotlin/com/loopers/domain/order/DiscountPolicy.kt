package com.loopers.domain.order

import java.math.BigDecimal

sealed interface DiscountPolicy {
    fun discount(amount: BigDecimal): BigDecimal

    class Rate(val rate: BigDecimal) : DiscountPolicy {
        init {
            require(rate >= BigDecimal.ZERO && rate <= 100.toBigDecimal()) {"비율은 0~100 사잇값이어야합니다."}
        }
        override fun discount(amount: BigDecimal): BigDecimal {
            return amount.multiply((100.toBigDecimal() - rate)) / 100.toBigDecimal()
        }
    }

    class None : DiscountPolicy {
        override fun discount(amount: BigDecimal): BigDecimal = amount
    }

    class Fixed(val fixedValue: BigDecimal) : DiscountPolicy {
        init {
            require(fixedValue >= BigDecimal.ZERO) {"정액할인은 0이상이어야합니다."}
        }
        override fun discount(amount: BigDecimal): BigDecimal {
            if (amount - fixedValue <= BigDecimal.ZERO) return BigDecimal.ZERO
            return amount - fixedValue
        }
    }

    enum class Type {
        FIXED, RATE
    }
}
