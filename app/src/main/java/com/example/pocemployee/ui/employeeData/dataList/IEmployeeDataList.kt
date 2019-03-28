package com.example.pocemployee.ui.employeeData.dataList

import androidx.lifecycle.MutableLiveData
import com.example.pocemployee.repo.employeeData.EDataSource
import com.example.pocemployee.repo.employeeData.model.EmployeeApiResponse
import com.github.mikephil.charting.data.BarData

interface IEmployeeDataList{

    interface View{
        fun updateView(employeeRecords: MutableList<EmployeeApiResponse>?)
        fun updateChart(employeeChartData: Pair<BarData, String>?)
    }

    interface ViewModel{
        val employeeDataChangeNotifier : MutableLiveData<MutableList<EmployeeApiResponse>>
        var dataSource : EDataSource?
        fun getDataFromApi()
        fun getDataFromDB()
        fun refreshDB()
        fun deleteEmployeeEntry(id: String)
    }
}