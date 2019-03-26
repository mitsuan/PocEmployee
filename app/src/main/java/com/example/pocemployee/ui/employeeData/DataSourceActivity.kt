package com.example.pocemployee.ui.employeeData

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.pocemployee.R
import com.example.pocemployee.repo.employeeData.EDataSource
import com.example.pocemployee.repo.login.AWSLoginInteractor
import com.example.pocemployee.ui.employeeData.dataList.EmployeeDataListActivity
import com.example.pocemployee.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_data_source.*
import org.koin.android.ext.android.inject


/**
 * DataSourceActivity is the activity which provides options to the user to choose the source of data
 * to fetch the employee data, i.e, DB, API or REFRESH_DB
 */
class DataSourceActivity : AppCompatActivity() {


    private val awsLoginInteractor: AWSLoginInteractor by inject()
    private val TAG = DataSourceActivity::class.java.simpleName

    companion object IntentMessage{
        val SOURCE_MESSAGE = "com.example.pocemployee.SOURCE_MESSAGE"
    }

    private val logoutSuccessObserver = Observer<Boolean>{
        logoutSuccess -> if(logoutSuccess){
//        val intent = Intent(Intent.ACTION_MAIN)
//        intent.addCategory(Intent.CATEGORY_HOME)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        startActivity(intent)

        launchLoginActivity()

        finish()
    }
        Log.d(TAG,"inside DataSourceActivity: Observe() ")
    }

    private fun launchLoginActivity() {
        Log.d(TAG,"Launching Login Activity")
        startActivity(Intent(this@DataSourceActivity, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

    }

    /**
     * onCreate method sets the dataSource to the intent based on the button click through OnClickListener
     * and passes the intent for stating the EmployeeDataListActivity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_source)

        awsLoginInteractor.logoutSuccessNotifier.observe(this,logoutSuccessObserver)

        val intent = Intent(this@DataSourceActivity, EmployeeDataListActivity::class.java)

        db_button.setOnClickListener {
            intent.putExtra(
                SOURCE_MESSAGE,
                EDataSource.DB
            )
            startActivity(intent)
        }

        db_refresh_button.setOnClickListener {
            intent.putExtra(
                SOURCE_MESSAGE,
                EDataSource.DB_REFRESH
            )
            startActivity(intent)
        }

        api_button.setOnClickListener {
            intent.putExtra(
                SOURCE_MESSAGE,
                EDataSource.API
            )
            startActivity(intent)
        }

    }


    fun signOut(view: View)
    {
        awsLoginInteractor.auth?.signOut()

    }

}
