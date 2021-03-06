package com.burbon13.planesmanager.planes.ui.list

import android.graphics.Color
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.burbon13.planesmanager.R
import com.burbon13.planesmanager.core.utils.extensions.TAG
import com.burbon13.planesmanager.planes.model.Plane

import kotlinx.android.synthetic.main.fragment_plane.view.*


/**
 * [RecyclerView.Adapter] that can display a [Plane] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MyPlaneRecyclerViewAdapter(
    private var planesList: List<Plane>,
    private val onListFragmentInteractionListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyPlaneRecyclerViewAdapter.ViewHolder>() {

    var planeList: List<Plane>
        set(value) {
            planesList = value
            Log.d(TAG, "Notify plane list data set changed")
            notifyDataSetChanged()
        }
        get() = planesList

    private val mOnClickListener: View.OnClickListener

    init {
        Log.d(TAG, "Initializing MyPlaneRecyclerViewAdapter")
        Log.d(TAG, "Setting click listener on plane items")
        mOnClickListener = View.OnClickListener { view ->
            val item = view.tag as Plane
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            onListFragmentInteractionListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.v(TAG, "onCreateViewHolder()")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_plane, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val plane = planesList[position]
        Log.v(TAG, "onBindViewHolder() for position=$position: $plane")
        viewHolder.planeBrandView.text = plane.brand?.toString()
        viewHolder.planeBrandView.setTextColor(getBrandColor(plane.brand))
        viewHolder.planeModelView.text = plane.model
        viewHolder.planePriceView.text = getPriceText(plane.price)
        viewHolder.planePriceView.setTextColor(getPriceColor(plane.price))
        viewHolder.planeYearView.text = plane.fabricationYear?.toString()
        viewHolder.planeTailNumberView.text = plane.tailNumber

        with(viewHolder.planeView) {
            tag = plane
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = planesList.size

    private fun getPriceText(price: Long?): String {
        if (price == null) {
            return "N/A"
        }
        return when (true) {
            price >= 1000000 -> "US$${price / 1000000} million"
            price >= 1000 -> "US$${price / 1000} K"
            else -> "US$$price"
        }
    }

    private fun getPriceColor(price: Long?): Int {
        if (price == null) {
            return Color.BLACK
        }
        return when (true) {
            price >= 1000000 -> Color.parseColor("#00e81f")
            price >= 1000 -> Color.parseColor("#489c55")
            else -> Color.parseColor("#466b4b")
        }
    }

    private fun getBrandColor(brand: Plane.Brand?): Int {
        return when (brand) {
            Plane.Brand.BOEING -> Color.parseColor("#0039a6")
            Plane.Brand.AIRBUS -> Color.parseColor("#eb34eb")
            Plane.Brand.BOMBARDIER -> Color.parseColor("#23ba35")
            Plane.Brand.ATR -> Color.parseColor("#1ac7b6")
            Plane.Brand.EMBRAER -> Color.parseColor("#e6e21c")
            else -> Color.BLACK
        }
    }

    inner class ViewHolder(val planeView: View) : RecyclerView.ViewHolder(planeView) {
        val planeBrandView: TextView = planeView.plane_brand
        val planeModelView: TextView = planeView.plane_model
        val planePriceView: TextView = planeView.plane_price
        val planeYearView: TextView = planeView.plane_fabrication_year
        val planeTailNumberView: TextView = planeView.plane_tail_number
    }
}
