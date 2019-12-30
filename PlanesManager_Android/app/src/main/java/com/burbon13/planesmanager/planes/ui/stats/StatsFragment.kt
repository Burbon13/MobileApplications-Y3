package com.burbon13.planesmanager.planes.ui.stats


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.burbon13.planesmanager.R
import com.burbon13.planesmanager.core.Result
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.utils.ColorTemplate


/**
 * A simple [Fragment] subclass.
 */
class StatsFragment : Fragment() {
    private lateinit var viewModel: StatsViewModel

    private lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StatsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_stats, container, false)
        pieChart = rootView.findViewById(R.id.pie_chart_stats)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.brandCountMap.observe(viewLifecycleOwner, Observer {
            if (it is Result.Success) {
                val pieDataSet = PieDataSet(it.data, "")
                pieDataSet.colors = ColorTemplate.COLORFUL_COLORS.toCollection(ArrayList())
                pieChart.data = PieData(pieDataSet)
                pieChart.animateXY(1000, 1000)
                pieChart.description = null
            } else if (it is Result.Error) {
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}
