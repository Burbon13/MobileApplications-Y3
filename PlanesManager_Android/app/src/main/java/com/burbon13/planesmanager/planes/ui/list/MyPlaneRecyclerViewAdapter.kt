package com.burbon13.planesmanager.planes.ui

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.burbon13.planesmanager.R
import com.burbon13.planesmanager.core.TAG
import com.burbon13.planesmanager.planes.model.Plane

import kotlinx.android.synthetic.main.fragment_plane.view.*


/**
 * [RecyclerView.Adapter] that can display a [Plane] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MyPlaneRecyclerViewAdapter(
    private var mValues: List<Plane>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyPlaneRecyclerViewAdapter.ViewHolder>() {

    var planeList: List<Plane>
        set(value) {
            mValues = value
            Log.d(TAG, "Notify plane list data set changed")
            notifyDataSetChanged()
        }
        get() = mValues
    private var mShowLoading = false

    companion object {
        private const val VIEW_TYPE_LOADING = 1
        private const val VIEW_TYPE_NORMAL = 2
    }

    private val mOnClickListener: View.OnClickListener

    init {
        Log.d(TAG, "Initializing MyPlaneRecyclerViewAdapter")
        Log.d(TAG, "Setting click listener on plane items")
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Plane
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "onCreateViewHolder()")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_plane, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        Log.v(TAG, "onBindViewHolder() for position=$position: $item")
        holder.planeBrandView.text = item.brand
        holder.planeModelView.text = item.model
        holder.planePriceView.text = "US$${item.price / 1000000} milion"
        holder.planeYearView.text = item.fabricationYear.toString()
        holder.planeTailNumberView.text = item.tailNumber

        with(holder.planeView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

//    override fun

    inner class ViewHolder(val planeView: View) : RecyclerView.ViewHolder(planeView) {
        val planeBrandView: TextView = planeView.plane_brand
        val planeModelView: TextView = planeView.plane_model
        val planePriceView: TextView = planeView.plane_price
        val planeYearView: TextView = planeView.plane_fabrication_year
        val planeTailNumberView: TextView = planeView.plane_tail_number
    }
}
