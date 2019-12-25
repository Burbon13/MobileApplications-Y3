package com.burbon13.planesmanager.model


data class Plane(
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
