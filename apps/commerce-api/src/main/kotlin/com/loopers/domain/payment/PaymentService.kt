package com.loopers.domain.payment

import com.loopers.domain.user.UserId
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.UUID

@Service
class PaymentService(private val paymentRepository: PaymentRepository) {
    fun findExternalTransaction(transactionId: String): Transaction =
        paymentRepository.findExternalTransaction(transactionId)
            ?: throw EntityNotFoundException("Payment with id $transactionId not found")
    fun updateTransaction(transaction: Transaction): PaymentTransaction = paymentRepository.saveTransaction(transaction)
    fun processInternal(
        orderId: UUID,
        userId: UserId,
        processors: Map<InternalPayProcessor.Type, InternalPayProcessor<*>>,
        targetPrice: BigDecimal,
    ): List<Payment> {
        val pointProcessor = processors[InternalPayProcessor.Type.USER_POINT]!!
        val cardProcessor = processors[InternalPayProcessor.Type.CARD]!!
        val pointPayment =
            paymentRepository.save(
                pointProcessor.pay(orderId = orderId, userId = userId, remainPrice = targetPrice),
            )
        val remainPrice = targetPrice - pointPayment.info.amount
        val cardPayment = paymentRepository.save(
            cardProcessor
                .pay(orderId = orderId, userId = userId, remainPrice = remainPrice),
        )
        return listOf(pointPayment, cardPayment)
    }

    fun request(paymentId: Long) {
        val payment = paymentRepository.findById(paymentId)
        checkNotNull(payment) { "Payment with id $paymentId does not exist." }
        when (payment) {
            is Payment.PointPayment -> return
            is Payment.CardPayment -> paymentRepository.request(payment)
        }
    }
}
