package com.example.ermonii.fragmentLocal

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapterLocal(
    fragmentActivity: FragmentActivity,
    private val usuarioID: Int // Añadimos parámetro para el ID de usuario
                           ) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragmentLocal()
            1 -> CrearEventoFragmentLocal()
            2 -> ChatFragmentLocal.newInstance(usuarioID) // Pasamos el ID al chat
            3 -> {
                val perfilFragment = PerfilFragmentLocal()
                val bundle = Bundle().apply {
                    putInt("usuario", usuarioID) // Pasamos el ID al perfil
                }
                perfilFragment.arguments = bundle
                perfilFragment
            }
            else -> throw IllegalArgumentException("Posición inválida: $position")
        }
    }

    override fun getItemCount(): Int = 4
}