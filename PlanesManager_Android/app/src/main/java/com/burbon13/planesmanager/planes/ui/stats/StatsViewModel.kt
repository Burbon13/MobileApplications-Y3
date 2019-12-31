package com.burbon13.planesmanager.planes.ui.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burbon13.planesmanager.planes.data.remote.PlaneDataSource
import com.burbon13.planesmanager.planes.data.PlaneRepository
import com.burbon13.planesmanager.core.Result
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class StatsViewModel : ViewModel() {
    private val planeRepository = PlaneRepository(PlaneDataSource())

    private val _brandsCountMap = MutableLiveData<Result<ArrayList<PieEntry>>>()
    val brandCountMap: LiveData<Result<ArrayList<PieEntry>>>
        get() = _brandsCountMap

    init {
        loadBrandsCount()
    }

    private fun loadBrandsCount() {
        viewModelScope.launch(Dispatchers.IO) {
            val brandsCountResult = planeRepository.getBrandsCount()
            if (brandsCountResult is Result.Success) {
                val brandsCountMap = brandsCountResult.data
                val pieEntryList = ArrayList<PieEntry>()
                for ((brand, count) in brandsCountMap) {
                    pieEntryList.add(PieEntry(count.toFloat(), brand))
                }
                _brandsCountMap.postValue(Result.Success(pieEntryList))
            } else {
                _brandsCountMap.postValue(Result.Error("An error occurred while loading brands"))
            }
        }
    }
}
