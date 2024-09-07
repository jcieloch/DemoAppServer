package com.example.demo.controller

import com.example.demo.model.IUser
import com.example.demo.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController  @Autowired constructor(
    private val userRepository: UserRepository
) {

    @GetMapping("/list")
    fun getUsers(): ResponseEntity<List<IUser>> {
        val users = userRepository.findAll()
        print(users)
        return ResponseEntity.ok(users)
    }

}
