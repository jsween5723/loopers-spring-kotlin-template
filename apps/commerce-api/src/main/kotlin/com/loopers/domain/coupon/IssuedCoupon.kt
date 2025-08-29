package com.loopers.domain.coupon

import com.loopers.domain.BaseEntity
import com.loopers.domain.user.UserId
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderBy
import jakarta.persistence.Version
import java.time.ZonedDateTime
import java.util.UUID

@Entity
class IssuedCoupon(
    val userId: UserId,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    val coupon: Coupon,
    private var usedAt: ZonedDateTime? = null,
) : BaseEntity() {
    @OneToMany(mappedBy = "issuedCouponId", cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("createdAt")
    private val reserved: LinkedHashSet<IssuedCouponReserve> = LinkedHashSet()

    @Version
    var version: Long = 0L

    fun use(eventId: UUID) {
        val reservedItem = IssuedCouponReserve(eventId, id)
        check(reserved.contains(reservedItem)) { "예약 후 사용 할 수 있습니다." }
        usedAt = ZonedDateTime.now()
        reserved.remove(reservedItem)
    }

    fun reserve(eventId: UUID): IssuedCouponReserve {
        val reservedItem = IssuedCouponReserve(eventId, id)
        check(usedAt == null && !reserved.contains(reservedItem)) { "이미 사용된 쿠폰입니다." }
        reserved.add(reservedItem)
        return reservedItem
    }

    fun cancelReserve(eventId: UUID) {
        reserved.remove(IssuedCouponReserve(eventId, id))
    }
}
