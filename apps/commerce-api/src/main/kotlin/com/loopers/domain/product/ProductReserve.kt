package com.loopers.domain.product

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import org.springframework.data.annotation.Immutable
import java.util.UUID

@Entity
@Immutable
data class ProductReserve(val eventId: UUID, @JoinColumn val skuId: Long, val quantity: Long) : BaseEntity()
