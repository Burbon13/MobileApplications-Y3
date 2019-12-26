package com.burbon13.planesmanager.core.utils.extensions

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.burbon13.planesmanager.core.TAG


fun RecyclerView.setDivider(@DrawableRes drawableRes: Int) {
    Log.d(TAG, "Setting divider for RecyclerView")
    val divider = DividerItemDecoration(
        this.context,
        DividerItemDecoration.VERTICAL
    )
    val drawable = ContextCompat.getDrawable(
        this.context,
        drawableRes
    )
    drawable?.let {
        divider.setDrawable(it)
        addItemDecoration(divider)
    }
}
