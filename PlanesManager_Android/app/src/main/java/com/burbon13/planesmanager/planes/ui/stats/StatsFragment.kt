package com.burbon13.planesmanager.planes.ui.stats


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.burbon13.planesmanager.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend


/**
 * A simple [Fragment] subclass.
 */
class StatsFragment : Fragment() {
    private lateinit var pieChart: PieChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_stats, container, false)

        pieChart = rootView.findViewById(R.id.pie_chart_stats)
        setPieChart(pieChart)

        return rootView
    }

    private fun setPieChart(pieChart: PieChart) {
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 0f, 10f, 5f)

        pieChart.dragDecelerationFrictionCoef = 0.95f

//        pieChart.setCenterTextTypeface(tfLight)
//        pieChart.centerText = generateCenterSpannableText()

        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)

        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f

        pieChart.setDrawCenterText(true)

        pieChart.rotationAngle = 0f
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true

        //pieChart.setUnit(" €");
        //pieChart.setDrawUnitsInChart(true);

        // add a selection listener
        //pieChart.setUnit(" €");
        //pieChart.setDrawUnitsInChart(true);

        pieChart.animateY(1400, Easing.EaseInOutQuad)
        //pieChart.spin(2000, 0, 360);
        //pieChart.spin(2000, 0, 360);
        val l: Legend = pieChart.getLegend()
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        // entry label styling
        // entry label styling
        pieChart.setEntryLabelColor(Color.WHITE)
//        pieChart.setEntryLabelTypeface(tfRegular)
        pieChart.setEntryLabelTextSize(12f)
    }

}
