package com.example.pocemployee.repo.employeeData.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DBEmployeeDao {
    @Query("SELECT * FROM dbEmployeeEntity")
    fun getAll(): List<DBEmployeeEntity>

    @Query("SELECT * FROM dbEmployeeEntity WHERE id IN (:id)")
    fun getById(id: String): DBEmployeeEntity

    @Insert
    fun insertAll(vararg users: DBEmployeeEntity)

    @Query("DELETE FROM dbEmployeeEntity WHERE id IN (:id)")
    fun deleteById(id: String): Int

    @Query("DELETE FROM dbEmployeeEntity")
    fun deleteAll()

    @Delete
    fun delete(user: DBEmployeeEntity)
}