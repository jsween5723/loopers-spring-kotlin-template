package com.loopers.domain.product

import com.loopers.domain.BaseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.OneToMany
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.UUID

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

    @OneToMany(mappedBy = "skuId", cascade = [CascadeType.ALL], orphanRemoval = true)
    val reserved: MutableSet<ProductReserve> = mutableSetOf()

    private val reservedStock = reserved.sumOf { it.quantity }

    fun reserve(eventId: UUID, quantity: Long): ProductReserve {
        check(isAvailable()) { "상품을 현재 차감할 수 없습니다." }
        check(maxQuantity >= quantity) { "회당 최대 수량보다 많이 차감할 수 없습니다." }
        check(stock + reservedStock >= quantity) { "재고보다 많이 차감할 수 없습니다." }
        val reservedItem = ProductReserve(eventId, id, quantity)
        reserved.add(reservedItem)
        return reservedItem
    }

    fun cancelReserve(eventId: UUID, quantity: Long) {
        reserved.remove(ProductReserve(eventId, id, quantity))
    }

    fun deduct(eventId: UUID, quantity: Long) {
        val reservedItem = ProductReserve(eventId = eventId, skuId = id, quantity = quantity)
        check(reserved.contains(reservedItem)) { "예약된 재고만 차감할 수 있습니다." }
        stock -= quantity
        reserved.remove(reservedItem)
    }

    private fun isAvailable(): Boolean = state.isAvailable() && displayedAt.isBefore(ZonedDateTime.now())

    enum class State {
        UNAVAILABLE,
        AVAILABLE,
        ;

        fun isAvailable() = this == AVAILABLE
    }
}
