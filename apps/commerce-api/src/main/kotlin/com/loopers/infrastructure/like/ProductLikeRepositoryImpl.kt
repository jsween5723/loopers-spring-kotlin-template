package com.loopers.infrastructure.like

import com.loopers.domain.product.Product
import com.loopers.domain.productlike.ProductLike
import com.loopers.domain.productlike.ProductLikeRepository
import com.loopers.domain.user.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
class ProductLikeRepositoryImpl(private val jpaRepository: ProductLikeJpaRepository) : ProductLikeRepository {
    override fun save(like: ProductLike): ProductLike = jpaRepository.save(like)
    override fun delete(like: ProductLike) = jpaRepository.delete(like)
    override fun findByUserIdAndProductIn(
        userId: UserId,
        products: List<Product>,
    ): List<ProductLike> = jpaRepository.findByUserIdAndProductIdIn(userId, products.map { it.id })

    override fun findByUserIdAndProduct(userId: UserId, product: Product): ProductLike? = jpaRepository.findByUserIdAndProductId(
        userId,
        product.id,
    )
}

@Repository
interface ProductLikeJpaRepository : JpaRepository<ProductLike, Long> {
    fun findByUserIdAndProductId(userId: UserId, productId: Long): ProductLike?
    fun findByUserIdAndProductIdIn(userId: UserId, product: Collection<Long>): List<ProductLike>
}
