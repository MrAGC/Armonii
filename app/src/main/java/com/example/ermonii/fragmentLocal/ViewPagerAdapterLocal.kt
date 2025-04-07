package com.example.ermonii.fragmentLocal

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapterLocal(fragmentActivity: FragmentActivity, private val usuarioId: Int) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragmentLocal.newInstance(usuarioId)
            1 -> CrearEventoFragmentLocal.newInstance(usuarioId)
            2 -> ChatFragmentLocal.newInstance(usuarioId)
            3 -> PerfilFragmentLocal.newInstance(usuarioId)
            else -> throw IndexOutOfBoundsException("Invalid position $position")
        }
    }
}
