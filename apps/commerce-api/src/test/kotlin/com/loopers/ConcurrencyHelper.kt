package com.loopers

import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.Future

class ConcurrencyHelper

fun <T> concurrency(acts: List<() -> T>): List<T> {
    val newFixedThreadPool = Executors.newFixedThreadPool(acts.size)
    val countDownLatch = CountDownLatch(acts.size)
    val results: List<Future<T>> = acts.map {
        newFixedThreadPool.submit<T> {
             try {
                return@submit it()
            } finally {
                countDownLatch.countDown()
            }
        }
    }
    countDownLatch.await()
    return results.map { it.get() }
}
