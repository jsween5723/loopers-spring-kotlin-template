package com.loopers.domain.coupon

import com.loopers.domain.BaseEntity
import com.loopers.domain.user.UserId
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.math.BigDecimal

@Entity
class Coupon(val name: String, val amount: BigDecimal, @Enumerated(EnumType.STRING) val type: Type, var stock: Long = 0L) :
    BaseEntity() {
    fun issue(userId: UserId): IssuedCoupon = IssuedCoupon(
            userId = userId,
            coupon = this,
        )
    enum class Type {
        FIXED,
        RATE,
    }
}
