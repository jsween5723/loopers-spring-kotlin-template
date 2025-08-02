package com.loopers.domain.orderpayment

import com.loopers.domain.BaseEntity
import com.loopers.domain.order.Order
import com.loopers.domain.payment.Payment
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class OrderPayment(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    val order: Order,
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "payment_id")
    val payment: Payment,
) : BaseEntity()
