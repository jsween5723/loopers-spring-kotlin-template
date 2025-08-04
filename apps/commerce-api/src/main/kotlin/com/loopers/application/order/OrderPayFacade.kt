package com.loopers.application.order

import com.loopers.domain.coupon.IssuedCouponRepository
import com.loopers.domain.order.LineItem
import com.loopers.domain.order.OrderRepository
import com.loopers.domain.payment.PaymentInfo
import com.loopers.domain.payment.PaymentMethod
import com.loopers.domain.payment.PaymentRepository
import com.loopers.domain.product.ProductDeductService
import com.loopers.domain.product.ProductRepository
import com.loopers.domain.user.UserId
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Component
class OrderPayFacade(
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository,
    private val productRepository: ProductRepository,
    private val methodFactory: PaymentMethodFactory,
    private val issuedCouponRepository: IssuedCouponRepository,
) {
    private val productDeductService = ProductDeductService()
    private val orderPaymentService = OrderPaymentService()

    @Transactional
    fun pay(userId: UserId, criteria: Criteria): Result {
        // 관련 도메인 객체 확보
        val order = orderRepository.getById(criteria.orderId)
        // 결제 및 결제 기록 저장
        val issuedCoupon = issuedCouponRepository.findById(order.issuedCouponId)
        val methods = methodFactory.generate(userId, criteria.targets)
        val payment = orderPaymentService.pay(order, methods.first(), issuedCoupon)
            .let(paymentRepository::save)
        // 재고 차감
        val products = order.lineItems.map(LineItem::productId)
            .let(productRepository::getByIdsForUpdate)
        productDeductService.deduct(products, order.qtys)
        return Result(
            orderId = order.id,
            payment = payment.info,
        )
    }

    data class Criteria(val orderId: Long, val targets: List<PaymentMethodTypeAndAmount>) {
        data class PaymentMethodTypeAndAmount(val type: PaymentMethod.Type, val amount: BigDecimal)
    }

    data class Result(val orderId: Long, val payment: PaymentInfo)
}
