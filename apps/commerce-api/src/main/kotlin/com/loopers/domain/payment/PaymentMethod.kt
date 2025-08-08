package com.loopers.domain.payment

import java.math.BigDecimal

interface PaymentMethod {
    val type: Type
    fun pay(amount: BigDecimal): PaymentInfo

    enum class Type {
        USER_POINT,
    }
}
