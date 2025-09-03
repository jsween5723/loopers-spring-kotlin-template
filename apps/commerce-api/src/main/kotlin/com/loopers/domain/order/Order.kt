package com.loopers.domain.order

import com.loopers.domain.BaseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.OneToMany
import java.math.BigDecimal
import java.util.UUID

@Entity(name = "orders")
class Order(
    val userId: UUID,
    @Enumerated(EnumType.STRING)
    var status: Status = Status.PLACED,
    var issuedCouponId: UUID? = null,
) : BaseEntity() {

    @OneToMany(mappedBy = "orderId", cascade = [(CascadeType.ALL)], orphanRemoval = true)
    val orderLines: MutableList<OrderLine> = mutableListOf()
    val totalPrice: BigDecimal get() = orderLines.sumOf { it.totalPrice }

    fun applyIssuedCoupon(reservedIssuedCoupon: ReservedIssuedCoupon) {
        this.issuedCouponId = reservedIssuedCoupon.issuedCouponId
    }

    fun addOrderLines(orderLines: List<OrderLine>) {
        this.orderLines.addAll(orderLines)
    }

    fun readyForPayment(remainingPrice: BigDecimal, card: Card): Receipt {
        if (status != Status.PLACED) throw IllegalStateException("Order order has already been payed")
        status = Status.PAYMENT_READY
        return Receipt(orderId = id, amount = remainingPrice, card = card)
    }

    fun cancel() {
        status = Status.CANCELLED
    }

    fun complete() {
        status = Status.COMPLETED
    }

    enum class Status {
        PLACED, PAYMENT_READY, CANCELLED, COMPLETED;
    }
}
