package com.burbon13.planesmanager.planes.ui.form


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.burbon13.planesmanager.R

/**
 * A simple [Fragment] subclass.
 */
class PlaneFormFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plane_form, container, false)
    }


}
