package com.loopers.application.order

import com.loopers.domain.payment.PaymentInstrument
import com.loopers.domain.point.UserPointRepository
import com.loopers.domain.shared.PayTarget
import com.loopers.domain.user.UserId
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PayTargetFactory(private val userPointRepository: UserPointRepository) {
    @Transactional(readOnly = true)
    fun generate(
        userId: UserId,
        targets: List<OrderPayFacade.Criteria.PaymentInstrumentTypeAndAmount>,
    ): List<PayTarget> =
        targets.map {
            when (it.type) {
                PaymentInstrument.Type.USER_POINT -> PayTarget(
                    userPointRepository.findByOrElsePersist(userId),
                    amount = it.amount,
                )
            }
        }
}
