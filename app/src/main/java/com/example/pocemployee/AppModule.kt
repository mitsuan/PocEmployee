package com.example.pocemployee

import androidx.room.Room
import com.example.pocemployee.repo.employeeData.EmployeeDataListRepo
import com.example.pocemployee.repo.employeeData.EmployeeDataListRepoImpl
import com.example.pocemployee.repo.employeeData.database.DBEmployee
import com.example.pocemployee.repo.login.AWSLoginInteractor
import com.example.pocemployee.ui.employeeData.dataList.EmployeeDataListViewModel
import com.example.pocemployee.ui.login.LoginViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


private const val EMPLOYEE_DB = "EMPLOYEE_DB"
private const val LOGGING_INTERCEPTOR = "LOGGING_INTERCEPTOR"
private const val EMPLOYEE_DATA_API = "EMPLOYEE_DATA_API"

fun getRetrofitClient(okHttpClient: OkHttpClient,url: String):Retrofit
{
    return Retrofit.Builder().client(okHttpClient).baseUrl(url).addConverterFactory(
        MoshiConverterFactory.create()).build()
}


val employeeModule : Module = module{

    viewModel { EmployeeDataListViewModel(get(),get()) }

    single<EmployeeDataListRepo> { EmployeeDataListRepoImpl(get(), get(EMPLOYEE_DB)) }

}

val loginModule: Module = module{

    viewModel { LoginViewModel(get()) }

    single{
        AWSLoginInteractor(get())
    }
}

val dbModule : Module = module{

    single(EMPLOYEE_DB){
        val employeeDB = Room.databaseBuilder(
            get(),
            DBEmployee::class.java, "employee-database"
        ).build()

        employeeDB
    }
}

val remoteModule: Module = module{

    single {
        getRetrofitClient(get(),getProperty(EMPLOYEE_DATA_API))
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get(LOGGING_INTERCEPTOR))
            .build()
    }

    single(LOGGING_INTERCEPTOR) {
        val logger = HttpLoggingInterceptor()

        logger.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        logger as Interceptor
    }
}