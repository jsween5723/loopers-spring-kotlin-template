package com.loopers.infrastructure.payment

import com.loopers.domain.payment.Payment
import com.loopers.domain.payment.PaymentRepository
import com.loopers.domain.payment.PaymentTransaction
import com.loopers.domain.payment.Transaction
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class PaymentRepositoryImpl(
    private val jpaRepository: PaymentJpaRepository,
    private val paymentGateway: PaymentGateway,
    private val transactionJpaRepository: PaymentTransactionRepository,
    @Value("\${payment-gateway.call-back-url}") private val callBackUrl: String,
) : PaymentRepository {
    override fun findById(id: Long): Payment? = jpaRepository.findByIdOrNull(id)

    override fun findExternalTransaction(transactionId: String): Transaction {
        val response = paymentGateway.getTransaction(transactionId)
        return response.toTransaction()
    }

    override fun saveTransaction(transaction: Transaction): PaymentTransaction {
        val found = transactionJpaRepository.findByInfo_TransactionKey(transaction.transactionKey)
            ?: throw EntityNotFoundException("Transaction with key ${transaction.transactionKey} not found")
        found.updateInfo(transaction)
        return transactionJpaRepository.save(found)
    }

    override fun request(payment: Payment) {
        if (payment is Payment.CardPayment) {
            val request = PaymentDto.PaymentRequest(
                orderId = payment.orderId.toString(),
                cardType = PaymentDto.CardTypeDto.fromCardType(payment.card.type),
                cardNo = payment.card.cardNumber,
                amount = payment.info.amount.toLong(),
                callbackUrl = callBackUrl,
            )
            val result = paymentGateway.request(request)
            val transaction = PaymentTransaction(payment = payment, info = result.toTransaction())
            transactionJpaRepository.save(transaction)
        }
    }

    override fun save(payment: Payment): Payment = jpaRepository.save(payment)
}

@Repository
interface PaymentJpaRepository : JpaRepository<Payment, Long>

@Repository
interface PaymentTransactionRepository : JpaRepository<PaymentTransaction, Long> {
    fun findByInfo_TransactionKey(transactionKey: String): PaymentTransaction?
}
