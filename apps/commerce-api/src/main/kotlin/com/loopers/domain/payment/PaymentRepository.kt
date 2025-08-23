package com.loopers.domain.payment

interface PaymentRepository {
    fun findById(id: Long): Payment?
    fun findExternalTransaction(transactionId: String): Transaction?
    fun saveTransaction(transaction: Transaction): PaymentTransaction
    fun request(payment: Payment)
    fun save(payment: Payment): Payment
}
