package com.example.pocemployee.ui.employeeData.dataList

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.pocemployee.R
import com.example.pocemployee.repo.employeeData.EDataSource
import com.example.pocemployee.repo.employeeData.EmployeeDataListRepo
import com.example.pocemployee.repo.employeeData.model.EmployeeApiResponse
import com.example.pocemployee.ui.employeeData.adapter.EmployeeDataAdapter
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random


/**
 * EmployeeDataListViewModel is the ViewModel class for the DataListActvity View class.
 * It handles the fetching of employee data from DB or API and setting the LiveData.
 */
class EmployeeDataListViewModel(val app: Application, var employeeDataListRepo: EmployeeDataListRepo) :
    AndroidViewModel(app), IEmployeeDataList.ViewModel, Callback<MutableList<EmployeeApiResponse>>, EmployeeDataAdapter.ItemClickListener  {

    val employeeChartDataNotifier = MutableLiveData<Pair<BarData,String>>()

    private val  TAG = EmployeeDataListViewModel::class.java.simpleName
    override val employeeDataChangeNotifier = MutableLiveData<MutableList<EmployeeApiResponse>>()
    private var employeeRecords : MutableList<EmployeeApiResponse>? = null
    private var storeDataToDBFlag = false
    override var dataSource: EDataSource? = null


    /**
     * getDataFromApi method is called when the dataSource is selected as API.
     * It uses the object of the EmployeeDataListRepoImpl class to fetch the data through the API call.
     */
    override fun getDataFromApi()
    {
        employeeDataListRepo.getServerData(this)
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
                        employeeDataListRepo.storeDataToDB(it)
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
            employeeDataListRepo.deleteAllEntries()
            getDataFromDB()
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
                employeeDataListRepo.deleteDBEntry(id)
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

            employeeRecords = employeeDataListRepo.getDBData()

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

    /**
     * showChart method is called on clicking an employee entry on the list
     * It generates a random health data for 30 days and adds it to the dataset
     * for the Bar chart.
     * The dataset is updated in a live data.
     */
    override fun showChart(employeeId: String,employeeName: String) {

        val entries = ArrayList<BarEntry>()
        val healthColors = ArrayList<Int>()

        healthColors.add(app.resources.getColor(R.color.bad_health))
        healthColors.add(app.resources.getColor(R.color.normal_health))
        healthColors.add(app.resources.getColor(R.color.good_health))

        val dataPoints = 30
        val seed = employeeId.toInt()
        val random = Random(seed)
        val employeeHealthData =listOf((1..dataPoints).toList(), List(dataPoints) { random.nextInt(0, 10) })
        for(index in (0 until employeeHealthData[0].size-1))
        {
            entries.add(BarEntry(employeeHealthData[0][index].toFloat(), employeeHealthData[1][index].toFloat()))
            println("${employeeHealthData[0][index].toFloat()}, ${employeeHealthData[1][index].toFloat()}")

        }

        val barDataSet = MyBarDataSet(entries,"health data")

        barDataSet.colors = healthColors

        val barData = BarData(barDataSet)
        employeeChartDataNotifier.value = Pair(barData,employeeName)
    }


    /**
     * Sub-class of BarDataSet to override the getColor()
     */
    inner class MyBarDataSet(yVals: List<BarEntry>, label: String) : BarDataSet(yVals, label)
    {
        override fun getEntryIndex(e: BarEntry?): Int
        {
            return super.getEntryIndex(e)
        }

        override fun getColor(index: Int): Int {
            return when {
                getEntryForIndex(index).y<= 2
                    // less than or equal to 2:  red
                -> mColors[0]
                getEntryForIndex(index).y <= 6
                    // less than blue
                -> mColors[1]
                else
                    // greater or equal than 100 red
                -> mColors[2]
            }
        }

    }

}
