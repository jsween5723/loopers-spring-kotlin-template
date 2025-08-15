package com.loopers.domain.point

import com.loopers.domain.BaseEntity
import com.loopers.domain.user.UserId
import jakarta.persistence.Entity
import org.hibernate.annotations.NaturalId
import java.math.BigDecimal

@Entity
class UserPoint(
    var point: BigDecimal = BigDecimal.ZERO,
    @NaturalId
    val userId: UserId,
) : BaseEntity() {
    fun charge(amount: BigDecimal) {
        require(amount >= BigDecimal.ZERO) { "충전 금액은 0 이상이어야 합니다." }
        point = point.add(amount)
    }

    fun use(amount: BigDecimal) {
        check(amount <= point) { "사용 금액은 보유량 이하여야 합니다." }
        point = point.subtract(amount)
    }
}
