package com.burbon13.planesmanager.planes.ui.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.burbon13.planesmanager.R
import com.burbon13.planesmanager.core.utils.extensions.TAG
import com.burbon13.planesmanager.core.utils.scroll.EndlessRecyclerViewScrollListener
import com.burbon13.planesmanager.core.utils.extensions.hideKeyboard
import com.burbon13.planesmanager.core.utils.extensions.setDivider
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.utils.scroll.EndlessScrollState
import com.burbon13.planesmanager.core.utils.scroll.EndlessScrollViewModel
import com.burbon13.planesmanager.core.utils.scroll.EndlessScrollViewModelFactory

import com.burbon13.planesmanager.planes.model.Plane
import com.burbon13.planesmanager.planes.ui.form.PlaneActionResult
import com.burbon13.planesmanager.planes.ui.shared.SharedPlaneFormViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


/**
 * A fragment representing a list of Planes.
 */
class PlanesFragment : Fragment(),
    OnListFragmentInteractionListener {
    private lateinit var planesViewModel: PlanesViewModel
    private lateinit var scrollViewModel: EndlessScrollViewModel
    private lateinit var sharedViewModelResult: SharedPlaneFormViewModel

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    private lateinit var progressBar: ProgressBar
    private lateinit var planesRecyclerViewAdapter: MyPlaneRecyclerViewAdapter
    private lateinit var planesRecyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var isFloatingMenuOpen = false
    private lateinit var menuButton: FloatingActionButton
    private lateinit var addPlaneButton: FloatingActionButton
    private lateinit var planeStatsButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")
        planesViewModel = ViewModelProviders.of(this).get(PlanesViewModel::class.java)
        scrollViewModel =
            ViewModelProviders.of(this, EndlessScrollViewModelFactory(EndlessScrollState()))
                .get(EndlessScrollViewModel::class.java)
        sharedViewModelResult = activity?.run {
            ViewModelProviders.of(this)[SharedPlaneFormViewModel::class.java]
        } ?: throw Exception("Unable to retrieve activity!")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView()")
        val rootView = inflater.inflate(R.layout.fragment_plane_list, container, false)

        planesRecyclerView = rootView.findViewById(R.id.recycler_view_planes)
        planesRecyclerView.setDivider(R.drawable.recycler_view_divider)

        linearLayoutManager = LinearLayoutManager(context)
        planesRecyclerView.layoutManager = linearLayoutManager

        planesRecyclerViewAdapter = MyPlaneRecyclerViewAdapter(
            listOf(),
            this@PlanesFragment as OnListFragmentInteractionListener
        )
        planesRecyclerView.adapter = planesRecyclerViewAdapter

        progressBar = rootView.findViewById(R.id.loading_progress_bar)
        menuButton = rootView.findViewById(R.id.floating_btn)
        addPlaneButton = rootView.findViewById(R.id.floating_btn_plane_form)
        planeStatsButton = rootView.findViewById(R.id.floating_btn_plane_stats)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        planesViewModel.planeLiveData.observe(this, Observer {
            planesRecyclerViewAdapter.planeList = it
        })
        planesViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })
        sharedViewModelResult.planeActionResult.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "(Observed) Plane action")
            if (it is Result.Success) {
                if (it.data != PlaneActionResult.NO_ACTION) {
                    sharedViewModelResult.resetState()
                    Log.d(TAG, "Refreshing planes list")
                    reloadList()
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart()")
        hideKeyboard()
        scrollListener =
            object : EndlessRecyclerViewScrollListener(scrollViewModel, linearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    Log.d(TAG, "EndlessRecyclerView onLoadMore()!!!")
                    loadNextDataFromApi(page)
                }
            }
        planesRecyclerView.addOnScrollListener(scrollListener)
        menuButton.setOnClickListener {
            if (!isFloatingMenuOpen) {
                Log.d(TAG, "Opening floating action button menu")
                openFabMenu()
            } else {
                Log.d(TAG, "Closing floating action button menu")
                closeFabMenu()
            }
        }
        addPlaneButton.setOnClickListener {
            Log.d(TAG, "Navigating from PlanesFragment to PlaneFormFragment")
            isFloatingMenuOpen = false
            findNavController().navigate(R.id.action_planesFragment_to_planeFormFragment)
        }
        planeStatsButton.setOnClickListener {
            Log.d(TAG, "Navigation from PlanesFragment to StatsFragment")
            isFloatingMenuOpen = false
            findNavController().navigate(R.id.action_planesFragment_to_statsFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        scrollListener.saveState()
    }

    override fun onStop() {
        super.onStop()
        planesRecyclerView.clearOnScrollListeners()
        menuButton.setOnClickListener(null)
        addPlaneButton.setOnClickListener(null)
        planeStatsButton.setOnClickListener(null)
    }

    override fun onListFragmentInteraction(item: Plane?) {
        item?.let {
            Log.i(
                TAG,
                "Plane with tailNumber=${item.tailNumber} touched; navigating to PlaneDataFragment"
            )
            isFloatingMenuOpen = false
            findNavController().navigate(
                PlanesFragmentDirections.actionPlanesFragmentToPlaneDataFragment(
                    item.tailNumber
                )
            )
        }
    }

    private fun loadNextDataFromApi(offset: Int) {
        Log.d(TAG, "Load data from next page with offset=$offset")
        planesViewModel.appendPlanesPage(offset)
    }

    private fun reloadList() {
        Log.d(TAG, "Reload list")
        scrollListener.resetState()
        planesViewModel.reloadList()
    }

    private fun openFabMenu() {
        isFloatingMenuOpen = true
        addPlaneButton.animate().translationY(-resources.getDimension(R.dimen.standard_65))
        planeStatsButton.animate().translationY(-resources.getDimension(R.dimen.standard_120))
    }

    private fun closeFabMenu() {
        isFloatingMenuOpen = false
        addPlaneButton.animate().translationY(0f)
        planeStatsButton.animate().translationY(0f)
    }
}
