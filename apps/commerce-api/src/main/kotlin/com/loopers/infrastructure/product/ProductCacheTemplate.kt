package com.loopers.infrastructure.product

import com.fasterxml.jackson.databind.ObjectMapper
import com.loopers.domain.product.ProductInfo
import com.loopers.domain.product.ProductKey
import com.loopers.domain.shared.CacheKey
import com.loopers.domain.shared.CacheTemplate
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class ProductCacheTemplate(redisTemplate: RedisTemplate<String, String>, private val objectMapper: ObjectMapper) :
    CacheTemplate<ProductInfo> {
    private val op = redisTemplate.opsForValue()
    override fun get(key: CacheKey): ProductInfo? {
        val result = op.get(key.key) ?: return null
        return kotlin.runCatching {
            objectMapper.readValue(result, ProductInfo::class.java)
        }.getOrNull()
    }

    override fun saveAll(values: List<ProductInfo>): List<ProductInfo> {
        op.multiSet(
            values.associateByTo(
                mutableMapOf(),
                {
                    ProductKey.GetProduct(it.id).key
                },
                { objectMapper.writeValueAsString(it) },
            ),
        )
        return values
    }

    override fun findAll(keys: Collection<CacheKey>): List<ProductInfo> {
        val result = op.multiGet(keys.map { it.key })
        return result
            ?.filterNotNull()?.map { objectMapper.readValue(it, ProductInfo::class.java) } ?: listOf()
    }

    override fun save(value: ProductInfo): ProductInfo {
        val key = ProductKey.GetProduct(value.id)
        op.set(key.key, objectMapper.writeValueAsString(value), key.ttl)
        return value
    }
}
