package com.loopers.domain.brand

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity

@Entity
class Brand(val name: String) : BaseEntity() {
    var state = State.OPENED

    fun isAvailable(): Boolean = state == State.OPENED
    enum class State {
        CLOSED,
        OPENED,
    }
}
