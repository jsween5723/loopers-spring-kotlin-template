package com.loopers.domain.payment

import com.loopers.domain.user.UserId
import java.math.BigDecimal

interface PaymentInstrument {
    val type: Type
    fun pay(amount: BigDecimal, userId: UserId): Paid

    enum class Type {
        USER_POINT,
    }
}
