package com.loopers.application.order

import com.loopers.application.shared.Tx
import com.loopers.domain.coupon.CouponService
import com.loopers.domain.order.OrderService
import com.loopers.domain.payment.PaymentService
import com.loopers.domain.product.ProductService
import com.loopers.domain.shared.IdAndQuantity
import com.loopers.domain.user.UserId
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OrderFacade(
    private val orderService: OrderService,
    private val couponService: CouponService,
    private val productService: ProductService,
    private val payProcessorFactory: PayProcessorFactory,
    private val paymentService: PaymentService,
) {
    private val lineItemService = LineItemService()

    @Transactional
    fun create(userId: UserId, productIdWithQty: List<IdAndQuantity>, issuedCouponId: Long = 0L): OrderResult.Create {
        val products = productService.getByIds(productIdWithQty.map { it.productId })
        val lineItems = lineItemService.toLineItem(products, productIdWithQty)
        val issuedCoupon = couponService.findIssuedById(issuedCouponId)
        val order = orderService.create(lineItems, issuedCoupon?.id ?: 0L)
        return OrderResult.Create(orderId = order.orderId)
    }

    fun payRequest(userId: UserId, criteria: OrderCriteria.Request): OrderResult.PayRequest {
        val payments = Tx.run(
            function = {
                val order = orderService.getById(criteria.orderId)
                val processors = payProcessorFactory.generate(userId, criteria.methods)
                val issuedCoupon = couponService.findIssuedById(order.issuedCouponId)
                // 결제 및 결제 기록 저장
                val discountPolicy = DiscountPolicyAdaptor().createPolicy(issuedCoupon?.coupon)
                val paymentPrice = order.calculatePaymentPrice(discountPolicy)
                paymentService.processInternal(
                    orderId = order.orderId,
                    userId = userId,
                    processors = processors,
                    targetPrice = paymentPrice,
                )
            },
            afterCommit = { payments ->
                payments.forEach { paymentService.request(it.id) }
            },
        )
        return OrderResult.PayRequest(payments = payments.map { it.info })
    }
}
