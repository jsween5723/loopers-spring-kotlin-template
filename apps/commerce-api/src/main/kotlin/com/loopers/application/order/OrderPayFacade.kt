package com.loopers.application.order

import com.loopers.domain.order.OrderRepository
import com.loopers.domain.payment.OrderPayment
import com.loopers.domain.payment.OrderPaymentRepository
import com.loopers.domain.payment.PreviousPayments
import com.loopers.domain.payment.Payment.Type
import com.loopers.domain.payment.PaymentMethod
import com.loopers.domain.payment.PaymentRepository
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
    private val paymentRepository: PaymentRepository,
    private val methodFactory: PaymentMethodFactory,
) {
    private val productDeductService = ProductDeductService()
    private val orderPaymentService = OrderPaymentService()
    private val paymentInfoFactory = PaymentInfoFactory()

    @Transactional
    fun pay(userId: UserId, criteria: Criteria): Result {
        // 관련 도메인 객체 확보
        val order = orderRepository.findByIdOrNull(criteria.orderId)
            ?: throw EntityNotFoundException("존재하지 않는 주문 정보입니다.")
        val methods = methodFactory.generate(userId, criteria.targets)
        val previousPayments = PreviousPayments(orderPaymentRepository.findByOrderId(order.id))

        // 결제 및 결제 기록 저장
        val payments = orderPaymentService.pay(order, previousPayments, methods)
            .let { paymentRepository.saveAll(it) }
            .also {
                it.map { payment -> OrderPayment(orderId = order.id, payment = payment) }
                .also { orderPayments -> orderPaymentRepository.saveAll(orderPayments) }
            }

        // 재고 차감
        productDeductService.deduct(order.productsAndQuantities)

        return Result(
            orderId = order.id,
            payments = payments.map { paymentInfoFactory.from(it) },
        )
    }

    data class Criteria(val orderId: Long, val targets: List<PaymentMethodTypeAndAmount>) {
        data class PaymentMethodTypeAndAmount(val type: PaymentMethod.Type, val amount: BigDecimal)
    }

    data class Result(val orderId: Long, val payments: List<PaymentInfo>) {
        data class PaymentInfo(val id: Long, val amount: BigDecimal, val instrumentType: PaymentMethod.Type, val type: Type)
    }
}
