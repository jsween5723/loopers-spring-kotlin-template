package com.loopers.application.order

import com.loopers.domain.payment.Paid
import com.loopers.domain.payment.PaymentMethod
import com.loopers.domain.point.UserPoint
import java.math.BigDecimal

class UserPointPay(private val userPoint: UserPoint, override val amount: BigDecimal) : PaymentMethod {
    override val type: PaymentMethod.Type
        get() = PaymentMethod.Type.USER_POINT

    override fun pay(): Paid {
        userPoint.use(amount)
        return Paid(userId = userPoint.userId, amount = amount, methodType = PaymentMethod.Type.USER_POINT)
    }
}
