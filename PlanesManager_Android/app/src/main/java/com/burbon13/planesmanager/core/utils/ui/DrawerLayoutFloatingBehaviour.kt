package com.burbon13.planesmanager.core.utils.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.snackbar.Snackbar


/**
 * CoordinatorLayout to make Snackbar push view upwards
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
        val translationY = 0f.coerceAtMost(dependency.translationY - dependency.height)
        child.translationY = translationY
        return true
    }
}
