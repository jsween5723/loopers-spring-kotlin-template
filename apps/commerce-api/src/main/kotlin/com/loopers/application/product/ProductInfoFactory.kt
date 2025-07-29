package com.loopers.application.product

import com.loopers.domain.brand.Brand
import com.loopers.domain.product.ProductSignal
import com.loopers.domain.productlike.ProductLike

class ProductInfoFactory {
    fun generate(
        productWithSignal: List<ProductSignal>,
        brands: List<Brand>,
        likes: List<ProductLike>,
    ): List<ProductFacade.Result.ProductInfo> {
        val brandIdMap = brands.associateBy { it.id }
        val likeProductIdMap = likes.associateBy { it.product.id }
        return productWithSignal.map {
            ProductFacade.Result.ProductInfo(
                id = it.product.id,
                name = it.product.name,
                brandId = it.product.brandId,
                brandName = brandIdMap[it.product.brandId]?.name ?: "",
                displayedAt = it.product.displayedAt,
                maxQuantity = it.product.maxQuantity,
                price = it.product.price,
                stock = it.product.stock,
                state = it.product.state,
                likeCount = it.likeCount,
                isLiked = likeProductIdMap.containsKey(it.product.id),
            )
        }
    }
}
