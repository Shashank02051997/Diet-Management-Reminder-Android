package com.shashank.dietmanagementreminder.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newcambassy.util.ApiException
import com.newcambassy.util.NoInternetException
import com.shashank.dietmanagementreminder.data.model.WeekDietListResponse
import com.shashank.dietmanagementreminder.data.repositories.WeekDietRepository
import com.shashank.dietmanagementreminder.util.Event
import com.shashank.dietmanagementreminder.util.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeekDietViewModel(private val repository: WeekDietRepository) :
    ViewModel() {


    private val _weekDietListLiveData =
        MutableLiveData<Event<State<WeekDietListResponse.WeekDietData>>>()
    val weekDietListLiveData: LiveData<Event<State<WeekDietListResponse.WeekDietData>>>
        get() = _weekDietListLiveData

    private lateinit var weekDietListResponse: WeekDietListResponse

    fun getWeekDietList() {
        _weekDietListLiveData.postValue(Event(State.loading()))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                weekDietListResponse =
                    repository.getWeekDietList()
                withContext(Dispatchers.Main) {
                    _weekDietListLiveData.postValue(
                        Event(
                            State.success(
                                weekDietListResponse.weekDietData
                            )
                        )
                    )
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _weekDietListLiveData.postValue(Event(State.error(e.message ?: "")))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _weekDietListLiveData.postValue(Event(State.error(e.message ?: "")))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _weekDietListLiveData.postValue(
                        Event(
                            State.error(
                                e.message ?: ""
                            )
                        )
                    )
                }
            }
        }
    }


}
