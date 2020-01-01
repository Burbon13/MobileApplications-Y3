package com.burbon13.planesmanager.planes.model

import java.lang.Exception
import java.util.Calendar


class PlaneValidator {
    fun isTailNumberValid(tailNumber: String): Boolean {
        return tailNumber.length > 4
    }

    fun isModelValid(model: String): Boolean {
        return model.length > 2
    }

    fun isFabricationYearValid(fabricationYear: Int): Boolean {
        return fabricationYear > 1950 && fabricationYear < Calendar.getInstance().get(Calendar.YEAR)
    }

    fun isFabricationYearValid(fabricationYear: String): Boolean {
        return try {
            val intFabricationYear = fabricationYear.toInt()
            intFabricationYear > 1950 && intFabricationYear < Calendar.getInstance().get(
                Calendar.YEAR
            )
        } catch (e: Exception) {
            false
        }
    }

    fun isPriceValid(price: Long): Boolean {
        return price > 0
    }

    fun isPriceValid(price: String): Boolean {
        return try {
            val longPrice = price.toLong()
            longPrice > 0
        } catch (e: Exception) {
            false
        }
    }
}
