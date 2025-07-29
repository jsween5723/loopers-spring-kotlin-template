package com.loopers.domain.payment

import com.loopers.domain.orderpayment.OrderPayments
import java.math.BigDecimal

interface HasPrice {
    fun payWith(instrument: PaymentInstrument, amount: BigDecimal, payments: OrderPayments): OrderPayments
}
