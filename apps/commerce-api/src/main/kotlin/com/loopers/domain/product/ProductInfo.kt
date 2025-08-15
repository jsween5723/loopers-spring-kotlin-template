package com.loopers.domain.product

import java.math.BigDecimal
import java.time.ZonedDateTime

data class ProductInfo(
    val id: Long,
    val name: String,
    val brandId: Long,
    val brandName: String,
    val displayedAt: ZonedDateTime,
    val maxQuantity: Long,
    val price: BigDecimal,
    val stock: Long,
    val state: Product.State,
    val likeCount: Long,
)
