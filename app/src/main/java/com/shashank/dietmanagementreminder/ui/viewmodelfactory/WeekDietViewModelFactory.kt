package com.shashank.dietmanagementreminder.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shashank.dietmanagementreminder.data.repositories.WeekDietRepository
import com.shashank.dietmanagementreminder.ui.viewmodel.WeekDietViewModel

@Suppress("UNCHECKED_CAST")
class WeekDietViewModelFactory(
    private val repository: WeekDietRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WeekDietViewModel(repository) as T
    }
}
