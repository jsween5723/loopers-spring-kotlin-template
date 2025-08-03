package com.loopers.domain.order

import jakarta.persistence.Embeddable
import java.math.BigDecimal

@Embeddable
data class LineItem(val productId: Long, val quantity: Long, val productName: String, val brandId: Long, val price: BigDecimal) {
    val totalPrice get() = price * quantity.toBigDecimal()
}
