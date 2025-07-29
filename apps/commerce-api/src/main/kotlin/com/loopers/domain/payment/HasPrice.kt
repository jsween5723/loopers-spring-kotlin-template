package com.loopers.domain.payment

import java.math.BigDecimal

interface HasPrice {
    fun payWith(instrument: PaymentInstrument, amount: BigDecimal)
}
