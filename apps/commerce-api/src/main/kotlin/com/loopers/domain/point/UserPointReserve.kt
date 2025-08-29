package com.loopers.domain.point

import com.loopers.domain.BaseEntity
import com.loopers.domain.user.UserId
import jakarta.persistence.Entity
import java.math.BigDecimal
import java.util.UUID

@Entity
class UserPointReserve(val eventId: UUID, val userId: UserId, val amount: BigDecimal) : BaseEntity()
