package com.loopers.application.coupon

import com.loopers.domain.coupon.CouponRepository
import com.loopers.domain.coupon.IssuedCouponRepository
import com.loopers.domain.user.UserId
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponFacade(private val couponRepository: CouponRepository, private val issuedCouponRepository: IssuedCouponRepository) {
    @Transactional
    fun issue(userId: UserId, couponId: Long): Result {
        val coupon = couponRepository.getForUpdateById(couponId)
        val issuedCoupon = issuedCouponRepository.save(coupon.issue(userId))
        return Result(issuedCoupon.id)
    }

    data class Result(val issuedCouponId: Long)
}
