package com.example.pocemployee.repo.employeeData.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(DBEmployeeEntity::class), version = 1)
abstract class DBEmployee : RoomDatabase() {
    abstract fun employeeDao(): DBEmployeeDao
}