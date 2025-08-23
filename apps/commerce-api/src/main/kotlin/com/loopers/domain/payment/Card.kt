package com.loopers.domain.payment

import jakarta.persistence.Embeddable

@Embeddable
data class Card(val cardNumber: String, val type: Type) {
    enum class Type {
        SAMSUNG,
        KB,
        HYUNDAI,
    }
}
