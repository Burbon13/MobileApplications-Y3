package com.burbon13.planesmanager.planes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.burbon13.planesmanager.R
import com.burbon13.planesmanager.core.TAG
import com.burbon13.planesmanager.core.utils.extensions.hideKeyboard
import com.burbon13.planesmanager.core.utils.extensions.setDivider

import com.burbon13.planesmanager.planes.dummy.DummyContent
import com.burbon13.planesmanager.planes.model.Plane
import kotlinx.android.synthetic.main.fragment_plane_list.*


/**
 * A fragment representing a list of Items.
 */
class PlanesFragment : Fragment(), OnListFragmentInteractionListener {

    // TODO: Customize parameters
    private var columnCount = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_plane_list, container, false)

        val recyclerViewPlanes = view.findViewById<RecyclerView>(R.id.recycler_view_planes)
        recyclerViewPlanes.setDivider(R.drawable.recycler_view_divider)
        recyclerViewPlanes.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }
        recyclerViewPlanes.adapter = MyPlaneRecyclerViewAdapter(
            DummyContent.ITEMS,
            this@PlanesFragment as OnListFragmentInteractionListener
        )

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hideKeyboard()
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            PlanesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    override fun onListFragmentInteraction(item: Plane?) {
        Log.i(TAG, "Plane touched")
    }
}
