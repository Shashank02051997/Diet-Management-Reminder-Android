package com.shashank.dietmanagementreminder.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import com.shashank.dietmanagementreminder.R
import com.shashank.dietmanagementreminder.databinding.ActivityWeekDietBinding
import com.shashank.dietmanagementreminder.ui.adapters.CustomAdapterWeekDiet
import com.shashank.dietmanagementreminder.ui.viewmodel.WeekDietViewModel
import com.shashank.dietmanagementreminder.ui.viewmodelfactory.WeekDietViewModelFactory
import com.shashank.dietmanagementreminder.util.AppUtils
import com.shashank.dietmanagementreminder.util.EventObserver
import com.shashank.dietmanagementreminder.util.State
import com.shashank.dietmanagementreminder.util.showToast
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.util.*

class WeekDietActivity : AppCompatActivity(), KodeinAware {

    companion object {
        private const val ALL_PERMISSIONS_RESULT = 101
    }

    override val kodein by closestKodein()
    private lateinit var dataBind: ActivityWeekDietBinding
    private lateinit var customAdapterWeekDiet: CustomAdapterWeekDiet
    private val factory: WeekDietViewModelFactory by instance()
    private val viewModel: WeekDietViewModel by lazy {
        ViewModelProvider(this, factory).get(WeekDietViewModel::class.java)
    }
    private var permissionsToRequest: ArrayList<String>? = null
    private val permissions = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBind = DataBindingUtil.setContentView(this, R.layout.activity_week_diet)
        setupUI()
        observeAPICall()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ALL_PERMISSIONS_RESULT -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initializeRecyclerView()
                } else {
                    showToast("Permission Failed!")
                }
            }
        }
    }

    private fun setupUI() {
        permissions.add(Manifest.permission.READ_CALENDAR)
        permissions.add(Manifest.permission.WRITE_CALENDAR)
        permissionsToRequest = findUnAskedPermissions(permissions)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest!!.size > 0) {
                requestPermissions(
                        permissionsToRequest!!.toTypedArray(),
                        ALL_PERMISSIONS_RESULT
                )
            } else {
                initializeRecyclerView()
            }
        }

    }

    private fun observeAPICall() {
        viewModel.weekDietListLiveData.observe(this, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    AppUtils.showProgressBar(this)
                }
                is State.Success -> {
                    customAdapterWeekDiet.setData(getWeekDietPlanDays(), state.data)
                    AppUtils.hideProgressBar()
                }
                is State.Error -> {
                    AppUtils.hideProgressBar()
                    showToast(state.message)
                }
            }
        })
    }

    private fun getWeekDietList() {
        viewModel.getWeekDietList()
    }

    private fun getWeekDietPlanDays() = arrayListOf("Monday", "Wednesday", "Thursday")

    private fun findUnAskedPermissions(wanted: ArrayList<String>): ArrayList<String> {
        val result = ArrayList<String>()
        for (perm in wanted) {
            if (!hasPermission(perm)) {
                result.add(perm)
            }
        }
        return result
    }

    private fun hasPermission(permission: String): Boolean {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return ContextCompat.checkSelfPermission(
                        this,
                        permission
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
        return true
    }

    private fun canMakeSmores(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }

    private fun initializeRecyclerView() {
        customAdapterWeekDiet = CustomAdapterWeekDiet()
        dataBind.recyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = customAdapterWeekDiet
        }
        getWeekDietList()
    }
}