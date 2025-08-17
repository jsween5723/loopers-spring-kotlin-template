package com.loopers.application.order

import com.loopers.domain.coupon.IssuedCouponRepository
import com.loopers.domain.order.LineItem
import com.loopers.domain.order.OrderRepository
import com.loopers.domain.product.ProductRepository
import com.loopers.domain.shared.IdAndQuantity
import com.loopers.domain.user.UserId
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.ZonedDateTime

@Component
class OrderCreateFacade(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val issuedCouponRepository: IssuedCouponRepository,
) {
    private val orderCreateService = OrderCreateService()
    private val lineItemService = LineItemService()

    @Transactional
    fun create(userId: UserId, productIdWithQty: List<IdAndQuantity>, issuedCouponId: Long = 0L): Result {
        val products = productIdWithQty.map { it.productId }.let { productRepository.getByIds(it) }
        val lineItems = lineItemService.toLineItem(products, productIdWithQty)
        val issuedCoupon = issuedCouponRepository.findById(issuedCouponId)
        val order = orderCreateService.create(lineItems, issuedCoupon)
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
