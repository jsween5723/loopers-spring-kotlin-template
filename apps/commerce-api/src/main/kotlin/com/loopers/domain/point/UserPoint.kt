package com.loopers.domain.point

import com.loopers.domain.BaseEntity
import com.loopers.domain.payment.Paid
import com.loopers.domain.payment.Payment
import com.loopers.domain.payment.PaymentInstrument
import com.loopers.domain.user.UserId
import jakarta.persistence.Entity
import org.hibernate.annotations.NaturalId
import java.math.BigDecimal

@Entity(name = "user_points")
class UserPoint(
    var point: BigDecimal = BigDecimal.ZERO,
    @NaturalId
    val userId: UserId,
) : BaseEntity(),
    PaymentInstrument {
    fun charge(amount: BigDecimal) {
        require(amount >= BigDecimal.ZERO) { "충전 금액은 0 이상이어야 합니다." }
        point = point.add(amount)
    }

    private fun use(amount: BigDecimal) {
        require(amount <= point) { "사용 금액은 보유량 이하여야 합니다." }
        point = point.subtract(amount)
    }

    override val type: PaymentInstrument.Type
        get() = PaymentInstrument.Type.USER_POINT

    override fun pay(amount: BigDecimal, userId: UserId): Paid {
        use(amount)
        return Payment.paid(
            target = amount,
            instrument = this,
            userId = userId,
        )
    }
}
