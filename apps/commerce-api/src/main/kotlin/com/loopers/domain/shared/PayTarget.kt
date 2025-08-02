package com.loopers.domain.shared

import com.loopers.domain.payment.PaymentInstrument
import java.math.BigDecimal

data class PayTarget(val instrument: PaymentInstrument, val amount: BigDecimal)
