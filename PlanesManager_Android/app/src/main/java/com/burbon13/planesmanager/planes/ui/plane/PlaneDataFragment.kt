package com.burbon13.planesmanager.planes.ui.plane


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
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
    private lateinit var planeDataViewModel: PlaneDataViewModel
    private lateinit var sharedPlaneFormViewModel: SharedPlaneFormViewModel
    private lateinit var loadedPlane: Plane
    private var planeAlreadyExists = false
    private val planeValidator = PlaneValidator()

    private val args: PlaneDataFragmentArgs by navArgs()

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
        planeDataViewModel = ViewModelProviders.of(this).get(PlaneDataViewModel::class.java)
        planeDataViewModel.loadPlane(args.planeTailNumber)
        planeDataViewModel.getPlaneGeolocation(args.planeTailNumber)
        sharedPlaneFormViewModel = activity?.run {
            ViewModelProviders.of(this)[SharedPlaneFormViewModel::class.java]
        } ?: throw Exception("Unable to retrieve activity!")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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
        planeDataViewModel.plane.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "(Observed) Plane result loaded")
            if (it is Result.Success) {
                Log.d(TAG, "Plane loaded successfully")
                if (planeAlreadyExists) {
                    sharedPlaneFormViewModel.planeUpdated()
                    deleteTextViewsBackground()
                    updateMode = false
                    locationAndCancelButton.text = getString(R.string.geolocation_button)
                }
                loadedPlane = it.data
                planeAlreadyExists = true
                setTextViewsToPlaneData()
                locationAndCancelButton.isEnabled = true
                updateButton.isEnabled = true
                deleteButton.isEnabled = true
                updateHintTextView.visibility = View.GONE
            } else if (it is Result.Error) {
                Log.e(TAG, "Error Result received")
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                if (planeAlreadyExists) {
                    locationAndCancelButton.isEnabled = true
                    updateButton.isEnabled = true
                }
            }
            progressBar.visibility = View.GONE
        })
        planeDataViewModel.planeDeletionResult.observe(viewLifecycleOwner, Observer {
            if (it is Result.Success) {
                Log.d(TAG, "Plane deletion successful, view model notification")
                sharedPlaneFormViewModel.planeDeleted()
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
                when (val geolocationResult = planeDataViewModel.planeGeolocationResult.value) {
                    is Result.Success -> {
                        val geolocation = geolocationResult.data
                        openGoogleMapsIntent(geolocation.x, geolocation.y)
                    }
                    is Result.Error -> {
                        Toast.makeText(
                            context,
                            "Geolocation error: ${geolocationResult.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {
                        Toast.makeText(context, "Geolocation not loaded", Toast.LENGTH_LONG).show()
                    }
                }
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
            showDeletionDialog(loadedPlane.tailNumber) {
                locationAndCancelButton.isEnabled = false
                updateButton.isEnabled = false
                deleteButton.isEnabled = false
                progressBar.visibility = View.VISIBLE
                loadedPlane.tailNumber?.let {
                    planeDataViewModel.deletePlane(it)
                }
            }

        }
        brandTextView.setOnClickListener {
            if (updateMode) {
                showPickerDialog(
                    { loadedPlane.brand.toString() },
                    brandTextView.text.toString(),
                    getString(R.string.brand),
                    Plane.BrandList,
                    brandTextView
                )
            }
        }
        modelTextView.setOnClickListener {
            if (updateMode) {
                showEditTextDialog(
                    { loadedPlane.model },
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
                showEditTextDialog(
                    { loadedPlane.fabricationYear.toString() },
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
                showPickerDialog(
                    { loadedPlane.engine.toString() },
                    engineTextView.text.toString(),
                    getString(R.string.engine),
                    Plane.EngineList,
                    engineTextView
                )
            }
        }
        priceTextView.setOnClickListener {
            if (updateMode) {
                showEditTextDialog(
                    { loadedPlane.price.toString() },
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
        brandTextView.setOnClickListener(null)
        modelTextView.setOnClickListener(null)
        yearTextView.setOnClickListener(null)
        engineTextView.setOnClickListener(null)
        priceTextView.setOnClickListener(null)
    }

    private fun showDeletionDialog(tailNumber: String, onPositiveAction: () -> Unit) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("Delete plane $tailNumber")
        dialogBuilder.setMessage("Are you sure you want to delete this plane?")
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            if (which == DialogInterface.BUTTON_POSITIVE) {
                onPositiveAction()
            }
        }
        dialogBuilder.setPositiveButton("YES", dialogClickListener)
        dialogBuilder.setNegativeButton("NO", dialogClickListener)
        dialogBuilder.create().show()
    }

    private fun openGoogleMapsIntent(x: Double, y: Double) {
        val gmmIntentUri = Uri.parse("geo:$x,$y")
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

    private fun showEditTextDialog(
        initialValue: () -> String,
        lastValue: String,
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
        editText.setText(lastValue)
        dialogBuilder.setView(editText)
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            if (which == DialogInterface.BUTTON_POSITIVE) {
                if (validator(editText.text.toString())) {
                    val newValue = editText.text.toString()
                    destinationTextView.text = newValue
                    changeEditedViewBackground(initialValue(), newValue, destinationTextView)
                } else {
                    Toast.makeText(context, conditions, Toast.LENGTH_LONG).show()
                }
            }
        }
        dialogBuilder.setPositiveButton("CHANGE", dialogClickListener)
        dialogBuilder.setNegativeButton("CANCEL", dialogClickListener)
        dialogBuilder.create().show()
    }

    private fun showPickerDialog(
        initialValue: () -> String,
        lastValue: String,
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
        picker.value = choices.indexOf(lastValue)
        dialogBuilder.setView(picker)
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            if (which == DialogInterface.BUTTON_POSITIVE) {
                val newValue = picker.displayedValues[picker.value]
                destinationTextView.text = newValue
                changeEditedViewBackground(initialValue(), newValue, destinationTextView)
            }
        }
        dialogBuilder.setPositiveButton("CHANGE", dialogClickListener)
        dialogBuilder.setNegativeButton("CANCEL", dialogClickListener)
        dialogBuilder.create().show()
    }

    private fun setTextViewsToPlaneData() {
        tailNumberTextView.text = loadedPlane.tailNumber
        brandTextView.text = loadedPlane.brand?.toString()
        modelTextView.text = loadedPlane.model
        engineTextView.text = loadedPlane.engine?.toString()
        yearTextView.text = loadedPlane.fabricationYear?.toString()
        priceTextView.text = loadedPlane.price?.toString()
    }

    private fun startUpdate() {
        updateMode = true
        deleteButton.isEnabled = false
        locationAndCancelButton.text = getString(R.string.cancel_button)
        updateHintTextView.visibility = View.VISIBLE
        markTextViewsAsNotEdited()
    }

    private fun cancelUpdate() {
        setTextViewsToPlaneData()
        deleteButton.isEnabled = true
        locationAndCancelButton.text = getString(R.string.geolocation_button)
        updateHintTextView.visibility = View.GONE
        updateMode = false
        deleteTextViewsBackground()
    }

    private fun updatePlane() {
        retrievePlaneDataFromTextViews()?.let { newPlane ->
            progressBar.visibility = View.VISIBLE
            locationAndCancelButton.isEnabled = false
            updateButton.isEnabled = false
            planeDataViewModel.updatePlane(newPlane)
        } ?: Toast.makeText(
            context,
            "Unable to retrieve edited plane data",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun retrievePlaneDataFromTextViews(): Plane? {
        return try {
            Plane(
                tailNumberTextView.text.toString(),
                Plane.Brand.valueOf(brandTextView.text.toString()),
                modelTextView.text.toString(),
                yearTextView.text.toString().toInt(),
                Plane.Engine.valueOf(engineTextView.text.toString()),
                priceTextView.text.toString().toLong()
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun markTextViewsAsNotEdited() {
        changeToNotEditedViewBackground(brandTextView)
        changeToNotEditedViewBackground(modelTextView)
        changeToNotEditedViewBackground(yearTextView)
        changeToNotEditedViewBackground(engineTextView)
        changeToNotEditedViewBackground(priceTextView)
    }

    private fun changeToNotEditedViewBackground(view: View) {
        changeEditedViewBackground("", "", view)
    }

    private fun deleteTextViewsBackground() {
        deleteViewBackground(brandTextView)
        deleteViewBackground(modelTextView)
        deleteViewBackground(yearTextView)
        deleteViewBackground(engineTextView)
        deleteViewBackground(priceTextView)
    }

    private fun changeEditedViewBackground(
        initialValue: String = "",
        newValue: String = "",
        view: View
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (newValue != initialValue) {
                view.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.shape_edited, null)
            } else {
                view.background =
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.shape_not_edited,
                        null
                    )
            }
        }
    }

    private fun deleteViewBackground(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.background = null
        }
    }
}
