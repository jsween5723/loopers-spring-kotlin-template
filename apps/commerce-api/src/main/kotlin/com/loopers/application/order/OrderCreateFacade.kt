package com.loopers.application.order

import com.loopers.domain.order.OrderCreateService
import com.loopers.domain.order.OrderRepository
import com.loopers.domain.product.LineItem
import com.loopers.domain.product.ProductRepository
import com.loopers.domain.shared.ProductAndQuantity
import com.loopers.domain.shared.ProductIdAndQuantity
import com.loopers.domain.user.UserId
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.ZonedDateTime

@Component
class OrderCreateFacade(private val orderRepository: OrderRepository, private val productRepository: ProductRepository) {
    private val orderCreateService = OrderCreateService()

    @Transactional
    fun create(userId: UserId, selects: List<ProductIdAndQuantity>): Result {
        val products = productRepository.findByIdIn(selects.map { it.productId })
        val order = orderCreateService.create(ProductAndQuantity.of(products, selects))
            .let(orderRepository::save)
        return Result(
            id = order.id,
            lineItems = order.lineItems,
            totalPrice = order.totalPrice,
            createdAt = order.createdAt,
        )
    }

    data class Result(val id: Long, val lineItems: List<LineItem>, val totalPrice: BigDecimal, val createdAt: ZonedDateTime)
}
