package com.loopers.domain.shared

import java.time.Duration

interface CacheKey {
    val key: String
    val ttl: Duration
}
