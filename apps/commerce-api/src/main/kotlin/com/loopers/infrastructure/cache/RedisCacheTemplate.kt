package com.loopers.infrastructure.cache

// @Repository
// class RedisCacheTemplate<T>(redisTemplate: RedisTemplate<String, String>, private val objectMapper: ObjectMapper) :
//    CacheTemplate<T> {
//    private val opForValue = redisTemplate.opsForValue()
//    override fun cacheAside(key: CacheKey<T>, data: T) {
//        val value = objectMapper.writeValueAsString(data)
//        opForValue.set(key.key, value, key.ttl)
//    }
//
//    override fun read(key: CacheKey<T>, type: TypeReference<T>): T? {
//        val value = opForValue.get(key.key) ?: return null
//        return objectMapper.readValue(value, type)
//    }
// }
//
// inline fun <reified T> RedisCacheTemplate<T>.read(key: CacheKey<T>): T? = read(key, object : TypeReference<T>() {})
