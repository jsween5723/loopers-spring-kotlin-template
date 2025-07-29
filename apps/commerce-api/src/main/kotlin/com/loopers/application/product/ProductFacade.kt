package com.loopers.application.product

import com.loopers.domain.brand.BrandRepository
import com.loopers.domain.product.Product
import com.loopers.domain.product.ProductQuery
import com.loopers.domain.product.ProductSearchRepository
import com.loopers.domain.productlike.ProductLikeRepository
import com.loopers.domain.user.UserId
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.ZonedDateTime

@Component
class ProductFacade(
    private val repository: ProductSearchRepository,
    private val brandRepository: BrandRepository,
    private val likeRepository: ProductLikeRepository,
) {
    private val factory = ProductInfoFactory()

    @Transactional(readOnly = true)
    fun search(userId: UserId, query: ProductQuery): Result {
        val productWithSignal = repository.search(query)
        val brands = brandRepository.findByIdIn(productWithSignal.map { it.product.brandId })
        val likes = likeRepository.findByUserIdAndProductIn(
            userId,
            productWithSignal.map {
                it.product
            },
        )
        return Result(products = factory.generate(productWithSignal = productWithSignal, brands = brands, likes = likes))
    }

    data class Result(val products: List<ProductInfo>) {
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
            val isLiked: Boolean,
        )
    }
}
