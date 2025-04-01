package com.example.ermonii.fragmentMusico

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapterMusico(
    fragmentActivity: FragmentActivity, private val usuarioID: Int) :
    FragmentStateAdapter(fragmentActivity) {

    // Metodo para crear cada fragmento con usuario cuando sea necesario
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragmentMusico()
            1 -> MapaFragmentMusico()
            2 -> ChatFragmentMusico()
            3 -> {
                val perfilFragment = PerfilFragmentMusico()
                val bundle = Bundle()
                bundle.putSerializable("usuario", usuarioID) // Pasamos el usuario
                perfilFragment.arguments = bundle
                perfilFragment
            }
            else -> throw IllegalArgumentException("Posición inválida")
        }
    }

    override fun getItemCount(): Int = 4
}
