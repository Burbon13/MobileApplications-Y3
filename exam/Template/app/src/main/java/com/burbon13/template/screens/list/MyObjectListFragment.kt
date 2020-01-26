package com.burbon13.template.screens.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.burbon13.template.R
import com.burbon13.template.core.extensions.TAG
import com.burbon13.template.myobjects.model.MyObject


class MyObjectListFragment : Fragment(), OnListFragmentInteractionListener {
    private lateinit var myObjectListViewModel: MyObjectListViewModel

    private lateinit var myObjectRecyclerViewAdapter: MyObjectRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")
        myObjectListViewModel = ViewModelProviders.of(this, MyObjectListViewModelFactory())
            .get(MyObjectListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView()")

        myObjectRecyclerViewAdapter = MyObjectRecyclerViewAdapter(
            listOf(),
            this@MyObjectListFragment as OnListFragmentInteractionListener
        )

        return inflater.inflate(R.layout.fragment_my_object_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        myObjectListViewModel.planeLiveData.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "MyObjectsList live data changed!")
            myObjectRecyclerViewAdapter.myObjectsList = it
        })
    }

    override fun onListFragmentInteraction(item: MyObject?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Log.e(TAG, "NOT IMPLEMENTED!")
    }
}
