package com.loopers.domain.coupon

import org.springframework.stereotype.Service

@Service
class CouponService(private val couponRepository: CouponRepository, private val issuedCouponRepository: IssuedCouponRepository) {
    fun findIssuedById(id: Long): IssuedCoupon? {
        val coupon = issuedCouponRepository.findById(id)
        check(coupon != null) { "Coupon with id $id not found" }
        return coupon
    }
}
