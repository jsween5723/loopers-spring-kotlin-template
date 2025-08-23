package com.loopers.application.product

import com.loopers.domain.brand.Brand
import com.loopers.domain.product.ProductInfo
import com.loopers.domain.product.ProductSignal
import com.loopers.domain.productlike.ProductLike

class ProductInfoFactory {
    fun generate(
        productWithSignals: List<ProductSignal>,
        brands: List<Brand>,
        likes: List<ProductLike>,
    ): List<ProductFacade.Result.ProductInfo> {
        val brandIdMap = brands.associateBy { it.id }
        return productWithSignals.map {
            generate(productWithSignal = it, brand = brandIdMap[it.product.brandId], likes = likes)
        }
    }
    fun generate(
        productWithSignal: ProductSignal,
        brand: Brand?,
        likes: Collection<ProductLike>,
    ): ProductFacade.Result.ProductInfo {
        val product = productWithSignal.product
        val likeProductIdMap = likes.associateBy { it.productId }
        return ProductFacade.Result.ProductInfo(
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
            isLiked = likeProductIdMap.containsKey(product.id),
        )
    }
}
