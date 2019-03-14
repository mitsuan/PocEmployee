package com.example.pocemployee.ui.employeeData.dataList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pocemployee.R
import com.example.pocemployee.repo.employeeData.EDataSource
import com.example.pocemployee.repo.employeeData.model.EmployeeApiResponse
import com.example.pocemployee.ui.employeeData.DataSourceActivity
import com.example.pocemployee.ui.employeeData.adapter.EmployeeDataAdapter
import kotlinx.android.synthetic.main.activity_data_list.*


/**
 * The EmployeeDataListActivity is the view class. It shows the list of employee names in a recycler view and
 * provides option for deleting a particular entry.
 * It uses the EmployeeDataListViewModel to fetch the data either from the API or the DB.
 */
class EmployeeDataListActivity : AppCompatActivity(), IEmployeeDataList.View, EmployeeDataAdapter.ItemDeleteListener {

    private val TAG = EmployeeDataListActivity::class.java.simpleName
    private var employeeListRecyclerView: RecyclerView? = null
    private var employeeDataAdapter: EmployeeDataAdapter? = null
    private var employeeListProgressBar : ProgressBar?=null

    private val employeeDataListViewModel: IEmployeeDataList.ViewModel by lazy {
        ViewModelProviders.of(this).get(EmployeeDataListViewModel::class.java)
    }

    private val employeeDataChangeObserver =
        Observer<MutableList<EmployeeApiResponse>> {
                employeeRecords -> employeeRecords?.let { updateView(employeeRecords) }
        }

    /**
     * updateView method is called when the Observable LiveData is changed by the
     * ViewModel (EmployeeDataListViewModel).
     * It sets the adapter of the recycler view and sets the visibility of the
     * progress bar to "GONE".
     *
     * @param employeeRecords
     * This parameter is a list of the employee records fetched either from the
     * DB or API call and used to fill the recycler view through its adapter.
     */
    override fun updateView(employeeRecords: MutableList<EmployeeApiResponse>?) {

        Log.d(TAG,"updating view")
        employeeDataAdapter = EmployeeDataAdapter(
            employeeRecords!!, R.layout.layout_employee_item, this
        )

        employeeListProgressBar!!.visibility = View.GONE

        employeeListRecyclerView?.adapter = employeeDataAdapter
    }

    /**
     * The onCreate method initializes the progress bar and the recycler view object and
     * sets the layout manager for the recycler view.
     * It also sets the Observable to the viewModel's changeNotifier.
     * The intent is fetched from the calling activity (DataSourceActivity) and the source
     * for fetching the data is determined i.e DB or API or perform a DB refresh.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_list)

        employeeListProgressBar = employee_list_progress_bar

        employeeListRecyclerView = employee_list_recycler_view
        employeeListRecyclerView?.setHasFixedSize(true)
        val employeeListLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        employeeListRecyclerView?.layoutManager = employeeListLayoutManager

        employeeDataListViewModel.employeeDataChangeNotifier.observe(this, employeeDataChangeObserver)

        val intent : Intent = getIntent()
        val dataSourceMessage = intent.getSerializableExtra(
            DataSourceActivity.SOURCE_MESSAGE
        ) as EDataSource

        if(dataSourceMessage == EDataSource.API)
        {
            employeeDataListViewModel.dataSource = EDataSource.API
            employeeDataListViewModel.getDataFromApi()

        }
        else if(dataSourceMessage == EDataSource.DB)
        {
            employeeDataListViewModel.dataSource = EDataSource.DB
            employeeDataListViewModel.getDataFromDB()
        }
        else if(dataSourceMessage == EDataSource.DB_REFRESH)
        {
            employeeDataListViewModel.dataSource = EDataSource.DB_REFRESH
            employeeDataListViewModel.refreshDB()
        }

    }

    /**
     * The onDestroy method is overridden to close the DB connection when the
     * activity is destroyed after pressing back button
     */
    override fun onDestroy() {
        employeeDataListViewModel.closeDB()
        super.onDestroy()

    }

    /**
     * onItemDeleteListener is an overridden method of the EmployeeDataAdapter.ItemDeleteListener interface.
     * This method checks deletes the employee record entry from the DB only if the dataSource is DB.
     * It uses the id from the EmployeeApiResponse object to delete the specific entry from the DB.
     *
     * @param employeeRecord
     * This parameter is the record which needs to be deleted from the DB.
     */
    override fun onItemDeleteListener(id: String, viewHolder: EmployeeDataAdapter.EmployeeViewHolder) {

        Log.d(TAG, "called delete listener")
        employeeDataListViewModel.deleteEmployeeEntry(id)
        viewHolder.remove()
    }

}
