package com.loopers.domain.order

import com.loopers.domain.BaseEntity
import com.loopers.domain.payment.HasPrice
import com.loopers.domain.payment.Payment
import com.loopers.domain.payment.PaymentInstrument
import com.loopers.domain.product.LineItem
import jakarta.persistence.CascadeType
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderColumn
import java.math.BigDecimal

@Entity(name = "`order`")
class Order :
    BaseEntity(),
    HasPrice {
    private val orderLines = OrderLines()
    private val orderPayments = OrderPayments()

    val lineItems get() = orderLines.lineItems
    val totalPrice get() = orderLines.totalPrice
    val remainPrice get() = totalPrice - orderPayments.totalPrice

    override fun payWith(instrument: PaymentInstrument, amount: BigDecimal) {
        check(remainPrice >= amount) { "남은 금액보다 결제 금액이 더 많습니다." }
        val paid = instrument.pay(amount)
        orderPayments.addPayment(OrderPayment(order = this, payment = paid))
    }

    fun updateLineItems(lineItems: List<LineItem>) {
        orderLines.changeTo(lineItems.map { OrderLine(this, it) })
    }
}

@Embeddable
private class OrderLines {
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderColumn(name = "position")
    val orderLines = mutableListOf<OrderLine>()
    val totalPrice: BigDecimal get() = orderLines.sumOf { it.price }
    val lineItems get() = orderLines.map { it.lineItem }
    fun changeTo(targets: List<OrderLine>) {
        orderLines.clear()
        orderLines.addAll(targets)
    }
}

@Entity
private data class OrderLine(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order,
    @Embedded
    val lineItem: LineItem,
) : BaseEntity() {
    val price: BigDecimal get() = lineItem.price
}

@Entity
private class OrderPayment(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    val order: Order,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    val payment: Payment,
) : BaseEntity()

@Embeddable
private class OrderPayments {
    @OneToMany(mappedBy = "order", cascade = [(CascadeType.ALL)], orphanRemoval = true)
    private val orderPayments = mutableListOf<OrderPayment>()
    val payments get() = orderPayments.map { it.payment }
    val paidPrice
        get() = orderPayments.filter { it.payment.type == Payment.Type.PAID }
            .sumOf { it.payment.amount }
    val totalPrice get() = paidPrice
    fun addPayment(payment: OrderPayment) {
        orderPayments.add(payment)
    }
}
