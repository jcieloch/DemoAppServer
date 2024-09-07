package com.example.demo.repository

import com.example.demo.model.User
import com.example.demo.model.UserSession
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserSessionRepository : JpaRepository<UserSession, UUID> {
    fun findByUserId(userId: Long): UserSession?
}