package com.loopers.application.order.required

import com.loopers.domain.order.Receipt
import java.util.UUID

interface PaymentPort {
    /**
     *
     * @return 생성된 paymentId
     */
    fun create(receipt: Receipt): UUID
}
