package com.loopers.domain.coupon

import com.loopers.domain.BaseEntity
import com.loopers.domain.coupon.Coupon.Type
import com.loopers.domain.user.UserId
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.math.BigDecimal
import java.time.ZonedDateTime

@Entity
class Coupon(val name: String, val amount: BigDecimal, @Enumerated(EnumType.STRING) val type: Type, var stock: Long = 0L) :
    BaseEntity() {
    fun issue(userId: UserId): IssuedCoupon = IssuedCoupon(
            userId = userId,
            coupon = this,
        )

    fun discount(price: BigDecimal): BigDecimal = DiscountPolicyAdaptor().createPolicy(this).discount(price)
    enum class Type {
        FIXED,
        RATE,
    }
}

@Entity
class IssuedCoupon(
    val userId: UserId,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    val coupon: Coupon,
    var usedAt: ZonedDateTime? = null,
) : BaseEntity() {
    fun use() {
        check(usedAt == null) { "이미 사용된 쿠폰입니다." }
        usedAt = ZonedDateTime.now()
    }
    fun discount(price: BigDecimal): BigDecimal = coupon.discount(price)
}
