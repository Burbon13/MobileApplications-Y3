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


    init {
        // Add some sample items.
        ITEMS.add(Plane("N903NN", "Boeing", "747-300", 1995, Plane.Engine.TURBOJET, 324000000))
        ITEMS.add(Plane("UR-GED", "Boeing", "747-200", 1991, Plane.Engine.TURBOJET, 301000000))
        ITEMS.add(Plane("JA8089", "Airbus", "A380", 2002, Plane.Engine.TURBOJET, 324000000))
        ITEMS.add(Plane("N904DE", "Airbus", "A320", 2001, Plane.Engine.TURBOJET, 424000000))
        ITEMS.add(Plane("F-WTSA", "Airbus", "A321 Neo", 2008, Plane.Engine.TURBOJET, 567000000))
        ITEMS.add(Plane("XR220", "Boeing", "747-300", 1995, Plane.Engine.TURBOJET, 324000000))
        ITEMS.add(Plane("N904DE", "Boeing", "747-200", 1991, Plane.Engine.TURBOJET, 301000000))
        ITEMS.add(Plane("N234DE", "Airbus", "A380", 2002, Plane.Engine.TURBOJET, 324000000))
        ITEMS.add(Plane("N9TTDE", "Airbus", "A320", 2001, Plane.Engine.TURBOJET, 424000000))
        ITEMS.add(Plane("XXX4DE", "Airbus", "A321 Neo", 2008, Plane.Engine.TURBOJET, 567000000))
        ITEMS.add(Plane("N904DE", "Boeing", "747-300", 1995, Plane.Engine.TURBOJET, 324000000))
        ITEMS.add(Plane("N934DI", "Boeing", "747-200", 1991, Plane.Engine.TURBOJET, 301000000))
        ITEMS.add(Plane("N9T4DE", "Airbus", "A380", 2002, Plane.Engine.TURBOJET, 324000000))
        ITEMS.add(Plane("NX04DE", "Airbus", "A320", 2001, Plane.Engine.TURBOJET, 424000000))
        ITEMS.add(Plane("N904DE", "Airbus", "A321 Neo", 2008, Plane.Engine.TURBOJET, 567000000))
        ITEMS.add(Plane("N9013E", "Boeing", "747-300", 1995, Plane.Engine.TURBOJET, 324000000))
        ITEMS.add(Plane("N804DE", "Boeing", "747-200", 1991, Plane.Engine.TURBOJET, 301000000))
        ITEMS.add(Plane("G-AMNN", "Airbus", "A380", 2002, Plane.Engine.TURBOJET, 324000000))
        ITEMS.add(Plane("N97810", "Airbus", "A320", 2001, Plane.Engine.TURBOJET, 424000000))
        ITEMS.add(Plane("N59LW", "Airbus", "A321 Neo", 2008, Plane.Engine.TURBOJET, 567000000))
    }


}
