package com.example.pocemployee.ui.employeeData.dataList

import androidx.lifecycle.MutableLiveData
import com.example.pocemployee.repo.employeeData.EDataSource
import com.example.pocemployee.repo.employeeData.model.EmployeeApiResponse

interface IEmployeeDataList{

    interface View{
        fun updateView(employeeRecords: MutableList<EmployeeApiResponse>?)
    }

    interface ViewModel{
        val employeeDataChangeNotifier : MutableLiveData<MutableList<EmployeeApiResponse>>
        var dataSource : EDataSource?
        fun getDataFromApi()
        fun getDataFromDB()
        fun refreshDB()
//        fun closeDB()
        fun deleteEmployeeEntry(id: String)
    }
}