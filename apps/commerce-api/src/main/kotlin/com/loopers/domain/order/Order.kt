package com.loopers.domain.order

import com.loopers.domain.BaseEntity
import com.loopers.domain.shared.IdAndQuantity
import jakarta.persistence.CascadeType
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import java.math.BigDecimal

@Entity(name = "orders")
class Order(var issuedCouponId: Long = 0L) : BaseEntity() {
    val orderLines = OrderLines()

    val lineItems get() = orderLines.lineItems
    val totalPrice get() = orderLines.totalPrice
    val qtys get() = lineItems.map { IdAndQuantity(it.productId, it.quantity) }
    fun applyCoupon(issuedCouponId: Long) {
        this.issuedCouponId = issuedCouponId
    }
    fun changeTo(lineItems: List<LineItem>) {
        val orderLines = lineItems.map {
            OrderLine(
                orderId = id,
                lineItem = it,
            )
        }
        this.orderLines.changeTo(orderLines)
    }
}

@Embeddable
class OrderLines {
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    val orderLines = mutableListOf<OrderLine>()
    val totalPrice: BigDecimal get() = orderLines.sumOf { it.price }
    val lineItems get() = orderLines.map { it.lineItem }
    fun changeTo(targets: List<OrderLine>) {
        orderLines.clear()
        orderLines.addAll(targets)
    }
}

@Entity
data class OrderLine(@JoinColumn val orderId: Long, val lineItem: LineItem) : BaseEntity() {
    val price: BigDecimal get() = lineItem.totalPrice
}
