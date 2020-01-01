package com.burbon13.planesmanager.planes.ui.stats


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.burbon13.planesmanager.R
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.utils.extensions.TAG
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.utils.ColorTemplate


class StatsFragment : Fragment() {
    private lateinit var statsViewModel: StatsViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statsViewModel = ViewModelProviders.of(this).get(StatsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_stats, container, false)
        pieChart = rootView.findViewById(R.id.pie_chart_stats)
        pieChart.visibility = View.INVISIBLE
        progressBar = rootView.findViewById(R.id.progress_bar_pie_chart)
        progressBar.visibility = View.VISIBLE
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        statsViewModel.brandCountMap.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "(Observer) Stats count map value changed")
            if (it is Result.Success) {
                Log.d(TAG, "Stats count map loaded successfully")
                val pieDataSet = PieDataSet(it.data, "")
                pieDataSet.colors = ColorTemplate.COLORFUL_COLORS.toCollection(ArrayList())
                pieChart.data = PieData(pieDataSet)
                pieChart.animateXY(1000, 1000)
                pieChart.description = null
            } else if (it is Result.Error) {
                Log.e(TAG, "Stats could not be loaded, toasting error message: ${it.message}")
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
            progressBar.visibility = View.GONE
            pieChart.visibility = View.VISIBLE
        })
    }
}
