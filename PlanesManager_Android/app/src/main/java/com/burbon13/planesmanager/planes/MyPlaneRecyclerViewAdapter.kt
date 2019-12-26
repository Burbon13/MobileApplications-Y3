package com.burbon13.planesmanager.planes

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.burbon13.planesmanager.R
import com.burbon13.planesmanager.planes.model.Plane

import kotlinx.android.synthetic.main.fragment_plane.view.*


/**
 * [RecyclerView.Adapter] that can display a [Plane] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyPlaneRecyclerViewAdapter(
    private val mValues: List<Plane>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyPlaneRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Plane
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_plane, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
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

    inner class ViewHolder(val planeView: View) : RecyclerView.ViewHolder(planeView) {
        val planeBrandView: TextView = planeView.plane_brand
        val planeModelView: TextView = planeView.plane_model
        val planePriceView: TextView = planeView.plane_price
        val planeYearView: TextView = planeView.plane_fabrication_year
        val planeTailNumberView: TextView = planeView.plane_tail_number
    }
}