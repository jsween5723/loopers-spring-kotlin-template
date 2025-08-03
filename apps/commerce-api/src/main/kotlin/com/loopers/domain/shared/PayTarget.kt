package com.loopers.domain.shared

import com.loopers.domain.payment.PaymentMethod
import java.math.BigDecimal

data class PayTarget(val instrument: PaymentMethod, val amount: BigDecimal)
