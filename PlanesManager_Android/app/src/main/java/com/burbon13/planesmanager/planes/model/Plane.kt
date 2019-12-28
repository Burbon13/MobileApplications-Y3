package com.burbon13.planesmanager.planes.model


data class Plane(
    val tailNumber: String,
    val brand: Brand,
    val model: String,
    val fabricationYear: Int,
    val engine: Engine,
    val price: Long
) {
    enum class Engine {
        TURBOPROP,
        TURBOJET,
        TURBOFAN,
        RAMJET
    }

    enum class Brand {
        BOEING,
        AIRBUS,
        ATR,
        EMBRAER,
        BOMBARDIER
    }
}
