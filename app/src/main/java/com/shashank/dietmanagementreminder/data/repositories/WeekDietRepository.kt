package com.shashank.dietmanagementreminder.data.repositories

import com.newcambassy.data.network.SafeApiRequest
import com.shashank.dietmanagementreminder.data.model.WeekDietListResponse
import com.shashank.dietmanagementreminder.data.network.ApiInterface

class WeekDietRepository(
    private val api: ApiInterface
) : SafeApiRequest() {

    suspend fun getWeekDietList(): WeekDietListResponse = apiRequest {
        api.getWeekDietListData()
    }


}
