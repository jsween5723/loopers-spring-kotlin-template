package com.loopers.application.order

import com.loopers.domain.payment.CardInternalPayProcessor
import com.loopers.domain.payment.InternalPayProcessor
import com.loopers.domain.payment.PaymentMethod
import com.loopers.domain.point.UserPointRepository
import com.loopers.domain.user.UserId
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PayProcessorFactory(private val userPointRepository: UserPointRepository) {
    @Transactional(readOnly = true)
    fun generate(
        userId: UserId,
        targets: List<PaymentMethod>,
    ): Map<InternalPayProcessor.Type, InternalPayProcessor<*>> = targets.associateBy(
            { it.type },
        ) {
            when (it) {
                is PaymentMethod.PointPay -> PointInternalPayProcessor(
                    userPointRepository = userPointRepository,
                    it.amount,
                )

                is PaymentMethod.CardPay -> CardInternalPayProcessor(
                    card = it.card,
                )
            }
        }
}
