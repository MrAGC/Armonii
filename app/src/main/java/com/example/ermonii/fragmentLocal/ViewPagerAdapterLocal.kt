package com.example.ermonii.fragmentLocal

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapterLocal(fragmentActivity: FragmentActivity, private val usuarioId: Int) :
    FragmentStateAdapter(fragmentActivity) {

    // Lista de fragmentos que se mostrarán en el ViewPager2
    private val fragments = listOf(
        HomeFragmentLocal(), CrearEventoFragmentLocal(), ChatFragmentLocal(), PerfilFragmentLocal(usuarioId)
                                  )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}