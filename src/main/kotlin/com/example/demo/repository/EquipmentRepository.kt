package com.example.demo.repository

import com.example.demo.model.Equipment
import com.example.demo.model.EquipmentAssigment
import com.example.demo.model.User
import com.example.demo.model.UserRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*


interface EquipmentRepository : JpaRepository<Equipment, Long> {
    fun findEquipmentById(id: Long): Equipment?

    @Query("""
            select
            ea
            from EquipmentAssigment as ea 
            where ea.equipment.id = :equipmentId
        """
    )
    fun getEquipmentAssignmentBy(equipmentId: Long): EquipmentAssigment?

    @Query("""
            select
            ea
            from Equipment as ea 
            where ea.qrCode = :qr
        """
    )
    fun findEquipmentBy(qr: String): Equipment

    @Query("""
            select
            ea.user
            from EquipmentAssigment as ea 
            where ea.equipment.qrCode = :qr
        """
    )
    fun getEquipmentUserBy(qr: String): User?
}
