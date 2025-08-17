package com.loopers.domain.payment

import com.loopers.domain.BaseEntity
import com.loopers.domain.user.UserId
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.math.BigDecimal

@Entity
class Payment(val info: PaymentInfo, val orderId: Long) : BaseEntity() {
    enum class Type {
        PAID,
        REFUND,
        CANCEL,
    }
}

@Embeddable
data class PaymentInfo private constructor(
    val userId: UserId,
    val amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    val methodType: PaymentMethod.Type,
    @Enumerated(EnumType.STRING)
    val type: Payment.Type,
) {
    companion object {
        fun paid(userId: UserId, amount: BigDecimal, method: PaymentMethod): PaymentInfo =
            PaymentInfo(
                userId = userId,
                amount = amount,
                methodType = method.type,
                type = Payment.Type.PAID,
            )
    }
}
