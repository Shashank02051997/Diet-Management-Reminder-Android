package com.shashank.dietmanagementreminder.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shashank.dietmanagementreminder.R
import com.shashank.dietmanagementreminder.data.model.WeekDietListResponse
import com.shashank.dietmanagementreminder.databinding.ListItemWeekDietBinding

class CustomAdapterWeekDiet : RecyclerView.Adapter<CustomAdapterWeekDiet.ViewHolder>() {

    private lateinit var weekDiet: WeekDietListResponse.WeekDietData
    private val weekDietPlanDays = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ListItemWeekDietBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.list_item_week_diet, parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(weekDietPlanDays[position], position)
    }

    override fun getItemCount(): Int = weekDietPlanDays.size

    fun setData(
        newWeekDietPlanDays: ArrayList<String>,
        newWeekDiet: WeekDietListResponse.WeekDietData
    ) {
        weekDietPlanDays.clear()
        weekDietPlanDays.addAll(newWeekDietPlanDays)
        weekDiet = newWeekDiet
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ListItemWeekDietBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindItems(weekDietDay: String, position: Int) {
            binding.apply {
                textDay.text = weekDietDay
                val mLayoutManager = LinearLayoutManager(
                    recyclerViewDayPlan.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                val dayDiet = when (position) {
                    0 -> weekDiet.monday
                    1 -> weekDiet.wednesday
                    else -> weekDiet.thursday
                }
                recyclerViewDayPlan.apply {
                    layoutManager = mLayoutManager
                    itemAnimator = DefaultItemAnimator()
                    adapter = CustomAdapterDayPlan(dayDiet)
                }
            }
        }
    }
}
