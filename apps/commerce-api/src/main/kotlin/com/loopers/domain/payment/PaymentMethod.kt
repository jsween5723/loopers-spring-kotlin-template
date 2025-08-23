package com.loopers.domain.payment

import java.math.BigDecimal

sealed class PaymentMethod(val type: InternalPayProcessor.Type) {
    class CardPay(val card: Card) : PaymentMethod(type = InternalPayProcessor.Type.CARD)
    class PointPay(val amount: BigDecimal) : PaymentMethod(type = InternalPayProcessor.Type.USER_POINT)
}
