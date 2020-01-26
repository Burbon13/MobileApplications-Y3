package com.burbon13.template.screens.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.burbon13.template.R
import com.burbon13.template.core.extensions.TAG
import com.burbon13.template.myobjects.model.MyObject
import kotlinx.android.synthetic.main.fragment_my_object_item.view.*


class MyObjectRecyclerViewAdapter(
    myObjectsList: List<MyObject>,
    private val onListFragmentInteractionListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyObjectRecyclerViewAdapter.ViewHolder>() {

    init {
        Log.d(TAG, "Initialize MyObjectRecyclerViewAdapter")
    }

    var myObjectsList: List<MyObject> = myObjectsList
        set(value) {
            field = value
            Log.d(TAG, "Notify myObject list data set changed")
            notifyDataSetChanged()
        }

    private val mOnClickListener: View.OnClickListener

    init {
        Log.d(TAG, "Initializing MyPlaneRecyclerViewAdapter")
        Log.d(TAG, "Setting click listener on plane items")
        mOnClickListener = View.OnClickListener { view ->
            val item = view.tag as MyObject
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            onListFragmentInteractionListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.v(TAG, "onCreateViewHolder()")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_my_object_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val myObject = myObjectsList[position]
        //Log.v(TAG, "onBindViewHolder() for position=$position: $plane")
        viewHolder.val1View.text = "Name: ${myObject.name}"
        viewHolder.val2View.text = "Count: ${myObject.count}"
        viewHolder.val3View.text = "Val3: ${myObject.val3}"
        viewHolder.val4View.text = "Val4: ${myObject.val4}"
        viewHolder.idView.text = myObject.id

        with(viewHolder.myObjectView) {
            tag = myObject
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = myObjectsList.size

    inner class ViewHolder(val myObjectView: View) : RecyclerView.ViewHolder(myObjectView) {
        val val1View: TextView = myObjectView.obj_item_val_1
        val val2View: TextView = myObjectView.obj_item_val_2
        val val3View: TextView = myObjectView.obj_item_val_3
        val val4View: TextView = myObjectView.obj_item_val_4
        val idView: TextView = myObjectView.obj_item_id
    }
}
