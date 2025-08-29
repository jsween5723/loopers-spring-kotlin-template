package com.loopers.domain.payment

import com.loopers.domain.BaseEntity
import com.loopers.domain.payment.Payment.TransactionStatus
import com.loopers.domain.user.UserId
import jakarta.persistence.DiscriminatorColumn
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import java.math.BigDecimal
import java.util.UUID

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "payment_method")
sealed class Payment(val info: PaymentInfo, val orderId: UUID) : BaseEntity() {
    @Entity(name = "point_payment")
    class PointPayment(info: PaymentInfo, orderId: UUID) : Payment(info, orderId)

    @Entity(name = "card_payment")
    class CardPayment(val card: Card, info: PaymentInfo, orderId: UUID) : Payment(info, orderId)

    enum class TransactionStatus {
        PENDING,
        SUCCESS,
        FAILED,
    }
}

@Entity
class PaymentTransaction(
    @JoinColumn(name = "payment_id") @OneToOne(fetch = FetchType.LAZY) val payment: Payment,
    var info: Transaction,
) : BaseEntity() {
    fun updateInfo(info: Transaction) {
        this.info = info
    }
}

@Embeddable
data class PaymentInfo private constructor(
    val userId: UserId,
    val amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    val type: TransactionStatus,
) {
    companion object {
        fun paid(userId: UserId, amount: BigDecimal): PaymentInfo =
            PaymentInfo(
                userId = userId,
                amount = amount,
                type = TransactionStatus.SUCCESS,
            )

        fun pending(
            userId: UserId,
            amount: BigDecimal,
        ): PaymentInfo = PaymentInfo(userId = userId, amount = amount, type = TransactionStatus.PENDING)

        fun fail(
            userId: UserId,
            amount: BigDecimal,
        ): PaymentInfo = PaymentInfo(userId = userId, amount = amount, type = TransactionStatus.FAILED)
    }
}

@Embeddable
data class Transaction(val transactionKey: String, val status: TransactionStatus, val reason: String?) {
    fun validate(info: Transaction) {
        check(info == this) { "Transaction $transactionKey does not match expected $this" }
    }
}
