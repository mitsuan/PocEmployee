package com.example.pocemployee

import android.app.Application
import org.koin.android.ext.android.startKoin

class ApplicationWrapper: Application(){

    override fun onCreate()
    {
        super.onCreate()
        startKoin(this,listOf(loginModule, employeeModule, dbModule, remoteModule),loadPropertiesFromFile = true)
//        setProperty("EMPLOYEE_DATA_API","http://dummy.restapiexample.com/api/v1/")

    }
}