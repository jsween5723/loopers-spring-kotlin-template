package com.loopers.application.order

import com.loopers.domain.payment.PaymentInfo
import com.loopers.domain.payment.PaymentMethod
import com.loopers.domain.point.UserPoint
import java.math.BigDecimal

class UserPointPay(private val userPoint: UserPoint) : PaymentMethod {
    override val type: PaymentMethod.Type
        get() = PaymentMethod.Type.USER_POINT

    override fun pay(amount: BigDecimal): PaymentInfo {
        userPoint.use(amount)
        return PaymentInfo.paid(userId = userPoint.userId, amount = amount, method = this)
    }
}
