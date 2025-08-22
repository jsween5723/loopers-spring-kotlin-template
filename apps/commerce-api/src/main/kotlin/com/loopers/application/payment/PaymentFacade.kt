package com.loopers.application.payment

import com.loopers.domain.order.OrderService
import com.loopers.domain.payment.PaymentService
import com.loopers.domain.product.ProductService
import com.loopers.interfaces.api.v1.payments.PaymentRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Component
class PaymentFacade(
    private val orderService: OrderService,
    private val paymentService: PaymentService,
    private val productService: ProductService,
) {
    @Transactional
    fun process(request: PaymentRequest.TransactionInfo) {
        val transaction = paymentService.findExternalTransaction(request.transactionKey)
        transaction.validate(request.toTransaction())
        paymentService.updateTransaction(transaction)
        val order = orderService.getByUuid(UUID.fromString(request.orderId))
        order.qtys.forEach(productService::deduct)
    }
}
