package com.burbon13.planesmanager.planes.ui.form


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.burbon13.planesmanager.R
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.utils.extensions.TAG
import com.burbon13.planesmanager.core.utils.extensions.afterTextChanged
import com.burbon13.planesmanager.planes.model.Plane
import com.burbon13.planesmanager.planes.ui.shared.SharedViewModelResult


class PlaneFormFragment : Fragment() {
    private lateinit var viewModel: PlaneFormViewModel
    private lateinit var sharedViewModelResult: SharedViewModelResult

    private lateinit var tailNumberEditText: EditText
    private lateinit var brandPicker: NumberPicker
    private lateinit var modelEditText: EditText
    private lateinit var yearEditText: EditText
    private lateinit var enginePicker: NumberPicker
    private lateinit var priceEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")
        viewModel = ViewModelProviders.of(this).get(PlaneFormViewModel::class.java)
        sharedViewModelResult = activity?.run {
            ViewModelProviders.of(this)[SharedViewModelResult::class.java]
        } ?: throw Exception("Invalid Activity!")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView()")
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
        priceEditText = rootView.findViewById(R.id.edit_text_price)
        submitButton = rootView.findViewById(R.id.button_submit_plane)
        progressBar = rootView.findViewById(R.id.progress_bar_plane_form)

        return rootView
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart()")
        setListeners()
    }

    private fun setListeners() {
        tailNumberEditText.afterTextChanged {
            Log.v(TAG, "Tail number field modified")
            formFieldsChanged()
        }
        modelEditText.afterTextChanged {
            Log.v(TAG, "Model field modified")
            formFieldsChanged()
        }
        yearEditText.afterTextChanged {
            Log.v(TAG, "Year field modified")
            formFieldsChanged()
        }
        priceEditText.afterTextChanged {
            Log.v(TAG, "Price field modified")
            formFieldsChanged()
        }
        viewModel.planeFormState.observe(this, Observer {
            Log.v(TAG, "(Observed) PlaneFormState changed")
            val planeFormState = it ?: return@Observer
            submitButton.isEnabled = planeFormState.isDataValid
            if (planeFormState.tailNumberError != null) {
                tailNumberEditText.error = getString(planeFormState.tailNumberError)
            }
            if (planeFormState.modelError != null) {
                modelEditText.error = getString(planeFormState.modelError)
            }
            if (planeFormState.fabricationYearError != null) {
                yearEditText.error = getString(planeFormState.fabricationYearError)
            }
            if (planeFormState.priceError != null) {
                priceEditText.error = getString(planeFormState.priceError)
            }
        })
        viewModel.processing.observe(this, Observer {
            Log.d(TAG, "(Observed) Plane processing")
            if (it) {
                progressBar.visibility = View.VISIBLE
                submitButton.isEnabled = false
            } else {
                progressBar.visibility = View.GONE
                submitButton.isEnabled = true
            }
        })
        viewModel.addPlaneResult.observe(this, Observer {
            Log.d(TAG, "(Observed) addPlaneResult observed in PlaneFormFragment")
            val addPlaneResult = it
            if (addPlaneResult is Result.Success) {
                Log.d(TAG, "Successful plane addition, setting addPlaneResult in view model")
                sharedViewModelResult.addPlaneResult.value = Result.Success("Plane added")
                Toast.makeText(context, "Plane added successfully!", Toast.LENGTH_LONG).show()
                Log.d(TAG, "Pop back stack!")
                findNavController().popBackStack()
            } else if (addPlaneResult is Result.Error) {
                Log.d(
                    TAG,
                    "Error (and adding to view model) on adding new plane: ${addPlaneResult.message}"
                )
                sharedViewModelResult.addPlaneResult.value = addPlaneResult
                Toast.makeText(context, addPlaneResult.message, Toast.LENGTH_LONG).show()
            }
        })
        submitButton.setOnClickListener {
            Log.d(TAG, "(Observed) Submit button pressed")
            viewModel.addPlane(
                tailNumberEditText.text.toString(),
                Plane.Brand.valueOf(brandPicker.displayedValues[brandPicker.value]),
                modelEditText.text.toString(),
                Plane.Engine.valueOf(enginePicker.displayedValues[enginePicker.value]),
                yearEditText.text.toString(),
                priceEditText.text.toString()
            )
        }
    }

    private fun formFieldsChanged() {
        viewModel.planeFormStateChanged(
            tailNumberEditText.text.toString(),
            modelEditText.text.toString(),
            yearEditText.text.toString(),
            priceEditText.text.toString()
        )
    }
}
