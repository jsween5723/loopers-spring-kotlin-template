package com.loopers.domain.point

import com.loopers.domain.BaseEntity
import com.loopers.domain.user.UserId
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import org.hibernate.annotations.NaturalId
import java.math.BigDecimal
import java.util.UUID

@Entity
class UserPoint(
    var point: BigDecimal = BigDecimal.ZERO,
    @NaturalId
    val userId: UserId,
) : BaseEntity() {

    @OneToMany(mappedBy = "user_point", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val reserved: MutableList<UserPointReserve> = mutableListOf()
    private val reservedAmount: BigDecimal = reserved.sumOf { it.amount }
    fun reserve(eventId: UUID, amount: BigDecimal): UserPointReserve {
        val reservedItem = UserPointReserve(eventId, userId, amount)
        check(amount + reservedAmount <= point) { "사용 금액은 보유량 이하여야 합니다." }
        reserved.add(reservedItem)
        return reservedItem
    }

    fun cancelReserve(eventId: UUID, amount: BigDecimal) {
        reserved.remove(UserPointReserve(eventId, userId, amount))
    }

    fun charge(amount: BigDecimal) {
        require(amount >= BigDecimal.ZERO) { "충전 금액은 0 이상이어야 합니다." }
        point = point.add(amount)
    }

    fun use(eventId: UUID, amount: BigDecimal) {
        val reservedItem = UserPointReserve(eventId, userId, amount)
        check(reserved.contains(reservedItem)) { "예약된 금액만 사용 가능합니다." }
        point = point.subtract(amount)
        reserved.remove(reservedItem)
    }
}
