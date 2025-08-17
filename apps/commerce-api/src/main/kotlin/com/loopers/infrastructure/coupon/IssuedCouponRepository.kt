package com.loopers.infrastructure.coupon

import com.loopers.domain.coupon.IssuedCoupon
import com.loopers.domain.coupon.IssuedCouponRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class IssuedCouponRepositoryImpl(private val jpaRepository: IssuedCouponJpaRepository) : IssuedCouponRepository {
    override fun save(issuedCoupon: IssuedCoupon): IssuedCoupon = jpaRepository.save(issuedCoupon)
    override fun findById(issuedCouponId: Long): IssuedCoupon? =
        jpaRepository.findByIdOrNull(issuedCouponId)

    override fun getById(issuedCouponId: Long): IssuedCoupon =
        jpaRepository.findByIdOrNull(issuedCouponId) ?: throw EntityNotFoundException("쿠폰을 찾을 수 없습니다.")
}

@Repository
interface IssuedCouponJpaRepository : JpaRepository<IssuedCoupon, Long>
