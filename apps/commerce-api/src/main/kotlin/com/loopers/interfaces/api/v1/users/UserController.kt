package com.loopers.interfaces.api.v1.users

import com.loopers.application.user.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(private val userService: UserService) {
    @PostMapping
    fun create(@RequestBody body: UserRequest.Create): UserResponse.Create = userService.create(body.toCommand()).let {
        UserResponse.Create.fromUser(it)
    }
}
