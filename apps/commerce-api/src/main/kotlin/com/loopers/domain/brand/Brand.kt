package com.loopers.domain.brand

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import org.hibernate.annotations.NaturalId

@Entity
class Brand(@NaturalId val name: String) : BaseEntity() {
    var state = State.OPENED

    fun isAvailable(): Boolean = state == State.OPENED
    enum class State {
        CLOSED,
        OPENED,
    }
}
