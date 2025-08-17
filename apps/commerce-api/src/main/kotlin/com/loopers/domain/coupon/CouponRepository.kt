package com.loopers.domain.coupon

interface CouponRepository {
    fun save(coupon: Coupon): Coupon
    fun getById(id: Long): Coupon
    fun getForUpdateById(couponId: Long): Coupon
}
