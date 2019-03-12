package com.example.pocemployee.ui.employeeData.dataList

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.pocemployee.repo.employeeData.EDataSource
import com.example.pocemployee.repo.employeeData.EmployeeDataListRepo
import com.example.pocemployee.repo.employeeData.EmployeeDataListRepoImpl
import com.example.pocemployee.repo.employeeData.model.EmployeeApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * EmployeeDataListViewModel is the ViewModel class for the DataListActvity View class.
 * It handles the fetching of employee data from DB or API and setting the LiveData.
 */
class EmployeeDataListViewModel(application: Application) : AndroidViewModel(application),
    IEmployeeDataList.ViewModel, Callback<MutableList<EmployeeApiResponse>>  {

    private val  TAG = EmployeeDataListViewModel::class.java.simpleName
    override val employeeDataChangeNotifier = MutableLiveData<MutableList<EmployeeApiResponse>>()
    private var employeeRecords : MutableList<EmployeeApiResponse>? = null
    private var employeeDataListRepo: EmployeeDataListRepo?=null
    private var storeDataToDBFlag = false
    override var dataSource: EDataSource? = null

    init{
        employeeDataListRepo = EmployeeDataListRepoImpl(getApplication())
    }

    /**
     * getDataFromApi method is called when the dataSource is selcted as API.
     * It uses the object of the EmployeeDataListRepoImpl class to fetch the data through the API call.
     */
    override fun getDataFromApi()
    {
        employeeDataListRepo?.getServerData(this)
    }

    /**
     * onResponse method is the callback function of retrofit API call for handling the response.
     * This method checks the storeDataToDBFlag and stores the fetched data to th DB.
     * It finally sets the value of the changeNotifier.
     */
    override fun onResponse(call: Call<MutableList<EmployeeApiResponse>>,
                            response: Response<MutableList<EmployeeApiResponse>>) {
        if (response.body() != null) {

            employeeRecords = response.body()

            if(storeDataToDBFlag){
                employeeRecords?.let {
                    Thread {
                        employeeDataListRepo?.storeDataToDB(it)
                    }.start()
                }
            }

            employeeDataChangeNotifier.value = employeeRecords

        }
    }

    /**
     * onFailure method is the callback function of retrofit API call for handling failure event.
     */
    override fun onFailure(call: Call<MutableList<EmployeeApiResponse>>, t: Throwable) {
        Log.d(TAG,t.toString())
    }

    /**
     * getDataFromDB method is called from the View class (EmployeeDataListActivity)
     * if the dataSource is selected as DB.
     */
    override fun getDataFromDB() {
        GetDBData().execute()
    }

    /**
     * refreshDB method is called if the dataSource is selected as REFRESH_DB.
     * This method deletes all entries from the DB and call the method getDataFromDB
     * which fetches the data through API and saves it to the DB.
     */
    override fun refreshDB() {
        Thread{
            employeeDataListRepo?.deleteAllEntries()
            getDataFromDB()
        }.start()
    }

    /**
     * closeDB method is called by the EmployeeDataListActivity when that activity is destroyed
     * on pressing the back button. This method calls the closeDBConnection method of the EmployeeDataListRepoImpl
     * class to safely close the DB connection and avoid any data loss in the DB.
     */
    override fun closeDB() {
        Thread{
            employeeDataListRepo?.closeDBConnection()
        }.start()
    }

    /**
     * deleteEmployeeEntry method is used to delete an employee record
     * with a given id from the DB, only if the dataSource is DB.
     *
     * @param id
     * This parameter is the id of the employee whose entry
     * is to be deleted from the DB.
     */
    override fun deleteEmployeeEntry(id: String)
    {
        if(dataSource == EDataSource.DB) {
            Thread {
                employeeDataListRepo?.deleteDBEntry(id)
            }.start()
        }
    }


    /**
     * GetDBData is an inner class, sub-class of AsyncTask used to perform DB operation
     * of fetching the data from the DB and setting the changeNotifier value after fetching
     * the data.
     */
    inner class GetDBData: AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void?{

            employeeRecords = employeeDataListRepo?.getDBData()

            return null
        }

        override fun onPostExecute(obj: Void?){

            if(employeeRecords == null) {

                Log.d(TAG, "Database is empty.")
                storeDataToDBFlag = true
                getDataFromApi()
                Log.d(TAG, "Fetched data from API")
            }
            else{
                this@EmployeeDataListViewModel.employeeDataChangeNotifier.value = employeeRecords
            }
        }
    }
}
