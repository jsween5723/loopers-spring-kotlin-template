package com.loopers.domain.coupon

interface IssuedCouponRepository {
    fun save(issuedCoupon: IssuedCoupon): IssuedCoupon
    fun findById(issuedCouponId: Long): IssuedCoupon?
    fun getById(issuedCouponId: Long): IssuedCoupon
}
