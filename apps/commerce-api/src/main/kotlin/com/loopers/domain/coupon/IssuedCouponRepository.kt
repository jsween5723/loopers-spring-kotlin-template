package com.loopers.domain.coupon

import java.util.UUID

interface IssuedCouponRepository {
    fun save(issuedCoupon: IssuedCoupon): IssuedCoupon
    fun findById(issuedCouponId: Long): IssuedCoupon?
    fun getById(issuedCouponId: Long): IssuedCoupon
    fun findByEventId(eventId: UUID): List<IssuedCoupon>
}
