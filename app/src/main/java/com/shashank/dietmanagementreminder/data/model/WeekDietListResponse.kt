package com.shashank.dietmanagementreminder.data.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class WeekDietListResponse(
    @SerializedName("diet_duration")
    val dietDuration: Int,
    @SerializedName("week_diet_data")
    val weekDietData: WeekDietData
) {
    @Keep
    data class WeekDietData(
        @SerializedName("monday")
        val monday: List<Monday>,
        @SerializedName("thursday")
        val thursday: List<Thursday>,
        @SerializedName("wednesday")
        val wednesday: List<Wednesday>
    ) {
        @Keep
        data class Monday(
            @SerializedName("food")
            val food: String,
            @SerializedName("meal_time")
            val mealTime: String
        )

        @Keep
        data class Thursday(
            @SerializedName("food")
            val food: String,
            @SerializedName("meal_time")
            val mealTime: String
        )

        @Keep
        data class Wednesday(
            @SerializedName("food")
            val food: String,
            @SerializedName("meal_time")
            val mealTime: String
        )
    }
}