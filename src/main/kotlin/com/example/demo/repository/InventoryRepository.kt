package com.example.demo.repository

import com.example.demo.model.EquipmentAssigment
import com.example.demo.model.Inventory
import com.example.demo.model.InventoryItemDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface InventoryRepository : JpaRepository<Inventory, Long> {

//    @Query("""
//            select
//            ea
//            from EquipmentAssigment as ea
//            where ea.equipment.qrCode = :qr
//        """
//    )
//    fun getInventoryItemsForQrCode(qr: String): EquipmentAssigment
//
//    fun getInventroryItemAssignment
}

