package com.example.demo.controller

import com.example.demo.model.*
import com.example.demo.repository.EquipmentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/equipment")
class EquipmentController  @Autowired constructor(
    private val equipmentRepository: EquipmentRepository
) {

    @GetMapping("/list")
    fun getEquipments(): ResponseEntity<List<IEquipment>> {
        return ResponseEntity.ok(equipmentRepository.findAll())
    }

    @PostMapping("/insert")
    fun insert(@RequestBody equipment: Equipment): ResponseEntity<String> {
        equipmentRepository.save(equipment)
        return ResponseEntity.ok("DODANO")
    }

    @GetMapping("/details/{id}")
    fun getEquipmentDetails(@PathVariable("id") id: Long): EquipmentDetailsDto {
        val equipment = equipmentRepository.findEquipmentById(id)
        val equipmentAssigment = equipmentRepository.getEquipmentAssignmentBy(id)
        return EquipmentDetailsDto(
            assignment = if (equipmentAssigment == null) null else EquipmentAssignmentDto(
                id = equipmentAssigment.id,
                equipmentId = equipmentAssigment.id,
                role = equipmentAssigment.user.role,
                email = equipmentAssigment.user.email,
                username = equipmentAssigment.user.username,
                localization = equipmentAssigment.localization,
                assignmentDate = equipmentAssigment.assigmentDate,
                phoneNumber = equipmentAssigment.user.phoneNumber,
            ),
            equipment = equipment
        )
    }
}