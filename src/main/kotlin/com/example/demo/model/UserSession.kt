package com.example.demo.model

import jakarta.persistence.*
import java.util.*

interface IUserSession {
    var id: Long
    var sessionID: UUID
    var refreshToken: String
    var user: User
}

interface IAuthToken {
    var accessToken: String
    var refreshToken: String
}

@Entity
@Table(name = "user_session")
class UserSession : IUserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long = 0L
    override var sessionID: UUID = UUID.randomUUID()
    override var refreshToken: String = ""

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(referencedColumnName = "id")
    override var user: User

    constructor(user: User, refreshToken: String, sessionID: UUID) {
        this.user = user
        this.refreshToken = refreshToken
        this.sessionID = sessionID
    }
}

class UserTokenResponse(
    user: User,
    override var accessToken: String,
    override var refreshToken: String,
): IUser, IAuthToken {

    override var id: Long = user.id
    override var username: String = user.username
    override var password: String = user.password
    override var email: String = user.email
    override var phoneNumber: String? = user.phoneNumber
    override var role: UserRole = UserRole.CATALOGUER

}