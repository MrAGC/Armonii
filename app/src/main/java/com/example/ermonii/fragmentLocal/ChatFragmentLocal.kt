package com.example.ermonii.fragmentLocal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ermonii.R

class ChatFragmentLocal : Fragment() {

    private var usuarioId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usuarioId = arguments?.getInt("usuarioId") ?: -1
    }

    companion object {
        fun newInstance(usuarioId: Int): CrearEventoFragmentLocal {
            val fragment = CrearEventoFragmentLocal()
            val args = Bundle()
            args.putInt("usuarioId", usuarioId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
                             ): View? {

        val view = inflater.inflate(R.layout.fragment_chat_local, container, false)

        return view
    }
}