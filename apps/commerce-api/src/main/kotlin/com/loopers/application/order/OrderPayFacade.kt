package com.loopers.application.order

import com.loopers.domain.order.OrderRepository
import com.loopers.domain.orderpayment.OrderPayService
import com.loopers.domain.orderpayment.OrderPayment
import com.loopers.domain.orderpayment.OrderPaymentRepository
import com.loopers.domain.orderpayment.OrderPayments
import com.loopers.domain.payment.Payment.Type
import com.loopers.domain.payment.PaymentInstrument
import com.loopers.domain.product.ProductDeductService
import com.loopers.domain.user.UserId
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Component
class OrderPayFacade(
    private val orderRepository: OrderRepository,
    private val orderPaymentRepository: OrderPaymentRepository,
    private val payTargetFactory: PayTargetFactory,
) {
    private val orderPayService = OrderPayService()
    private val productDeductService = ProductDeductService()

    @Transactional
    fun pay(userId: UserId, criteria: Criteria): Result {
        val order = orderRepository.findByIdOrNull(criteria.orderId)
            ?: throw EntityNotFoundException("존재하지 않는 주문 정보입니다.")
        val payTargets = payTargetFactory.generate(userId, criteria.targets)
        val previousPayments = OrderPayments(orderPaymentRepository.findByOrder(order))
        val orderPayments = orderPayService.payOrder(
            OrderPayService.Command(
                userId = userId,
                order = order,
                targets = payTargets,
                previousPayments = previousPayments,
            ),
        )
        productDeductService.deduct(order.productsAndQuantities)
        val results = orderPaymentRepository.saveAll(orderPayments)
        return Result(
            orderId = order.id,
            payments = results.map { Result.PaymentInfo.from(it) },
        )
    }

    data class Criteria(val orderId: Long, val targets: List<PaymentInstrumentTypeAndAmount>) {
        data class PaymentInstrumentTypeAndAmount(val type: PaymentInstrument.Type, val amount: BigDecimal)
    }

    data class Result(val orderId: Long, val payments: List<PaymentInfo>) {
        data class PaymentInfo(val id: Long, val amount: BigDecimal, val instrumentType: PaymentInstrument.Type, val type: Type) {
            companion object {
                fun from(orderPayment: OrderPayment): PaymentInfo = with(orderPayment.payment) {
                    return PaymentInfo(
                        id = id,
                        amount = amount,
                        instrumentType = instrumentType,
                        type = type,
                    )
                }
            }
        }
    }
}
