package com.burbon13.planesmanager.core.utils.scroll


data class EndlessScrollState(
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    val visibleThreshold: Int = 5,
    // The current offset index of data you have loaded
    val currentPage: Int = 0,
    // The total number of items in the dataset after the last load
    val previousTotalItemCount: Int = 0,
    // True if we are still waiting for the last set of data to load.
    val loading: Boolean = true,
    // Sets the starting page index
    val startingPageIndex: Int = 0
)
