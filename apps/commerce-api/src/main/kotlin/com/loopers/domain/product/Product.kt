package com.loopers.domain.product

import com.loopers.domain.BaseEntity
import com.loopers.domain.brand.Brand
import jakarta.persistence.CascadeType
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
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "brand_id", nullable = false)
    val brand: Brand,
    val displayedAt: ZonedDateTime,
    val maxQuantity: Long,
    val price: BigDecimal,
    var stock: Long,
) : BaseEntity() {
    @Enumerated(EnumType.STRING)
    private var state: State = State.AVAILABLE

    fun select(quantity: Long): LineItem {
        check(isAvailable()) { "상품을 현재 선택할 수 없습니다." }
        check(brand.isAvailable()) { "브랜드가 선택 가능한 상태가 아닙니다." }
        check(maxQuantity >= quantity) { "회당 최대 수량보다 많이 선택할 수 없습니다." }
        check(stock >= quantity) { "재고보다 많이 선택할 수 없습니다." }
        return LineItem.from(product = this, quantity = quantity)
    }

    fun deduct(quantity: Long): LineItem {
        check(isAvailable()) { "상품을 현재 재고차감할 수 없습니다." }
        check(brand.isAvailable()) { "브랜드가 재고차감 가능한 상태가 아닙니다." }
        check(maxQuantity >= quantity) { "회당 최대 수량보다 많이 재고차감할 수 없습니다." }
        check(stock >= quantity) { "재고보다 많이 재고차감할 수 없습니다." }
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
