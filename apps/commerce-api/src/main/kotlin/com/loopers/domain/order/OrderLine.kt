package com.loopers.domain.order

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import java.math.BigDecimal
import java.util.UUID

@Entity
class OrderLine(
    val orderId: UUID,
    val productId: UUID,
    val name: String,
    val quantity: Long,
    val price: BigDecimal,
) : BaseEntity() {
    val totalPrice: BigDecimal get() = price.multiply(quantity.toBigDecimal())
}
