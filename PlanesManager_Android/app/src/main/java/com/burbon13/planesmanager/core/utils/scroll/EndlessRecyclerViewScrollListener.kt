package com.burbon13.planesmanager.core.utils.scroll

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.burbon13.planesmanager.core.TAG


abstract class EndlessRecyclerViewScrollListener(
    private val viewModel: EndlessScrollViewModel,
    private val mLayoutManager: LinearLayoutManager
) :
    RecyclerView.OnScrollListener() {
    private var state = viewModel.state.value ?: EndlessScrollState()

    fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        Log.v(TAG, "getLastVisibleItem() -> $maxSize")
        return maxSize
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        Log.v(TAG, "onScrolled()")
        val totalItemCount = mLayoutManager.itemCount
        val lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition()

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < state.previousTotalItemCount) {
            state = if (totalItemCount == 0) {
                Log.d(TAG, "List is invalidated, loading set to TRUE")
                state.copy(
                    currentPage = state.startingPageIndex,
                    previousTotalItemCount = totalItemCount,
                    loading = true
                )
            } else {
                state.copy(
                    currentPage = state.startingPageIndex,
                    previousTotalItemCount = totalItemCount
                )
            }
        }

        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (state.loading && totalItemCount > state.previousTotalItemCount) {
            Log.d(TAG, "Dataset count has changed, loading set to FALSE")
            state = state.copy(loading = false, previousTotalItemCount = totalItemCount)
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!state.loading && lastVisibleItemPosition + state.visibleThreshold > totalItemCount) {
            Log.d(TAG, "Loading one more page...")
            state = state.copy(currentPage = state.currentPage + 1)
            onLoadMore(state.currentPage, totalItemCount, view)
            state = state.copy(loading = true)
        }
    }

    // Call this method whenever performing new searches
    fun resetState() {
        Log.d(TAG, "resetState() - performing new searches")
        state = state.copy(
            currentPage = state.startingPageIndex,
            previousTotalItemCount = 0,
            loading = true
        )
    }

    fun saveState() {
        viewModel.state.value = state
    }

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?)
}
