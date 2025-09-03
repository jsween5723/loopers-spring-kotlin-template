package com.loopers.domain.order

data class Card(val type: Type, val cardNo: String) {
    enum class Type {
        SAMSUNG, KB, HYUNDAI
    }
}
