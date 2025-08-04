package com.loopers.domain.payment

import com.loopers.domain.BaseEntity
import com.loopers.domain.payment.Payment.Type
import com.loopers.domain.user.UserId
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import java.math.BigDecimal

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("type")
abstract class Payment(val paymentInfo: PaymentInfo) : BaseEntity() {
    enum class Type {
        PAID,
        REFUND,
        CANCEL,
    }
}

@Entity
class OrderPayment(val orderId: Long, paymentInfo: PaymentInfo) :
    Payment(
        paymentInfo = paymentInfo,
    )

@Embeddable
data class PaymentInfo private constructor(
    val userId: UserId,
    val amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    val methodType: PaymentMethod.Type,
    @Enumerated(EnumType.STRING)
    val type: Type,
) {
    companion object {
        fun paid(userId: UserId, amount: BigDecimal, method: PaymentMethod): PaymentInfo =
            PaymentInfo(
                userId = userId,
                amount = amount,
                methodType = method.type,
                type = Type.PAID,
            )
    }
}
