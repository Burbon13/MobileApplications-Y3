package com.burbon13.planesmanager.planes.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.burbon13.planesmanager.R
import com.burbon13.planesmanager.core.TAG
import com.burbon13.planesmanager.core.utils.EndlessRecyclerViewScrollListener
import com.burbon13.planesmanager.core.utils.extensions.hideKeyboard
import com.burbon13.planesmanager.core.utils.extensions.setDivider

import com.burbon13.planesmanager.planes.model.Plane


/**
 * A fragment representing a list of Items.
 */
class PlanesFragment : Fragment(), OnListFragmentInteractionListener {
    private lateinit var viewModel: PlanesViewModel
    private lateinit var recyclerViewAdapter: MyPlaneRecyclerViewAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var loadingProgressBar: ProgressBar
    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView()")
        val view = inflater.inflate(R.layout.fragment_plane_list, container, false)

        val recyclerViewPlanes = view.findViewById<RecyclerView>(R.id.recycler_view_planes)
        recyclerViewPlanes.setDivider(R.drawable.recycler_view_divider)
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerViewPlanes.layoutManager = linearLayoutManager
        recyclerViewAdapter = MyPlaneRecyclerViewAdapter(
            listOf(),
            this@PlanesFragment as OnListFragmentInteractionListener
        )
        recyclerViewPlanes.adapter = recyclerViewAdapter
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page)
            }
        }
        recyclerViewPlanes.addOnScrollListener(scrollListener)
        loadingProgressBar = view.findViewById(R.id.loading_progress_bar)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated()")
        hideKeyboard()
        viewModel = ViewModelProviders.of(this).get(PlanesViewModel::class.java)
        setListeners()
    }

    private fun setListeners() {
        viewModel.toastMessageLiveData.observe(this, Observer {
            Log.d(TAG, "Toast changed, showing")
            activity?.runOnUiThread {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        })
        viewModel.planeLiveData.observe(this, Observer {
            recyclerViewAdapter.planeList = it
        })
        viewModel.loading.observe(this, Observer {
            if (it) {
                loadingProgressBar.visibility = View.VISIBLE
            } else {
                loadingProgressBar.visibility = View.GONE
            }
        })
    }


    override fun onListFragmentInteraction(item: Plane?) {
        Log.i(TAG, "Plane touched")
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    private fun loadNextDataFromApi(offset: Int) {
        Log.d(TAG, "Load data from next page with offset=$offset")
        viewModel.appendPlanesPage(offset)
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            PlanesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
