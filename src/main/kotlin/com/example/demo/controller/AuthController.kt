package com.example.demo.controller

import com.example.demo.model.*
import com.example.demo.provider.JwtAuthenticationFilter
import com.example.demo.provider.TokenProvider
import com.example.demo.repository.UserRepository
import com.example.demo.repository.UserSessionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

data class LoginRequest(val username: String, val password: String)

@RestController
@RequestMapping("/auth")
class AuthController  @Autowired constructor(
    private val userRepository: UserRepository,
    private val tokenProvider: TokenProvider,
    private val userSessionRepository: UserSessionRepository
) {
    // TODO: refactor
    @PostMapping("/register")
    fun register(@RequestBody user: User): ResponseEntity<UserTokenResponse> {
        val refreshToken = tokenProvider.generateRefreshToken()
        val sessionUUID = tokenProvider.generateSessionUUID()
        val token = tokenProvider.generateToken(user, sessionUUID)
        val savedUser = userRepository.save(user)
        userSessionRepository.save(UserSession(savedUser, refreshToken, sessionUUID))
        return ResponseEntity.ok(UserTokenResponse(savedUser, token, refreshToken))
    }

    @PostMapping("/login")
    fun login(@RequestBody user: LoginRequest): ResponseEntity<UserTokenResponse> {
        val registeredUser = userRepository.findByUsername(user.username)
        println(user.username)

        if(registeredUser != null && registeredUser.password == user.password){
            val refreshToken = tokenProvider.generateRefreshToken()
            val sessionUUID = tokenProvider.generateSessionUUID()
            val token = tokenProvider.generateToken(registeredUser, sessionUUID)
            val session = userSessionRepository.findByUserId(registeredUser.id)
            session?.refreshToken = refreshToken
            session?.sessionID = sessionUUID
            if (session != null) {
                userSessionRepository.save(session)
            }
            return ResponseEntity.ok(UserTokenResponse(registeredUser, token, refreshToken))
        } else {
            throw Exception("Bad username or password")
        }

    }

}
