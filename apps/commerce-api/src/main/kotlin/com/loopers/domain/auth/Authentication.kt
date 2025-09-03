package com.loopers.domain.auth

import java.util.UUID

open class Authentication(val userId: UUID, val roles: Set<Role> = setOf(Role.USER)) {
    fun hasRole(vararg targetRoles: Role) {
        targetRoles.forEach {
            if(roles.contains(it)) return
        }
        throw IllegalAccessException(" $roles does not have $targetRoles")
    }
}

class AnonymousAuthentication : Authentication(UUID.randomUUID(), setOf(Role.ANONYMOUS))
