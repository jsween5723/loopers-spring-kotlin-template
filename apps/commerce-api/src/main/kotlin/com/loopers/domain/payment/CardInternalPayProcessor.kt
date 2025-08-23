package com.loopers.domain.payment

import com.loopers.domain.user.UserId
import java.math.BigDecimal
import java.util.UUID

class CardInternalPayProcessor(private val card: Card) : InternalPayProcessor<Payment.CardPayment> {
    override fun pay(orderId: UUID, userId: UserId, remainPrice: BigDecimal): Payment = Payment.CardPayment(
            card = card,
            info = PaymentInfo.pending(
                userId = userId,
                amount = remainPrice,
            ),
            orderId = orderId,
        )
}
