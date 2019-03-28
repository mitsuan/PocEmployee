package com.example.pocemployee.ui.employeeData.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.example.pocemployee.BR
import com.example.pocemployee.R
import com.example.pocemployee.databinding.LayoutEmployeeItemBinding
import com.example.pocemployee.repo.employeeData.model.EmployeeApiResponse
import kotlinx.android.synthetic.main.layout_employee_item.view.*

class EmployeeDataAdapter(private val employeeData: MutableList<EmployeeApiResponse>, private val rowLayout: Int,
                          val deleteListener: ItemDeleteListener, val clickListener: ItemClickListener) : RecyclerSwipeAdapter<EmployeeDataAdapter.EmployeeViewHolder>() {

    private val TAG = EmployeeDataAdapter::class.java.simpleName

    override fun getSwipeLayoutResourceId(position: Int): Int {
     return R.id.employee_row_swipe_layout
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): EmployeeViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
        val binding: LayoutEmployeeItemBinding = DataBindingUtil.inflate(view, rowLayout, viewGroup, false)
        return EmployeeViewHolder(binding)
    }

    override fun onBindViewHolder(employeeViewHolder: EmployeeViewHolder, position: Int) {

        employeeViewHolder.bind(position)
        Log.d(TAG, "Employee Name: " + employeeData[position].employeeName)
    }

    override fun getItemCount(): Int {
        return employeeData.size
    }

    inner class EmployeeViewHolder(val binding: LayoutEmployeeItemBinding) : RecyclerView.ViewHolder(binding.root) {

        var employeeSwipeLayout: SwipeLayout = itemView.employee_row_swipe_layout

        fun bind(position: Int) {
            employeeSwipeLayout.showMode = SwipeLayout.ShowMode.LayDown
            employeeSwipeLayout.addDrag(SwipeLayout.DragEdge.Right,employeeSwipeLayout.bottom_wrapper)
            employeeSwipeLayout.addDrag(SwipeLayout.DragEdge.Left, null)

            mItemManger.bindView(itemView, position)

            with(binding){
                binding.setVariable(BR.employeeId,employeeData[position].id)
                binding.setVariable(BR.employeeName,employeeData[position].employeeName)
                binding.setVariable(BR.deleteListener,this@EmployeeDataAdapter.deleteListener)
                binding.setVariable(BR.clickListener,this@EmployeeDataAdapter.clickListener)
                binding.setVariable(BR.viewHolder, this@EmployeeViewHolder)
                executePendingBindings()
            }
        }

        /**
         * This method is used to remove an employee record from the employeeData List
         * and notify the adapter of the same to reflect in the UI.
         * This method is called from the onItemDeleteListener implemented in the
         * View class (EmployeeDataListActivity)
         */
        fun remove()
        {   employeeData.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
        }
    }

    interface ItemClickListener{
        fun showChart(employeeId: String, employeeName: String)
    }

    interface ItemDeleteListener{
        fun onItemDeleteListener(id: String, viewHolder: EmployeeViewHolder)
    }
}
