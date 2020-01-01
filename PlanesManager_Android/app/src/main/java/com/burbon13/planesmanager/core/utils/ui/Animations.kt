package com.burbon13.planesmanager.core.utils.ui

import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce


object Animations {
    private const val STIFFNESS = SpringForce.STIFFNESS_MEDIUM
    private const val DAMPING_RATIO = SpringForce.DAMPING_RATIO_HIGH_BOUNCY

    fun attachSpringAnimation(movingView: View) {

        lateinit var xAnimation: SpringAnimation
        lateinit var yAnimation: SpringAnimation

        movingView.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    xAnimation = createSpringAnimation(
                        movingView, SpringAnimation.X, movingView.x,
                        STIFFNESS,
                        DAMPING_RATIO
                    )
                    yAnimation = createSpringAnimation(
                        movingView, SpringAnimation.Y, movingView.y,
                        STIFFNESS,
                        DAMPING_RATIO
                    )
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        movingView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            })

        var dX = 0f
        var dY = 0f
        movingView.setOnTouchListener { view, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    // capture the difference between view's top left corner and touch point
                    dX = view.x - event.rawX
                    dY = view.y - event.rawY

                    // cancel animations so we can grab the view during previous animation
                    xAnimation.cancel()
                    yAnimation.cancel()
                }
                MotionEvent.ACTION_MOVE -> {
                    //  a different approach would be to change the view's LayoutParams.
                    movingView.animate()
                        .x(event.rawX + dX)
                        .y(event.rawY + dY)
                        .setDuration(0)
                        .start()
                }
                MotionEvent.ACTION_UP -> {
                    xAnimation.start()
                    yAnimation.start()
                }
            }
            true
        }
    }

    private fun createSpringAnimation(
        view: View,
        property: DynamicAnimation.ViewProperty,
        finalPosition: Float,
        stiffness: Float,
        dampingRatio: Float
    ): SpringAnimation {
        val animation = SpringAnimation(view, property)
        val spring = SpringForce(finalPosition)
        spring.stiffness = stiffness
        spring.dampingRatio = dampingRatio
        animation.spring = spring
        return animation
    }
}
