package com.example.demo.model

import jakarta.persistence.*

enum class EquipmentStatus {
    IN_USE, LOST, NOT_IN_USE
}

interface IEquipment {
    var id: Long
    var name: String
    var qrCode: String
    var description: String?
    var producer: String?
    var status: EquipmentStatus?
}

@Entity
@Table(name = "equipments")
class Equipment: IEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long = 0L

    override var name: String = ""
    override var qrCode: String = ""
    override var description: String? = null
    override var producer: String? = null
    @Enumerated(EnumType.STRING)
    override var status: EquipmentStatus? = null
}
