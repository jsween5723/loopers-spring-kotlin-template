package com.loopers.infrastructure.payment

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
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

@HttpExchange(url = "/api/v1/payments")
interface PaymentGateway {
    @PostExchange
    @CircuitBreaker(name = "pg-get")
    fun request(@RequestBody request: PaymentDto.PaymentRequest): PaymentDto.TransactionResponse

    @GetExchange("/{transactionKey}")
    @CircuitBreaker(name = "pg-get")
    fun getTransaction(@PathVariable transactionKey: String): PaymentDto.TransactionDetailResponse
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
