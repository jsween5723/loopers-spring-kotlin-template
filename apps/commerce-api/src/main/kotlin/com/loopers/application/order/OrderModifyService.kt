package com.loopers.application.order

import com.loopers.application.order.provided.OrderFinder
import com.loopers.application.order.provided.OrderProcessor
import com.loopers.application.order.required.CouponPort
import com.loopers.application.order.required.OrderRepository
import com.loopers.application.order.required.PaymentPort
import com.loopers.application.order.required.PointPort
import com.loopers.application.order.required.ProductPort
import com.loopers.domain.order.Order
import com.loopers.domain.order.Pricing
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderModifyService(
    private val productPort: ProductPort,
    private val couponPort: CouponPort,
    private val pointPort: PointPort,
    private val orderRepository: OrderRepository,
    private val orderFinder: OrderFinder,
    private val paymentPort: PaymentPort
): OrderProcessor {

    @Transactional
    override fun place(command: OrderProcessor.PlaceCommand): OrderProcessor.PlaceResult = with(command) {
        val order = orderRepository.save(Order(userId = userId))
        val orderLines = productPort.reserve(order.id, qtys)
        order.addOrderLines(orderLines)
        return OrderProcessor.PlaceResult(order.id, order.totalPrice)
    }

    @Transactional
    override fun pay(command: OrderProcessor.PayCommand): OrderProcessor.PayResult = with(command){
        val order = orderFinder.find(orderId)
        val reservedCoupon = couponPort.reserve(orderId, userId, issuedCouponId)
        val reservedPoint = pointPort.reserve(amount = pointAmount, orderId = orderId, userId = userId)
        val price = Pricing(order)
            .applyPoint(reservedPoint)
            .applyCoupon(reservedCoupon)
            .complete()
        val receipt = order.readyForPayment(price, card)
        val paymentId = paymentPort.create(receipt)
        return OrderProcessor.PayResult(orderId = orderId, payedAmount = receipt.amount, paymentId = paymentId)
    }

    @Transactional
    override fun cancel(command: OrderProcessor.CancelCommand) {
        val order = orderFinder.find(command.orderId)
        order.cancel()
    }

    @Transactional
    override fun complete(command: OrderProcessor.CompleteCommand) {
        val order = orderFinder.find(command.orderId)
        order.complete()
    }
}
