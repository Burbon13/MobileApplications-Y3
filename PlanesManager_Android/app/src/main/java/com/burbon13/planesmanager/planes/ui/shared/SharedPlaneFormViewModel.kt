package com.burbon13.planesmanager.planes.ui.shared

import com.burbon13.planesmanager.planes.ui.form.PlaneActionResult
import com.burbon13.planesmanager.core.Result


class SharedPlaneFormViewModel : SharedViewModelResult<PlaneActionResult>() {
    fun planeDeleted() {
        super.planeActionResult.value = Result.Success(PlaneActionResult.PLANE_DELETED)
    }

    fun resetState() {
        super.planeActionResult.value = Result.Success(PlaneActionResult.NO_ACTION)
    }

    fun planeAdded() {
        super.planeActionResult.value = Result.Success(PlaneActionResult.PLANE_ADDED)
    }
}
