package com.loopers.infrastructure.payment

import com.loopers.domain.payment.Card
import com.loopers.domain.payment.Payment
import com.loopers.domain.payment.Transaction

object PaymentDto {
    data class PaymentRequest(
        val orderId: String,
        val cardType: CardTypeDto,
        val cardNo: String,
        val amount: Long,
        val callbackUrl: String,
    )

    data class TransactionDetailResponse(
        val transactionKey: String,
        val orderId: String,
        val cardType: CardTypeDto,
        val cardNo: String,
        val amount: Long,
        val status: TransactionStatusResponse,
        val reason: String?,
    ) {
        fun toTransaction(): Transaction = Transaction(
            transactionKey = transactionKey,
            status = status.toStatus(),
            reason = reason,
        )
    }

    data class TransactionResponse(val transactionKey: String, val status: TransactionStatusResponse, val reason: String?) {
        fun toTransaction(): Transaction = Transaction(
            transactionKey = transactionKey,
            status = status.toStatus(),
            reason = reason,
        )
    }

    data class OrderResponse(val orderId: String, val transactions: List<TransactionResponse>)

    enum class CardTypeDto {
        SAMSUNG,
        KB,
        HYUNDAI,
        ;

        fun toCardType(): Card.Type = when (this) {
            SAMSUNG -> Card.Type.SAMSUNG
            KB -> Card.Type.KB
            HYUNDAI -> Card.Type.HYUNDAI
        }
        companion object {
            fun fromCardType(cardType: Card.Type) = when (cardType) {
                Card.Type.SAMSUNG -> SAMSUNG
                Card.Type.KB -> KB
                Card.Type.HYUNDAI -> HYUNDAI
            }
        }
    }

    enum class TransactionStatusResponse {
        PENDING,
        SUCCESS,
        FAILED,
        ;

        fun toStatus(): Payment.TransactionStatus = when (this) {
            PENDING -> Payment.TransactionStatus.PENDING
            SUCCESS -> Payment.TransactionStatus.SUCCESS
            FAILED -> Payment.TransactionStatus.FAILED
        }
    }
}
