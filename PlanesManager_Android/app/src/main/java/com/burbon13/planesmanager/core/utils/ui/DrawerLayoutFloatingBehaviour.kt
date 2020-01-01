package com.burbon13.planesmanager.core.utils.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.snackbar.Snackbar


/**
 * CoordinatorLayout to make the Snackbar push the main view upwards
 */
class DrawerLayoutFloatingBehaviour : CoordinatorLayout.Behavior<DrawerLayout> {

    constructor() : super()

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: DrawerLayout,
        dependency: View
    ): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: DrawerLayout,
        dependency: View
    ): Boolean {
        val layoutParams = child.layoutParams
        layoutParams.height = parent.height - dependency.height
        child.layoutParams = layoutParams
        return true
    }

    override fun onDependentViewRemoved(
        parent: CoordinatorLayout,
        child: DrawerLayout,
        dependency: View
    ) {
        val layoutParams = child.layoutParams
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        child.layoutParams = layoutParams
    }
}
