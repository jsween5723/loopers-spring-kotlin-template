package com.loopers.infrastructure.order

import com.loopers.application.order.required.PaymentPort
import com.loopers.domain.order.Receipt
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PaymentPortImpl: PaymentPort {
    override fun create(receipt: Receipt): UUID {
        TODO("Not yet implemented")
    }
}
