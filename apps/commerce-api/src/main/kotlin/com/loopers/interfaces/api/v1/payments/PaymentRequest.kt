package com.loopers.interfaces.api.v1.payments

import com.loopers.domain.payment.Card
import com.loopers.domain.payment.Payment
import com.loopers.domain.payment.Transaction

object PaymentRequest {
    /**
     * 트랜잭션 정보
     *
     * @property transactionKey 트랜잭션 KEY
     * @property orderId 주문 ID
     * @property cardType 카드 종류
     * @property cardNo 카드 번호
     * @property amount 금액
     * @property status 처리 상태
     * @property reason 처리 사유
     */
    data class TransactionInfo(
        val transactionKey: String,
        val orderId: String,
        val cardType: CardTypeRequest,
        val cardNo: String,
        val amount: Long,
        val status: TransactionStatusRequest,
        val reason: String?,
    ) {
        fun toTransaction() = Transaction(transactionKey = transactionKey, status = status.toStatus(), reason = reason)
    }
}

enum class TransactionStatusRequest {
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

enum class CardTypeRequest {
    SAMSUNG,
    KB,
    HYUNDAI,
    ;

    fun toCardType(): Card.Type = when (this) {
        SAMSUNG -> Card.Type.SAMSUNG
        KB -> Card.Type.KB
        HYUNDAI -> Card.Type.HYUNDAI
    }
}
