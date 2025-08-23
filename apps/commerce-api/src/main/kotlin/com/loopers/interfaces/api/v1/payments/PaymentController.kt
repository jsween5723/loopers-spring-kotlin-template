package com.loopers.interfaces.api.v1.payments

import com.loopers.application.payment.PaymentFacade
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/payments")
class PaymentController(private val paymentFacade: PaymentFacade) {
    @PostMapping
    fun process(@RequestBody transaction: PaymentRequest.TransactionInfo) {
        paymentFacade.process(transaction)
    }
}
