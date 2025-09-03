package com.loopers.interfaces.api.v1.orders

import com.loopers.domain.order.Card
import java.math.BigDecimal
import java.util.UUID

object OrderRequest {
    data class Place(val qtys: List<Pair<UUID, Long>>)
    data class Pay(val issuedCouponId: UUID, val pointAmount: BigDecimal, val card: CardDTO)
    data class CardDTO(val cardNo: String, val type: Type) {
        enum class Type {
            SAMSUNG, HYUNDAI, KB;
            fun toCardType(): Card.Type {
                return when(this) {
                    SAMSUNG -> Card.Type.SAMSUNG
                    HYUNDAI -> Card.Type.HYUNDAI
                    KB -> Card.Type.KB
                }
            }
        }

        fun toCard() : Card = Card(type.toCardType(), cardNo)
    }
}
