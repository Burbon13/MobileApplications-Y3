package com.burbon13.planesmanager.planes.ui.plane


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.burbon13.planesmanager.R
import com.burbon13.planesmanager.core.Result
import com.burbon13.planesmanager.core.utils.extensions.TAG
import kotlinx.android.synthetic.main.fragment_plane_data.*
import java.lang.Exception


class PlaneDataFragment : Fragment() {
    private lateinit var viewModel: PlaneDataViewModel
    private val args: PlaneDataFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PlaneDataViewModel::class.java)
        viewModel.loadPlane(args.planeTailNumber)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_plane_data, container, false)

        rootView.findViewById<Button>(R.id.geolocation_button).isEnabled = false
        rootView.findViewById<Button>(R.id.update_button).isEnabled = false
        rootView.findViewById<Button>(R.id.button_delete_plane).isEnabled = false
        rootView.findViewById<ProgressBar>(R.id.progress_bar_plane_data).visibility = View.VISIBLE

        return rootView
    }

    @Suppress("UNNECESSARY_SAFE_CALL")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.plane.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "(Observed) Plane result loaded")
            if (it is Result.Success) {
                Log.d(TAG, "Plane loaded successfully")
                val plane = it.data
                tail_number_value.text = plane.tailNumber
                brand_value.text = plane.brand?.toString()
                model_value.text = plane.model
                engine_value.text = plane.engine?.toString()
                year_value.text = plane.fabricationYear?.toString()
                price_value.text = plane.price?.toString()
                geolocation_button.isEnabled = true
                update_button.isEnabled = true
                button_delete_plane.isEnabled = true
            } else if (it is Result.Error) {
                Log.e(TAG, "Error Result received")
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
            progress_bar_plane_data.visibility = View.GONE
        })
    }

    override fun onStart() {
        super.onStart()
        geolocation_button.setOnClickListener {
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
        update_button.setOnClickListener {

        }
        button_delete_plane.setOnClickListener {

        }
    }

    override fun onStop() {
        super.onStop()
        update_button.setOnClickListener(null)
        geolocation_button.setOnClickListener(null)
        button_delete_plane.setOnClickListener(null)
    }
}
