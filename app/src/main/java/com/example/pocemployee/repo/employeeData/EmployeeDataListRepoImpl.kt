package com.example.pocemployee.repo.employeeData

import android.util.Log
import com.example.pocemployee.repo.employeeData.database.DBEmployee
import com.example.pocemployee.repo.employeeData.database.DBEmployeeEntity
import com.example.pocemployee.repo.employeeData.model.EmployeeApiResponse
import com.example.pocemployee.ui.employeeData.dataList.EmployeeDataListViewModel
import retrofit2.Retrofit

/**
 * The EmployeeDataListRepoImpl class is the repository class for the EmployeeDataListViewModel used to
 * fetch data from the DB or through an API call.
 */
class EmployeeDataListRepoImpl(private var apiClient : Retrofit, private var db : DBEmployee ): EmployeeDataListRepo {

    private val  TAG = EmployeeDataListRepoImpl::class.java.simpleName

    /**
     * getDBData method is used to fetch all the enrties in the DB
     * It returns null if the DB is empty, else returns a MutableList of EmployeeApiResponse objects containing the
     * employee records fetched from the DB.
     */
    override fun getDBData(): MutableList<EmployeeApiResponse>?
    {
        val employeeRecords: List<EmployeeApiResponse>?
        if(db.employeeDao().getAll().isEmpty())
        {
            return null
        }
        else {
            employeeRecords = mutableListOf()
            val employeeRecordsFromDB :List<DBEmployeeEntity> = db.employeeDao().getAll()
            Log.d(TAG, employeeRecordsFromDB[0].toString())

            for((index,employeeRecord) in employeeRecordsFromDB.withIndex())
            {
                val employeeApiResponse = EmployeeApiResponse(
                    employeeRecord.id, employeeRecord.employeeName, employeeRecord.employeeSalary,
                    employeeRecord.employeeAge, employeeRecord.profileImage
                )
                employeeRecords.add(employeeApiResponse)

                if(index < 10)
                Log.d(TAG, "got data from DB: entry index: $index "+ employeeRecords.get(index).toString())
            }

            Log.d(TAG, "got data from DB: entry index 0: "+ employeeRecords[0].toString())
            return employeeRecords

        }

    }

    /**
     * deleteDBEntry method is used to a single entry from DB with the corresponding "id" passed as a parameter
     *
     * @param id
     * This parameter is the id of the employee whose entry is to be deleted from the DB.
     */
    override fun deleteDBEntry(id: String)
    {
        val entryToDelete = db.employeeDao().getById(id)
        Log.d(TAG, "entry to delete: $entryToDelete")
        val dbResponse = db.employeeDao().deleteById(id)
        val entry = db.employeeDao().getById(id)
        Log.d(TAG,"deleting id: $id --> $dbResponse")
        Log.d(TAG, "entry not deleted : $entry")
    }


    /**
     * deleteAllEntries method is used to delete all the entries of the DB
     */
    override fun deleteAllEntries()
    {
        db.employeeDao().deleteAll()
    }


    /**
     * storeDataToDB method makes a List of objects of the DBEmployeeEntity type from the EmployeeApiResponse List
     * and saves the list to the DB.
     *
     * @param employeeRecords
     * This parameter is a MutableList of EmployeeApiResponse objects which is fetched through the API call.
     */
    override fun storeDataToDB(employeeRecords:MutableList<EmployeeApiResponse>) {
        var index = db.employeeDao().getAll().size
        for (employeeRecord in employeeRecords) {

            val dbEmployeeEntity = DBEmployeeEntity(index, employeeRecord.id, employeeRecord.employeeName,
                employeeRecord.employeeSalary, employeeRecord.employeeAge, employeeRecord.profileImage)

            index++

            try{
                db.employeeDao().insertAll(dbEmployeeEntity)
            }
            catch (exception: Exception)
            {
                Log.d(TAG,"exception in storing data: $exception.toString()")

            }

        }
    }


    /**
     * getServerData method uses the retrofit client object to fetch the employee data through the API.
     *
     * @param employeeDataListViewModel
     * This parameter is a reference of the EmployeeDataListViewModel object used to call the callback functions
     * of retrofit.
     */
    override fun getServerData(employeeDataListViewModel: EmployeeDataListViewModel)
    {
        val employeeApiEndpoint = apiClient.create(EmployeeApiEndpoint::class.java)
        val call = employeeApiEndpoint.getEmployeeData()
        call.enqueue(employeeDataListViewModel)
    }


}