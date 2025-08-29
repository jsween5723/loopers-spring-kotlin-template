package com.loopers.application.shared

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager

@Component
class Tx(_txAdvice: TxAdvice) {

    init {
        Tx.txAdvice = _txAdvice
    }

    companion object {
        private lateinit var txAdvice: TxAdvice

        fun <T> run(function: () -> T, afterCommit: ((T) -> Unit)? = null): T = txAdvice.run(function)
    }

    @Component
    class TxAdvice {

        @Transactional
        fun <T> run(function: () -> T, afterCommitFunction: ((T) -> Unit)? = null): T {
            val result = function()
            TransactionSynchronizationManager.registerSynchronization(
                object : TransactionSynchronization {
                    override fun afterCommit() {
                        afterCommitFunction?.invoke(result)
                    }
                },
            )
            return result
        }
    }
}
