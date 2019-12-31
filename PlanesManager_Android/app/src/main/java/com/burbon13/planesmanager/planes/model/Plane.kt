package com.burbon13.planesmanager.planes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


@Entity(tableName = "planes")
@TypeConverters(PlaneRoomConverter::class)
data class Plane(
    @PrimaryKey
    @ColumnInfo(name = "tailNumber")
    val tailNumber: String,

    @ColumnInfo(name = "brand")
    val brand: Brand,

    @ColumnInfo(name = "model")
    val model: String,

    @ColumnInfo(name = "fabricationYear")
    val fabricationYear: Int,

    @ColumnInfo(name = "engine")
    val engine: Engine,

    @ColumnInfo(name = "price")
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

    companion object {
        val EngineList = Engine.values().map { it.toString() }
        val BrandList = Brand.values().map { it.toString() }
    }
}
