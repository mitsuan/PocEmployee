package com.example.pocemployee.repo.employeeData.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DBEmployeeEntity(
    @PrimaryKey var uid: Int,
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "employee_name") val employeeName: String,
    @ColumnInfo(name = "employee_salary") val employeeSalary: String,
    @ColumnInfo(name = "employee_age") val employeeAge: String,
    @ColumnInfo(name = "profile_image") val profileImage: String
)