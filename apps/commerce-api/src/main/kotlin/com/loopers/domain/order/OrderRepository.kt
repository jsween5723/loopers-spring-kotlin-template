package com.loopers.domain.order

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long> {
    @Query("select o from orders o join fetch o.orderLines where o.id = :id")
    fun findByIdOrNull(id: Long): Order?
}
