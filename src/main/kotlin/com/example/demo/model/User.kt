package com.example.demo.model

import jakarta.persistence.*

interface IUser {
    var id: Long
    var username: String
    var password: String
    var email: String
    var phoneNumber: String?
    var role: UserRole
}

@Entity
@Table(name = "users")
class User: IUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long = 0L

    override var username: String = ""
    override var password: String = ""
    override var email: String = ""
    override var phoneNumber: String? = null
    @Enumerated(EnumType.STRING)
    override var role: UserRole = UserRole.CATALOGUER

}

enum class UserRole { CATALOGUER, EMPLOYEE }

