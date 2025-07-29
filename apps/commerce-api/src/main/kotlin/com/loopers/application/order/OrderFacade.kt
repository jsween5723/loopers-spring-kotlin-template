package com.loopers.application.order

import com.loopers.domain.auth.Authentication
import com.loopers.domain.order.OrderService
import com.loopers.domain.product.LineItem
import com.loopers.domain.shared.ProductIdAndQuantity
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.ZonedDateTime

@Component
class OrderCreateUseCase(private val orderService: OrderService) {
    @Transactional
    fun create(authentication: Authentication, selects: List<ProductIdAndQuantity>): Result {
        val lineItems = selects.map {
            val product = (
                productRepository.findByIdOrNull(it.productId)
                ?: throw EntityNotFoundException("${it.productId} 상품이 존재하지 않습니다.")
            )
            product.sale(it.quantity)
        }
        val order = orderService.create(lineItems)
        return Result(
            id = order.id,
            lineItems = order.lineItems,
            totalPrice = order.totalPrice,
            createdAt = order.createdAt,
        )
    }

    data class Result(val id: Long, val lineItems: List<LineItem>, val totalPrice: BigDecimal, val createdAt: ZonedDateTime)
}
