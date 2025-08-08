package com.loopers.domain.productlike

import com.loopers.domain.BaseEntity
import com.loopers.domain.user.UserId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["product_id", "user_id"])])
data class ProductLike(val productId: Long, val userId: UserId) : BaseEntity()
