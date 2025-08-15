package com.loopers.domain.product

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.math.BigDecimal
import java.time.ZonedDateTime

@Entity
class Product(
    val name: String,
    val brandId: Long,
    val displayedAt: ZonedDateTime,
    val maxQuantity: Long,
    val price: BigDecimal,
    var stock: Long,
) : BaseEntity() {
    @Enumerated(EnumType.STRING)
    var state: State = State.AVAILABLE

    fun select(quantity: Long) {
        check(isAvailable()) { "상품을 현재 선택할 수 없습니다." }
        check(maxQuantity >= quantity) { "회당 최대 수량보다 많이 선택할 수 없습니다." }
        check(stock >= quantity) { "재고보다 많이 선택할 수 없습니다." }
    }

    fun deduct(quantity: Long) {
        check(isAvailable()) { "상품을 현재 재고차감할 수 없습니다." }
        check(maxQuantity >= quantity) { "회당 최대 수량보다 많이 재고차감할 수 없습니다." }
        check(stock >= quantity) { "재고보다 많이 재고차감할 수 없습니다." }
        stock -= quantity
    }

    private fun isAvailable(): Boolean = state.isAvailable() && displayedAt.isBefore(ZonedDateTime.now())

    enum class State {
        UNAVAILABLE,
        AVAILABLE,
        ;

        fun isAvailable() = this == AVAILABLE
    }
}
