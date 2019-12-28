package com.burbon13.planesmanager.planes.ui.form


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import com.burbon13.planesmanager.R
import com.burbon13.planesmanager.planes.model.Plane


class PlaneFormFragment : Fragment() {
    private lateinit var tailNumberEditText: EditText
    private lateinit var brandPicker: NumberPicker
    private lateinit var modelEditText: EditText
    private lateinit var yearEditText: EditText
    private lateinit var enginePicker: NumberPicker
    private lateinit var price: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_plane_form, container, false)

        tailNumberEditText = rootView.findViewById(R.id.edit_text_tail_number)
        brandPicker = rootView.findViewById(R.id.number_picker_brand)
        val brands = Plane.Brand.values().map { it.toString() }
        brandPicker.minValue = 0
        brandPicker.maxValue = brands.size - 1
        brandPicker.displayedValues = brands.toTypedArray()
        modelEditText = rootView.findViewById(R.id.edit_text_model)
        yearEditText = rootView.findViewById(R.id.edit_text_year)
        enginePicker = rootView.findViewById(R.id.number_picker_engine)
        val engines = Plane.Engine.values().map { it.toString() }
        enginePicker.minValue = 0
        enginePicker.maxValue = engines.size - 1
        enginePicker.displayedValues = engines.toTypedArray()
        price = rootView.findViewById(R.id.edit_text_price)

        return rootView
    }

}
