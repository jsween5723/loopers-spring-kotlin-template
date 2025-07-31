package com.loopers.domain.order

import com.loopers.domain.BaseEntity
import com.loopers.domain.product.LineItem
import jakarta.persistence.CascadeType
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderColumn
import java.math.BigDecimal
import java.time.ZonedDateTime

@Entity(name = "`order`")
class Order(lineItems: List<LineItem>) : BaseEntity() {
    @Embedded
    private val orderLines = OrderLines().apply { updateLineItems(lineItems) }
    val lineItems get() = orderLines.lineItems
    val totalPrice: BigDecimal get() = orderLines.totalPrice
    val orderedAt: ZonedDateTime = ZonedDateTime.now()
    var paidAt: ZonedDateTime? = null
    fun updateLineItems(lineItems: List<LineItem>) {
        orderLines.changeTo(lineItems.map { OrderLine(this, it) })
    }
}

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
