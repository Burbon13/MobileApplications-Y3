package com.burbon13.planesmanager.planes.model


data class Plane(
    val tailNumber: String,
    val brand: String,
    val model: String,
    val fabricationYear: Int,
    val engine: Engine,
    val price: Int
) {
    enum class Engine {
        TURBOPROP,
        TURBOJET,
        TURBOFAN,
        RAMJET
    }
}
