package com.loopers.domain.point

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import org.hibernate.annotations.NaturalId
import java.math.BigDecimal

@Entity(name = "user_points")
class UserPoint(var point: BigDecimal = BigDecimal.ZERO, @NaturalId val userId: Long) : BaseEntity() {
    fun charge(amount: BigDecimal) {
        point = point.add(amount)
    }
}
