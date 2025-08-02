package com.loopers.domain.productlike

import com.loopers.domain.BaseEntity
import com.loopers.domain.product.Product
import com.loopers.domain.user.UserId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
data class ProductLike(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: Product,
    val userId: UserId,
) : BaseEntity()
