package com.loopers.infrastructure.like

import com.loopers.domain.product.Product
import com.loopers.domain.productlike.ProductLike
import com.loopers.domain.productlike.ProductLikeKey
import com.loopers.domain.productlike.ProductLikeRepository
import com.loopers.domain.user.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ZSetOperations.TypedTuple
import org.springframework.stereotype.Repository

@Repository
class ProductLikeRepositoryImpl(private val jpaRepository: ProductLikeJpaRepository, redisTemplate: RedisTemplate<String, Long>) :
    ProductLikeRepository {
    private val op = redisTemplate.opsForZSet()
    private val valOp = redisTemplate.opsForValue()
    override fun existsByUserIdAndProduct(
        userId: UserId,
        product: Product,
    ): Boolean = jpaRepository.existsByUserIdAndProduct(userId, product)

    override fun findLikedProductIds(userId: UserId, productIds: List<Long>): Set<Long> {
        val key = ProductLikeKey.GetProductLikes(userId).key
        val ids = op.range(key, 0, -1)
        if (ids != null) {
            return ids.map { it.toLong() }.toSet()
        }
        val likes = jpaRepository.findByUserIdAndProductIdIn(userId, productIds)
        val idSet = likes.map { it.product.id }.toSet()
        op.add(key, idSet.map { TypedTuple.of(it, it.toDouble()) }.toMutableSet())
        return idSet
    }

    override fun save(like: ProductLike): ProductLike {
        op.add(ProductLikeKey.GetProductLikes(like.userId).key, like.product.id, like.product.id.toDouble())
        return jpaRepository.save(like)
    }
    override fun delete(like: ProductLike) {
        op.remove(ProductLikeKey.GetProductLikes(like.userId).key, like.product.id)
        jpaRepository.delete(like)
    }
}

@Repository
interface ProductLikeJpaRepository : JpaRepository<ProductLike, Long> {
    fun existsByUserIdAndProduct(userId: UserId, product: Product): Boolean
    fun findByUserIdAndProductIdIn(userId: UserId, product: Collection<Long>): List<ProductLike>
}
