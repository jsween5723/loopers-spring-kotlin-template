package com.loopers.domain.payment

import java.math.BigDecimal

interface PaymentMethod {
    val type: Type
    val amount: BigDecimal
    fun pay(): PaymentInfo

    enum class Type {
        USER_POINT,
    }
}
