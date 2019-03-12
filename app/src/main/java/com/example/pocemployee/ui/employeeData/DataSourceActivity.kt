package com.example.pocemployee.ui.employeeData

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pocemployee.R
import com.example.pocemployee.repo.employeeData.EDataSource
import com.example.pocemployee.ui.employeeData.dataList.EmployeeDataListActivity
import kotlinx.android.synthetic.main.activity_data_source.*


/**
 * DataSourceActivity is the activity which provides options to the user to choose the source of data
 * to fetch the employee data, i.e, DB, API or REFRESH_DB
 */
class DataSourceActivity : AppCompatActivity() {

    companion object IntentMessage{
        val SOURCE_MESSAGE = "com.example.pocemployee.SOURCE_MESSAGE"
    }

    /**
     * onCreate method sets the dataSource to the intent based on the button click through OnClickListener
     * and passes the intent for stating the EmployeeDataListActivity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_source)

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
}
