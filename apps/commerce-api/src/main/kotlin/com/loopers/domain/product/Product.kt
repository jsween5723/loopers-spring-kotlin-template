package com.loopers.domain.product

import com.loopers.domain.BaseEntity
import com.loopers.domain.brand.Brand
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.math.BigDecimal
import java.time.ZonedDateTime

@Entity(name = "products")
class Product(
    val name: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    val brand: Brand,
    val displayedAt: ZonedDateTime,
    val maxQuantity: Long,
    val price: BigDecimal,
    var stock: Long,
) : BaseEntity() {
    @Enumerated(EnumType.STRING)
    private var state: State = State.AVAILABLE

    fun sale(quantity: Long): LineItem {
        check(isAvailable()) { "상품을 현재 판매할 수 없습니다." }
        check(brand.isAvailable()) { "브랜드가 판매 가능한 상태가 아닙니다." }
        check(maxQuantity >= quantity) { "회당 최대 수량보다 많이 판매할 수 없습니다." }
        check(stock >= quantity) { "재고보다 많이 판매할 수 없습니다." }
        stock -= quantity
        return LineItem.from(product = this, quantity = quantity)
    }

    private fun isAvailable(): Boolean = state.isAvailable() && displayedAt.isBefore(ZonedDateTime.now())

    enum class State {
        UNAVAILABLE,
        AVAILABLE,
        ;

        fun isAvailable() = this == AVAILABLE
    }
}
