package com.loopers.domain.payment

import com.loopers.domain.user.UserId
import java.math.BigDecimal
import java.util.UUID

interface InternalPayProcessor<T : Payment> {
    fun pay(orderId: UUID, userId: UserId, remainPrice: BigDecimal): Payment
    enum class Type {
        USER_POINT,
        CARD,
    }
}
