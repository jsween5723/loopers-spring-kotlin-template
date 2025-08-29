package com.loopers.domain.coupon

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class IssuedCouponService(private val issuedCouponRepository: IssuedCouponRepository) {
    fun findIssuedById(id: Long): IssuedCoupon? {
        val coupon = issuedCouponRepository.findById(id)
        check(coupon != null) { "Coupon with id $id not found" }
        return coupon
    }

    @Transactional
    fun reserve(eventId: UUID, issuedCouponId: Long): IssuedCoupon {
        val issuedCoupon = issuedCouponRepository.findById(issuedCouponId) ?: throw EntityNotFoundException()
        issuedCoupon.reserve(eventId)
        return issuedCouponRepository.save(issuedCoupon)
    }

    @Transactional
    fun cancelReserve(eventId: UUID): List<Unit> {
        val issuedCoupons = issuedCouponRepository.findByEventId(eventId)
        return issuedCoupons.map { issuedCoupon -> issuedCoupon.cancelReserve(eventId) }
    }

    @Transactional
    fun use(eventId: UUID, issuedCouponId: Long): IssuedCoupon {
        val issuedCoupon = issuedCouponRepository.findById(issuedCouponId) ?: throw EntityNotFoundException()
        issuedCoupon.use(eventId)
        return issuedCouponRepository.save(issuedCoupon)
    }
}
