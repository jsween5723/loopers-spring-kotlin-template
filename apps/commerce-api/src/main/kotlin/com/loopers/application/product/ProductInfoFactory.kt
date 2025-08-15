package com.loopers.application.product

import com.loopers.domain.brand.Brand
import com.loopers.domain.product.ProductInfo
import com.loopers.domain.product.ProductSignal

class ProductInfoFactory {
    fun generateResults(
        infos: List<ProductInfo>,
        userLikedProductIds: Set<Long>,
    ): List<ProductFacade.Result.ProductResult> = infos
        .map { generateResult(it, userLikedProductIds = userLikedProductIds) }

    fun generateInfo(
        productWithSignal: ProductSignal,
        brand: Brand?,
    ): ProductInfo {
        val product = productWithSignal.product
        return ProductInfo(
            id = product.id,
            name = product.name,
            brandId = product.brandId,
            brandName = brand?.name ?: "",
            displayedAt = product.displayedAt,
            maxQuantity = product.maxQuantity,
            price = product.price,
            stock = product.stock,
            state = product.state,
            likeCount = productWithSignal.likeCount,
        )
    }

    fun generateResult(
        productInfo: ProductInfo,
        userLikedProductIds: Set<Long>,
    ): ProductFacade.Result.ProductResult {
        val isLiked = userLikedProductIds.contains(productInfo.id)
        return ProductFacade.Result.ProductResult(productInfo, isLiked)
    }
}
