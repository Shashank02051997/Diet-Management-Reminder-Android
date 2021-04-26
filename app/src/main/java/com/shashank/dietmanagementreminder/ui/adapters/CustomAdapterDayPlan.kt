package com.shashank.dietmanagementreminder.ui.adapters

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.shashank.dietmanagementreminder.R
import com.shashank.dietmanagementreminder.data.model.WeekDietListResponse
import com.shashank.dietmanagementreminder.databinding.ListItemDayPlanBinding
import java.util.*


class CustomAdapterDayPlan(private val imagesList: List<Any>) :
    RecyclerView.Adapter<CustomAdapterDayPlan.ViewHolder>() {

    companion object {
        private const val MO = "MO"
        private const val WE = "WE"
        private const val TH = "TH"
        private const val FREQ = "WEEKLY"
        private const val COUNT = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ListItemDayPlanBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.list_item_day_plan, parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(imagesList[position])
    }

    override fun getItemCount(): Int = imagesList.size

    inner class ViewHolder(private val binding: ListItemDayPlanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindItems(item: Any) {

            val calendar = Calendar.getInstance()
            val dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK)

            binding.apply {
                when (item) {
                    is WeekDietListResponse.WeekDietData.Monday -> {
                        imageFood.setImageResource(R.drawable.food_img_1)
                        textFoodName.text = item.food
                        textMealTime.text = item.mealTime
                        if (dayOfTheWeek != 2)
                            calendar.add(
                                Calendar.DAY_OF_WEEK,
                                Calendar.MONDAY - dayOfTheWeek
                            )
                        val year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH)
                        val day = calendar.get(Calendar.DAY_OF_MONTH)
                        addEventToCalendar(
                            textMealTime.context,
                            year,
                            month,
                            day,
                            MO,
                            item.food,
                            item.mealTime
                        )
                    }
                    is WeekDietListResponse.WeekDietData.Wednesday -> {
                        imageFood.setImageResource(R.drawable.food_img_2)
                        textFoodName.text = item.food
                        textMealTime.text = item.mealTime
                        if (dayOfTheWeek != 4)
                            calendar.add(
                                Calendar.DAY_OF_WEEK,
                                Calendar.WEDNESDAY - dayOfTheWeek
                            )
                        val year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH)
                        val day = calendar.get(Calendar.DAY_OF_MONTH)
                        addEventToCalendar(
                            textMealTime.context,
                            year,
                            month,
                            day,
                            WE,
                            item.food,
                            item.mealTime
                        )
                    }
                    is WeekDietListResponse.WeekDietData.Thursday -> {
                        imageFood.setImageResource(R.drawable.food_img_3)
                        textFoodName.text = item.food
                        textMealTime.text = item.mealTime
                        if (dayOfTheWeek != 5)
                            calendar.add(
                                Calendar.DAY_OF_WEEK,
                                Calendar.THURSDAY - dayOfTheWeek
                            )
                        val year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH)
                        val day = calendar.get(Calendar.DAY_OF_MONTH)
                        addEventToCalendar(
                            textMealTime.context,
                            year,
                            month,
                            day,
                            TH,
                            item.food,
                            item.mealTime
                        )
                    }
                }

            }
        }

        private fun addEventToCalendar(
            context: Context,
            year: Int,
            month: Int,
            day: Int,
            byDay: String, foodName: String, mealTime: String
        ) {
            val calID: Long = 3
            val startMillis: Long = Calendar.getInstance().run {
                set(
                    year,
                    month,
                    day,
                    mealTime.substringBefore(":").toInt(),
                    mealTime.substringAfter(":").toInt()
                )
                timeInMillis
            }
            // Added 20 min
            val endMillis: Long = startMillis + 20 * 60 * 1000
            val values = ContentValues().apply {
                put(CalendarContract.Events.DTSTART, startMillis)
                put(CalendarContract.Events.DTEND, endMillis)
                put(CalendarContract.Events.TITLE, "Diet Plan - Eat this $foodName")
                put(CalendarContract.Events.DESCRIPTION, "Eat this $foodName")
                put(CalendarContract.Events.CALENDAR_ID, calID)
                put(CalendarContract.Events.RRULE, "FREQ=$FREQ;BYDAY=$byDay;COUNT=$COUNT")
                put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
            }
            val uri: Uri? =
                context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)

            // get the event ID that is the last element in the Uri
            val eventID: Long = uri?.lastPathSegment?.toLong() ?: 0
            Log.i("Info", "Event Id = $eventID")
        }


    }

}