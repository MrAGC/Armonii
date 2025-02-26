package com.example.ermonii.FragmentLocal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ermonii.R

class CrearEventoFragmentLocal : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                             ): View? {
        return inflater.inflate(R.layout.fragment_crear, container, false)
    }
}