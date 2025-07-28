package com.loopers.domain.product

import com.loopers.domain.BaseEntity
import com.loopers.domain.brand.Brand
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity(name = "products")
class Product(
    val name: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    val brand: Brand,
    var maxQuantity: Long,
    var stock: Long,
) : BaseEntity() {
    @Enumerated(EnumType.STRING)
    private var state: State = State.AVAILABLE

    fun deduct(quantity: Long): LineItem {
        check(state.isAvailable()) { "상품을 현재 판매할 수 없습니다." }
        check(brand.isAvailable()) { "브랜드가 입점상태인 상품만 판매할 수 있습니다." }
        check(maxQuantity >= quantity) { "회당 최대 수량보다 많이 판매할 수 없습니다." }
        check(stock >= quantity) { "재고보다 많이 판매할 수 없습니다." }
        stock -= quantity
        return LineItem(product = this, quantity = quantity)
    }

    enum class State {
        UNAVAILABLE,
        AVAILABLE,
        ;

        fun isAvailable() = this == AVAILABLE
    }
}
