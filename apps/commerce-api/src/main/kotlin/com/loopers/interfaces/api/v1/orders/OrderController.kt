package com.loopers.interfaces.api.v1.orders

import com.loopers.application.order.provided.OrderProcessor
import com.loopers.domain.auth.Authentication
import com.loopers.domain.auth.Role
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping(value = ["/api/v1/orders"])
class OrderController(private val orderProcessor: OrderProcessor) {
    @PostMapping
    fun place(@RequestBody body: OrderRequest.Place, authentication: Authentication): OrderResponse.Place = authentication.hasRole(Role.USER) {
        val command = OrderProcessor.PlaceCommand(userId = authentication.userId, qtys = body.qtys)
        val result = orderProcessor.place(command)
        return@hasRole OrderResponse.Place(orderId = result.orderId, totalAmount = result.totalPrice)
    }

    @PostMapping("/{id}/payment")
    fun pay(@PathVariable id: UUID, @RequestBody body: OrderRequest.Pay, authentication: Authentication): OrderResponse.Pay = authentication.hasRole(Role.USER) {
        val command = OrderProcessor.PayCommand(
            userId = authentication.userId,
            orderId = id,
            issuedCouponId = body.issuedCouponId,
            pointAmount = body.pointAmount,
            card = body.card.toCard()
        )
        val result = orderProcessor.pay(command)
        return@hasRole OrderResponse.Pay(orderId = result.orderId, payedAmount = result.payedAmount, paymentId = result.paymentId)
    }
}
