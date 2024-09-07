package com.example.demo.model

import jakarta.persistence.*

interface IEquipmentAssigment {
    var id: Long
    var equipment: Equipment
    var user: User
    var description: String?
    var localization: String?
    var assigmentDate: String?
}

@Entity
@Table(name = "equipment_assigments")
class EquipmentAssigment: IEquipmentAssigment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long = 0L
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    override var equipment: Equipment
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    override var user: User
    override var description: String? = null
    override var localization: String?
    override var assigmentDate: String?

    constructor(equipment: Equipment, user: User, description: String?, localization: String?, assigmentDate: String?) {
        this.user = user
        this.description = description
        this.localization = localization
        this.assigmentDate = assigmentDate
        this.equipment = equipment
    }
}

data class EquipmentAssignmentDto(
    var id: Long,
    var localization: String?,
    var assignmentDate: String?,
    var email: String,
    var phoneNumber: String?,
    var role: UserRole,
    var username: String,
    var equipmentId: Long
)

data class EquipmentDetailsDto(
    var assignment: EquipmentAssignmentDto?,
    var equipment: Equipment?
)
