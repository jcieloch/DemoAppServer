package com.example.demo.provider

import com.example.demo.model.IUser
import com.example.demo.repository.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*


data class JWTPayload(
    val sessionUUID: UUID,
    val userName: String
) : java.io.Serializable

@Component
class TokenProvider(
    @Value("\${security.jwt.secret-key}") private val secretKey: String,
    @Value("\${security.jwt.expiration-time}") private val expirationTime: Long,
    private val userRepository: UserRepository,
    ) {

    fun generateToken(authentication: IUser, sessionUUID: UUID): String {
        val userDetails = authentication.username
        val claims = Jwts.claims().setSubject(userDetails).setAudience(sessionUUID.toString())
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(SignatureAlgorithm.HS512, secretKey.toByteArray())
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser().setSigningKey(secretKey.toByteArray()).parseClaimsJws(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getUsernameFromToken(token: String): String? {
        return Jwts.parser().setSigningKey(secretKey.toByteArray()).parseClaimsJws(token).body.subject
    }

    fun generateRefreshToken(): String {
        return UUID.randomUUID().toString()
    }

    fun generateSessionUUID(): UUID {
        return UUID.randomUUID()
    }

}

class JwtAuthenticationFilter(private val jwtTokenProvider: TokenProvider) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token = request.getHeader("Authorization")?.removePrefix("Bearer ")
        if (token != null && jwtTokenProvider.validateToken(token)) {
            val username = jwtTokenProvider.getUsernameFromToken(token)
            val authentication = UsernamePasswordAuthenticationToken(username, null, listOf())
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }
}