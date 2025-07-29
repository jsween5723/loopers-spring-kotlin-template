package com.loopers.domain.payment

import java.math.BigDecimal

interface PaymentInstrument {
    val type: Type
    fun pay(target: BigDecimal): Paid

    enum class Type {
        USER_POINT,
    }
}
