package com.loopers.application.order

import com.loopers.domain.payment.PaymentMethod
import com.loopers.domain.point.UserPointRepository
import com.loopers.domain.user.UserId
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PaymentMethodFactory(private val userPointRepository: UserPointRepository) {
    @Transactional(readOnly = true)
    fun generate(
        userId: UserId,
        targets: List<OrderPayFacade.Criteria.PaymentMethodTypeAndAmount>,
    ): List<PaymentMethod> =
        targets.map {
            when (it.type) {
                PaymentMethod.Type.USER_POINT -> UserPointPay(
                    userPoint = userPointRepository.findByOrElsePersist(userId),
                    amount = it.amount,
                )
            }
        }
}
