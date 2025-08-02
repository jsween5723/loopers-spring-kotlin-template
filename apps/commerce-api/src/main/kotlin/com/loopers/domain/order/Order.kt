package com.loopers.domain.order

import com.loopers.domain.BaseEntity
import com.loopers.domain.product.LineItem
import com.loopers.domain.shared.ProductAndQuantity
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

@Entity(name = "orders")
class Order : BaseEntity() {
    val orderLines = OrderLines()

    val lineItems get() = orderLines.lineItems
    val totalPrice get() = orderLines.totalPrice
    val productsAndQuantities
        get() = lineItems.map {
            ProductAndQuantity(
                product = it.product,
                quantity = it.quantity,
            )
        }

    fun changeTo(lineItems: List<LineItem>) {
        val orderLines = lineItems.map { OrderLine(order = this, lineItem = it) }
        this.orderLines.changeTo(orderLines)
    }
}

@Embeddable
class OrderLines {
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
data class OrderLine(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order,
    @Embedded
    val lineItem: LineItem,
) : BaseEntity() {
    val price: BigDecimal get() = lineItem.price
}
