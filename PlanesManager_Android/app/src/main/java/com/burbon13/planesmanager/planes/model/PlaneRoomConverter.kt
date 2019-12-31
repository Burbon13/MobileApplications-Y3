package com.burbon13.planesmanager.planes.model

import androidx.room.TypeConverter


class PlaneRoomConverter {
    @TypeConverter
    fun storeEngineToString(engine: Plane.Engine): String = engine.toString()

    @TypeConverter
    fun storeStringToEngine(engine: String): Plane.Engine = Plane.Engine.valueOf(engine)

    @TypeConverter
    fun storeBrandToString(brand: Plane.Brand): String = brand.toString()

    @TypeConverter
    fun storeStringToBrand(brand: String): Plane.Brand = Plane.Brand.valueOf(brand)
}
