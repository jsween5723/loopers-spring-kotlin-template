package com.loopers.application.order

import com.loopers.domain.payment.InternalPayProcessor
import com.loopers.domain.payment.Payment
import com.loopers.domain.payment.PaymentInfo
import com.loopers.domain.point.UserPointRepository
import com.loopers.domain.user.UserId
import java.math.BigDecimal
import java.util.UUID

class PointInternalPayProcessor(private val userPointRepository: UserPointRepository, private val amount: BigDecimal) :
    InternalPayProcessor<Payment.PointPayment> {

    override fun pay(orderId: UUID, userId: UserId, remainPrice: BigDecimal): Payment {
        val userPoint = userPointRepository.findByOrElsePersist(userId)
        check(remainPrice - amount >= BigDecimal.ZERO) { "포인트 사용량이 주문 금액보다 많습니다." }
        userPoint.use(amount)
        return Payment.PointPayment(PaymentInfo.paid(userId = userId, amount = amount), orderId = orderId)
    }
}
