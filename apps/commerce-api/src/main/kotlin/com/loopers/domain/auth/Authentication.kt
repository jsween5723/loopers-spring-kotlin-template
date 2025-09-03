package com.loopers.domain.auth

import java.util.UUID

open class Authentication(val userId: UUID, val roles: Set<Role> = setOf(Role.USER)) {
    fun <T> hasRole(vararg targetRoles: Role, func: ()->T): T {
        targetRoles.forEach {
            if(roles.contains(it)) return func()
        }
        throw IllegalAccessException(" $roles does not have $targetRoles")
    }
}

class AnonymousAuthentication : Authentication(UUID.randomUUID(), setOf(Role.ANONYMOUS))
