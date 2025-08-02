package com.loopers.interfaces.api.v1.users

import com.loopers.application.user.UserService
import com.loopers.domain.auth.Authentication
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(private val userService: UserService) {
    @PostMapping
    fun create(
        @Valid @RequestBody request: UserRequest.Create,
    ): UserResponse.Create = userService.create(request.toCommand())
        .let {
            UserResponse.Create.fromUser(it)
        }

    @GetMapping("/me")
    fun getMe(
        authentication: Authentication,
    ): UserResponse.GetMe {
        val user = userService.read(authentication.userId)
                ?: throw NoSuchElementException("${authentication.userId} 식별자를 가진 사용자는 존재하지 않습니다.")
        return UserResponse.GetMe.fromUser(user)
    }
}
