package com.burbon13.planesmanager.planes.ui.plane


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.burbon13.planesmanager.R
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.utils.extensions.TAG
import com.burbon13.planesmanager.planes.model.Plane
import com.burbon13.planesmanager.planes.model.PlaneValidator
import com.burbon13.planesmanager.planes.ui.shared.SharedPlaneFormViewModel
import java.lang.Exception


class PlaneDataFragment : Fragment() {
    private lateinit var viewModel: PlaneDataViewModel
    private lateinit var sharedViewModel: SharedPlaneFormViewModel
    private val args: PlaneDataFragmentArgs by navArgs()
    private lateinit var plane: Plane
    private var planeAlreadyExists = false
    private val planeValidator = PlaneValidator()

    private lateinit var locationAndCancelButton: Button
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var tailNumberTextView: TextView
    private lateinit var brandTextView: TextView
    private lateinit var modelTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var engineTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var updateHintTextView: TextView

    private var updateMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PlaneDataViewModel::class.java)
        viewModel.loadPlane(args.planeTailNumber)
        sharedViewModel = activity?.run {
            ViewModelProviders.of(this)[SharedPlaneFormViewModel::class.java]
        } ?: throw Exception("Invalid Activity!")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_plane_data, container, false)

        tailNumberTextView = rootView.findViewById(R.id.tail_number_value)
        brandTextView = rootView.findViewById(R.id.brand_value)
        modelTextView = rootView.findViewById(R.id.model_value)
        yearTextView = rootView.findViewById(R.id.year_value)
        engineTextView = rootView.findViewById(R.id.engine_value)
        priceTextView = rootView.findViewById(R.id.price_value)
        updateHintTextView = rootView.findViewById(R.id.text_view_update_hint)

        locationAndCancelButton = rootView.findViewById(R.id.geolocation_button)
        locationAndCancelButton.isEnabled = false
        updateButton = rootView.findViewById(R.id.update_button)
        updateButton.isEnabled = false
        deleteButton = rootView.findViewById(R.id.button_delete_plane)
        deleteButton.isEnabled = false
        progressBar = rootView.findViewById(R.id.progress_bar_plane_data)
        progressBar.visibility = View.VISIBLE

        return rootView
    }

    @Suppress("UNNECESSARY_SAFE_CALL")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.plane.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "(Observed) Plane result loaded")
            if (it is Result.Success) {
                Log.d(TAG, "Plane loaded successfully")
                if (planeAlreadyExists) {
                    sharedViewModel.planeUpdated()
                }
                plane = it.data
                planeAlreadyExists = true
                setTextViewsToPlaneData()
                locationAndCancelButton.isEnabled = true
                updateButton.isEnabled = true
                deleteButton.isEnabled = true
                locationAndCancelButton.text = getString(R.string.geolocation_button)
                updateHintTextView.visibility = View.GONE
                updateMode = false
            } else if (it is Result.Error) {
                Log.e(TAG, "Error Result received")
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
            progressBar.visibility = View.GONE
        })
        viewModel.planeDeletion.observe(viewLifecycleOwner, Observer {
            if (it is Result.Success) {
                Log.d(TAG, "Plane deletion successful, view model notification")
                sharedViewModel.planeDeleted()
                Log.d(TAG, "Toast message")
                Toast.makeText(context, "Plane deleted", Toast.LENGTH_LONG).show()
                Log.d(TAG, "POP BACK STACK")
                findNavController().popBackStack()
            } else if (it is Result.Error) {
                Log.d(TAG, "Plane deletion failed, showing toast message")
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                locationAndCancelButton.isEnabled = true
                updateButton.isEnabled = true
                deleteButton.isEnabled = true
                progressBar.visibility = View.GONE
            }
        })
    }

    override fun onStart() {
        super.onStart()
        locationAndCancelButton.setOnClickListener {
            if (!updateMode) {
                openGoogleMapsIntent()
            } else {
                cancelUpdate()
            }
        }
        updateButton.setOnClickListener {
            if (!updateMode) {
                startUpdate()
            } else {
                updatePlane()
            }
        }
        deleteButton.setOnClickListener {
            showDeletionDialog(plane.tailNumber) {
                locationAndCancelButton.isEnabled = false
                updateButton.isEnabled = false
                deleteButton.isEnabled = false
                progressBar.visibility = View.VISIBLE
                plane.tailNumber?.let {
                    viewModel.deletePlane(it)
                }
            }

        }
        tailNumberTextView.setOnClickListener {
            if (updateMode) {
                showEditTextDialogForResult(
                    tailNumberTextView.text.toString(),
                    getString(R.string.tail_number),
                    getString(R.string.invalid_tail_number),
                    tailNumberTextView
                ) {
                    planeValidator.isTailNumberValid(it)
                }
            }
        }
        brandTextView.setOnClickListener {
            if (updateMode) {
                showPickerDialogForResult(
                    brandTextView.text.toString(),
                    getString(R.string.brand),
                    Plane.BrandList,
                    brandTextView
                )
            }
        }
        modelTextView.setOnClickListener {
            if (updateMode) {
                showEditTextDialogForResult(
                    modelTextView.text.toString(),
                    getString(R.string.model),
                    getString(R.string.invalid_model),
                    modelTextView
                ) {
                    planeValidator.isModelValid(it)
                }
            }
        }
        yearTextView.setOnClickListener {
            if (updateMode) {
                showEditTextDialogForResult(
                    yearTextView.text.toString(),
                    getString(R.string.year),
                    getString(R.string.invalid_fabrication_year),
                    yearTextView,
                    InputType.TYPE_CLASS_NUMBER
                ) {
                    planeValidator.isFabricationYearValid(it)
                }
            }
        }
        engineTextView.setOnClickListener {
            if (updateMode) {
                showPickerDialogForResult(
                    engineTextView.text.toString(),
                    getString(R.string.engine),
                    Plane.EngineList,
                    engineTextView
                )
            }
        }
        priceTextView.setOnClickListener {
            if (updateMode) {
                showEditTextDialogForResult(
                    priceTextView.text.toString(),
                    getString(R.string.price),
                    getString(R.string.invalid_price),
                    priceTextView,
                    InputType.TYPE_CLASS_NUMBER
                ) {
                    planeValidator.isPriceValid(it)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        updateButton.setOnClickListener(null)
        locationAndCancelButton.setOnClickListener(null)
        deleteButton.setOnClickListener(null)
    }

    private fun showDeletionDialog(tailNumber: String, yesFunction: () -> Unit) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("Delete plane $tailNumber")
        dialogBuilder.setMessage("Are you sure you want to delete this plane?")
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            if (which == DialogInterface.BUTTON_POSITIVE) {
                yesFunction()
            }
        }
        dialogBuilder.setPositiveButton("YES", dialogClickListener)
        dialogBuilder.setNegativeButton("NO", dialogClickListener)
        dialogBuilder.create().show()
    }

    private fun openGoogleMapsIntent() {
        val gmmIntentUri = Uri.parse("geo:53.4269764,-6.2593187")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        try {
            Log.i(TAG, "Checking that com.google.android.apps.maps package is installed")
            if (mapIntent.resolveActivity(context!!.packageManager) != null) {
                Log.i(
                    TAG,
                    "com.google.android.apps.maps package is installed; starting activity"
                )
                startActivity(mapIntent)
            } else {
                Log.w(TAG, "com.google.android.apps.maps not installed")
                Toast.makeText(
                    context,
                    "Google Maps not found on device",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error occurred while starting Google Maps intent: ${e.message}")
            Toast.makeText(
                context,
                "An error occurred while opening Google Maps",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun showEditTextDialogForResult(
        initialValue: String,
        title: String,
        conditions: String,
        destinationTextView: TextView,
        inputType: Int = InputType.TYPE_CLASS_TEXT,
        validator: (String) -> Boolean
    ) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage(conditions)
        val editText = EditText(context)
        editText.inputType = inputType
        editText.setText(initialValue)
        dialogBuilder.setView(editText)
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            if (which == DialogInterface.BUTTON_POSITIVE) {
                if (validator(editText.text.toString())) {
                    destinationTextView.text = editText.text
                } else {
                    Toast.makeText(context, conditions, Toast.LENGTH_LONG).show()
                }
            }
        }
        dialogBuilder.setPositiveButton("CHANGE", dialogClickListener)
        dialogBuilder.setNegativeButton("CANCEL", dialogClickListener)
        dialogBuilder.create().show()
    }

    private fun showPickerDialogForResult(
        initialValue: String,
        title: String,
        choices: List<String>,
        destinationTextView: TextView
    ) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle(title)
        val picker = NumberPicker(context)
        picker.minValue = 0
        picker.maxValue = choices.size - 1
        picker.displayedValues = choices.toTypedArray()
        dialogBuilder.setView(picker)
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            if (which == DialogInterface.BUTTON_POSITIVE) {
                destinationTextView.text = picker.displayedValues[picker.value]
            }
        }
        dialogBuilder.setPositiveButton("CHANGE", dialogClickListener)
        dialogBuilder.setNegativeButton("CANCEL", dialogClickListener)
        dialogBuilder.create().show()
    }

    private fun setTextViewsToPlaneData() {
        tailNumberTextView.text = plane.tailNumber
        brandTextView.text = plane.brand?.toString()
        modelTextView.text = plane.model
        engineTextView.text = plane.engine?.toString()
        yearTextView.text = plane.fabricationYear?.toString()
        priceTextView.text = plane.price?.toString()
    }

    private fun startUpdate() {
        updateMode = true
        deleteButton.isEnabled = false
        locationAndCancelButton.text = getString(R.string.cancel_button)
        updateHintTextView.visibility = View.VISIBLE
    }

    private fun cancelUpdate() {
        setTextViewsToPlaneData()
        deleteButton.isEnabled = true
        locationAndCancelButton.text = getString(R.string.geolocation_button)
        updateHintTextView.visibility = View.GONE
        updateMode = false
    }

    private fun updatePlane() {
        try {
            val newPlane = Plane(
                tailNumberTextView.text.toString(),
                Plane.Brand.valueOf(brandTextView.text.toString()),
                modelTextView.text.toString(),
                yearTextView.text.toString().toInt(),
                Plane.Engine.valueOf(engineTextView.text.toString()),
                priceTextView.text.toString().toLong()
            )
            updateMode = false
            progressBar.visibility = View.VISIBLE
            locationAndCancelButton.isEnabled = false
            updateButton.isEnabled = false
            viewModel.updatePlane(newPlane)
        } catch (e: Exception) {
            Log.e(TAG, "Error occurred: ${e.message}")
            updateMode = true
            progressBar.visibility = View.GONE
            locationAndCancelButton.isEnabled = true
            updateButton.isEnabled = true
        }
    }
}
