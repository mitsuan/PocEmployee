package com.example.pocemployee.repo.employeeData

import com.example.pocemployee.repo.employeeData.model.EmployeeApiResponse
import com.example.pocemployee.ui.employeeData.dataList.EmployeeDataListViewModel

interface EmployeeDataListRepo {
    fun getDBData(): MutableList<EmployeeApiResponse>?
    fun deleteDBEntry(id: String)
    fun deleteAllEntries()
    fun storeDataToDB(employeeRecords: MutableList<EmployeeApiResponse>)
//    fun closeDBConnection()
    fun getServerData(employeeDataListViewModel: EmployeeDataListViewModel)
}