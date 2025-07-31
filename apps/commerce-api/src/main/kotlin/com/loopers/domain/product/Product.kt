package com.loopers.domain.product

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Entity(name = "products")
class Product(val name: String, var stock: Long) : BaseEntity() {
    @Enumerated(EnumType.STRING)
    private var state: State = State.AVAILABLE

    fun deduct(quantity: Long): LineItem {
        check(state.isAvailable()) { "상품을 현재 판매할 수 없습다니다." }
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
