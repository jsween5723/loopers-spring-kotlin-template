package com.loopers.domain.point

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import org.hibernate.annotations.NaturalId
import java.math.BigDecimal

@Entity(name = "user_points")
class UserPoint(var point: BigDecimal = BigDecimal.ZERO, @NaturalId val userId: Long) : BaseEntity() {
    fun charge(amount: BigDecimal) {
        require(amount >= BigDecimal.ZERO) { "충전 금액은 0 이상이어야 합니다." }
        point = point.add(amount)
    }
}
