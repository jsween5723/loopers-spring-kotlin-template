package com.loopers.domain.product

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne

@Entity
class ProductSignal(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product,
    var likeCount: Long = 0L,
) : BaseEntity() {
    fun increaseLikeCount() {
        likeCount += 1
    }
    fun decreaseLikeCount() {
        check(likeCount > 0L) { "좋아요 수는 1 이상이어야한다." }
        likeCount -= 1
    }
}
