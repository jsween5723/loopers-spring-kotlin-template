package com.loopers.interfaces.api.v1.users

import com.loopers.application.user.UserService
import com.loopers.domain.auth.Authentication
import jakarta.validation.Valid
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(private val userService: UserService) {
    @PostMapping
    fun create(@Valid @RequestBody body: UserRequest.Create): UserResponse.Create = userService.create(body.toCommand()).let {
            UserResponse.Create.fromUser(it)
        }

    @GetMapping("me")
    fun getMe(
        authentication: Authentication,
    ): UserResponse.GetMe? = userService.read(authentication.id)?.let { UserResponse.GetMe.fromUser(it) }
}
