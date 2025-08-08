package com.loopers.infrastructure.coupon

import com.loopers.domain.coupon.Coupon
import com.loopers.domain.coupon.CouponRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CouponRepositoryImpl(private val jpaRepository: CouponJpaRepository) : CouponRepository {
    override fun save(coupon: Coupon): Coupon = jpaRepository.save(coupon)
    override fun getForUpdateById(couponId: Long) =
        jpaRepository.findForUpdateById(couponId) ?: throw EntityNotFoundException("No coupon found with id $couponId")
    override fun getById(id: Long): Coupon =
        jpaRepository.findByIdOrNull(id) ?: throw EntityNotFoundException("Entity with id $id not found")
}

@Repository
interface CouponJpaRepository : JpaRepository<Coupon, Long> {
    fun findForUpdateById(couponId: Long): Coupon
}
