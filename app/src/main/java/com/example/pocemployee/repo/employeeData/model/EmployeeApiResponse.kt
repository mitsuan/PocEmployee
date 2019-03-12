package com.example.pocemployee.repo.employeeData.model

import com.squareup.moshi.Json

data class EmployeeApiResponse(@field:Json(name = "id") val id: String,
                               @field:Json(name = "employee_name") val employeeName: String,
                               @field:Json(name = "employee_salary") val employeeSalary: String,
                               @field:Json(name = "employee_age") val employeeAge: String,
                               @field:Json(name = "profile_image") val profileImage: String)