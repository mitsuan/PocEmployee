package com.example.pocemployee.repo.employeeData

import com.example.pocemployee.repo.employeeData.model.EmployeeApiResponse
import retrofit2.Call
import retrofit2.http.GET

interface EmployeeApiEndpoint {
    @GET("employees")
    fun getEmployeeData(): Call<MutableList<EmployeeApiResponse>>
}
