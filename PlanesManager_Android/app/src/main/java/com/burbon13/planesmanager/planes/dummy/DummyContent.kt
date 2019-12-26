package com.burbon13.planesmanager.planes.dummy

import com.burbon13.planesmanager.planes.model.Plane
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<Plane> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */

    private val COUNT = 5

    init {
        // Add some sample items.
        ITEMS.add(Plane(1, "Boeing", "747-300", 1995, Plane.Engine.TURBOJET, 324000000))
        ITEMS.add(Plane(2, "Boeing", "747-200", 1991, Plane.Engine.TURBOJET, 301000000))
        ITEMS.add(Plane(3, "Airbus", "A380", 2002, Plane.Engine.TURBOJET, 324000000))
        ITEMS.add(Plane(4, "Airbus", "A320", 2001, Plane.Engine.TURBOJET, 424000000))
        ITEMS.add(Plane(5, "Airbus", "A321 Neo", 2008, Plane.Engine.TURBOJET, 567000000))
    }


}
