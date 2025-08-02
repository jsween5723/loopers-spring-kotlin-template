package com.loopers.domain.orderpayment

import com.loopers.domain.order.Order
import com.loopers.domain.shared.PayTarget
import com.loopers.domain.user.UserId

class OrderPayService {
    fun payOrder(command: Command): List<OrderPayment> = with(command) {
        val remainingPrice = order.totalPrice - previousPayments.totalPrice
        check(remainingPrice >= targets.sumOf { it.amount }) { "남은 금액보다 결제 금액이 더 많습니다." }
        targets.map { it.instrument.pay(it.amount, userId) }.map { OrderPayment(order, it) }
    }

    data class Command(val userId: UserId, val order: Order, val targets: List<PayTarget>, val previousPayments: OrderPayments)
}
