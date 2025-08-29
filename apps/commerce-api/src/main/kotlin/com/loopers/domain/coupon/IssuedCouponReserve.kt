package com.loopers.domain.coupon

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import org.springframework.data.annotation.Immutable
import java.util.UUID

@Entity
@Immutable
data class IssuedCouponReserve(val eventId: UUID, @JoinColumn val issuedCouponId: Long) : BaseEntity()
