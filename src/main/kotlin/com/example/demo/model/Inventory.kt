package com.example.demo.model

import jakarta.persistence.*

enum class InventoryStatus {
    IN_PROGRESS, FINISHED
}

interface IInventory {
    var id: Long
    var date: String
    var status: InventoryStatus
    var cataloguer: User
    var items: List<InventoryItem>
}

interface IInventoryItem {
    var id: Long
    var equipmentStatus: EquipmentStatus?
    var qr: String
    var user: User?
    var description: String?
    var localization: String?
}

@Entity
@Table(name = "inventory")
class Inventory: IInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long = 0L
    override var date: String
    @Enumerated(EnumType.STRING)
    override var status: InventoryStatus
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    override var cataloguer: User
    @OneToMany(mappedBy = "inventory", cascade = [CascadeType.ALL], orphanRemoval = true)
    override var items: List<InventoryItem>

    constructor(date: String, equipment: Equipment, status: InventoryStatus, cataloguer: User, items: List<InventoryItem>) {
        this.date = date
        this.status = status
        this.cataloguer = cataloguer
        this.items = items
    }
}

@Entity
@Table(name = "inventory_items")
class InventoryItem: IInventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long = 0L
//    @ManyToOne
//    @JoinColumn(referencedColumnName = "id")
//    override var equipment: Equipment
    override var equipmentStatus: EquipmentStatus?
    override var qr: String
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    override var user: User?
    override var description: String?
    override var localization: String?
    @ManyToOne
    @JoinColumn(name="inventory_id")
    var inventory: Inventory? = null

    constructor(equipmentStatus: EquipmentStatus, qr: String, user: User?, description: String?, localization: String?, inventory: Inventory) {
        this.equipmentStatus = equipmentStatus
        this.qr = qr
        this.user = user
        this.description = description
        this.localization = localization
        this.inventory = inventory
    }
}

data class InventoryItemDto(
    var id: Long,
//    var equipmentStatus: EquipmentStatus?,
    var equipmentStatus: String?,
    var qr: String,
    val username: String?,
    val email: String?,
    val phoneNumber: String?,
    var description: String?,
    var localization: String?,
)


