package com.loopers.infrastructure.payment

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import java.util.UUID

@HttpExchange(url = "/api/v1/payments")
interface PaymentGateway {
    @PostExchange
    @CircuitBreaker(name = "pg-get")
    @Retry(name = "pg-request", fallbackMethod = "requestPaymentFallback")
    fun request(@RequestBody request: PaymentDto.PaymentRequest): PaymentDto.TransactionResponse

    @GetExchange("/{transactionKey}")
    @CircuitBreaker(name = "pg-get")
    fun getTransaction(@PathVariable transactionKey: String): PaymentDto.TransactionDetailResponse

    private fun requestPaymentFallback(
       request: PaymentDto.PaymentRequest,
        ex: Throwable,
    ): PaymentDto.TransactionResponse {
        log.error("PG createPayment failed $request", ex)
        return PaymentDto.TransactionResponse(
            transactionKey = UUID.randomUUID().toString(),
            status = PaymentDto.TransactionStatusResponse.FAILED,
            reason = null,
        )
    }
}

@Configuration
class PaymentGatewayConfiguration {
    @Bean
    fun paymentGateway(
        @Value("\${payment-gateway.base-url}") baseUrl: String,
        @Value("\${payment-gateway.client-id}") clientId: String,
    ): PaymentGateway {
        val restClient = RestClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("X-USER-ID", clientId)
            .build()
        val adapter = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builderFor(adapter)
            .build()
        return factory.createClient(PaymentGateway::class.java)
    }
}
