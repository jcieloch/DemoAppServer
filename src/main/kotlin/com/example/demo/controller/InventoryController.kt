package com.example.demo.controller

import com.example.demo.model.IEquipment
import com.example.demo.model.InventoryItemDto
import com.example.demo.repository.EquipmentRepository
import com.example.demo.repository.InventoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/inventory")
class InventoryController  @Autowired constructor(
    private val inventoryRepository: InventoryRepository,
    private val equipmentRepository: EquipmentRepository
) {

    @GetMapping("/item/{qr}")
    fun getInventoryItemForQr(@PathVariable qr: String): ResponseEntity<InventoryItemDto> {
        val equipment = equipmentRepository.findEquipmentBy(qr)
        val assignment = equipmentRepository.getEquipmentAssignmentBy(equipment.id)
        val user = equipmentRepository.getEquipmentUserBy(qr)

        return ResponseEntity.ok(InventoryItemDto(
            description = equipment.description,
            localization = assignment?.localization,
            id = equipment.id,
            qr = equipment.qrCode,
            email = user?.email,
            username = user?.username,
            phoneNumber = user?.phoneNumber,
            equipmentStatus = equipment.status.toString()
        ))
    }

    @PostMapping("/send")
    fun sendInventory(@RequestBody inventoryItems: List<InventoryItemDto>): ResponseEntity<String> {
        //TODO saving inventories

        val equipments = equipmentRepository.findAll()
        val fromDataBase = equipments.map {
            val equipment = equipmentRepository.findEquipmentBy(it.qrCode)
            val assignment = equipmentRepository.getEquipmentAssignmentBy(equipment.id)
            val user = equipmentRepository.getEquipmentUserBy(it.qrCode)

            return@map InventoryItemDto(
                description = equipment.description,
                localization = assignment?.localization,
                id = equipment.id,
                qr = equipment.qrCode,
                email = user?.email,
                username = user?.username,
                phoneNumber = user?.phoneNumber,
                equipmentStatus = equipment.status.toString()
            )
        }

        val report = generateReport(inventoryItems, fromDataBase)

        return ResponseEntity.ok(report)
    }

}


fun generateReport(list1: List<InventoryItemDto>, list2: List<InventoryItemDto>): String {
    val report = StringBuilder()

    val map1 = list1.associateBy { it.qr }
    val map2 = list2.associateBy { it.qr }

    val allIds = (map1.keys + map2.keys).distinct()

    allIds.forEach { id ->
        val item1 = map1[id]
        val item2 = map2[id]

        if (item1 != item2) {
            report.append("Differences for QR Code $id:\n")
            if (item1 == null) {
                report.append("In database but not in inventory:\n$item2\n")
            } else if (item2 == null) {
                report.append("In inventory but not in database:\n$item1\n")
            } else {
                compareProperties(item1, item2, report)
            }
            report.append("\n")
        }
    }

    return report.toString()
}

private fun compareProperties(item1: InventoryItemDto, item2: InventoryItemDto, report: StringBuilder) {
    if (item1.equipmentStatus != item2.equipmentStatus) {
        report.append("  - equipmentStatus: ${item1.equipmentStatus} vs ${item2.equipmentStatus}\n")
    }
    if (item1.qr != item2.qr) {
        report.append("  - qr: ${item1.qr} vs ${item2.qr}\n")
    }
    if (item1.username != item2.username) {
        report.append("  - username: ${item1.username} vs ${item2.username}\n")
    }
    if (item1.email != item2.email) {
        report.append("  - email: ${item1.email} vs ${item2.email}\n")
    }
    if (item1.phoneNumber != item2.phoneNumber) {
        report.append("  - phoneNumber: ${item1.phoneNumber} vs ${item2.phoneNumber}\n")
    }
    if (item1.description != item2.description) {
        report.append("  - description: ${item1.description} vs ${item2.description}\n")
    }
    if (item1.localization != item2.localization) {
        report.append("  - localization: ${item1.localization} vs ${item2.localization}\n")
    }
}