package com.loopers.application.product

import com.loopers.domain.brand.BrandRepository
import com.loopers.domain.product.ProductInfo
import com.loopers.domain.product.ProductKey
import com.loopers.domain.product.ProductQuery
import com.loopers.domain.product.ProductSearchRepository
import com.loopers.domain.product.ProductSignalRepository
import com.loopers.domain.productlike.ProductLikeRepository
import com.loopers.domain.shared.CacheTemplate
import com.loopers.domain.user.UserId
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.awt.SystemColor.info

@Component
class ProductFacade(
    private val repository: ProductSearchRepository,
    private val brandRepository: BrandRepository,
    private val likeRepository: ProductLikeRepository,
    private val productSignalRepository: ProductSignalRepository,
    private val productCacheTemplate: CacheTemplate<ProductInfo>,
) {
    private val factory = ProductInfoFactory()

    @Transactional(readOnly = true)
    fun search(userId: UserId, query: ProductQuery): Result {
        val ids = repository.searchForIds(query)
            .map { ProductKey.GetProduct(it) }
        var productInfos = productCacheTemplate.findAll(ids)

        if (productInfos.size != ids.size) {
            val signals = repository.search(query)
            val brands = brandRepository.findByIdIn(signals.map { it.product.brandId })
                .associateBy { it.id }
            productInfos = signals.map { factory.generateInfo(it, brands[it.product.brandId]) }
        }
        val productIds = likeRepository.findLikedProductIds(userId, productIds = productInfos.map { it.id })
        val results = factory.generateResults(
            infos = productInfos,
            userLikedProductIds = productIds,
        ) z
        productCacheTemplate.saveAll(results.map { it.info })
        return Result(products = results)
    }

    @Transactional(readOnly = true)
    fun getDetail(userId: UserId, productId: Long): Result.ProductResult {
        val productInfo = productCacheTemplate.get(ProductKey.GetProduct(productId)) ?: productInfo(productId)
        val likes = likeRepository.findLikedProductIds(userId, listOf(productInfo.id))
        return factory.generateResult(
            productInfo,
            likes,
        )
    }

    private fun productInfo(
        productId: Long,
    ): ProductInfo {
        val productWithSignal = productSignalRepository.findByIdOrNull(productId)
            ?: throw EntityNotFoundException("$productId 는 존재하지 않는 상품입니다.")
        val brand = brandRepository.findByIdOrNull(productWithSignal.product.brandId)
            ?: throw EntityNotFoundException("${productWithSignal.product.brandId}는 존재하지 않는 브랜드입니다.")
        return productCacheTemplate.save(factory.generateInfo(productWithSignal, brand))
    }

    data class Result(val products: List<ProductResult>) {
        data class ProductResult(val info: ProductInfo, val isLiked: Boolean)
    }
}
