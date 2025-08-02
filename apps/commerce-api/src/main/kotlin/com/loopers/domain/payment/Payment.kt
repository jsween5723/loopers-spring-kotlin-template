package com.loopers.domain.payment

import com.loopers.domain.BaseEntity
import com.loopers.domain.user.UserId
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import java.math.BigDecimal

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("type")
sealed class Payment(
    val userId: UserId,
    val amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    val instrumentType: PaymentInstrument.Type,
    @Enumerated(EnumType.STRING)
    val type: Type,
) : BaseEntity() {
    enum class Type {
        PAID,
        REFUND,
        CANCEL,
    }

    companion object {
        fun paid(userId: UserId, target: BigDecimal, instrument: PaymentInstrument): Paid =
            Paid(userId = userId, amount = target, instrumentType = instrument.type)
    }
}

@Entity
class Paid internal constructor(userId: UserId, amount: BigDecimal, instrumentType: PaymentInstrument.Type) :
    Payment(
        userId = userId,
        amount = amount,
        instrumentType = instrumentType,
        type = Type.PAID,
    )
