package com.example.ermonii.FragmentMusico

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapterMusico(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    // Lista de fragmentos que se mostrarán en el ViewPager2
    private val fragments = listOf(
        HomeFragmentMusico(),
        MapaFragmentMusico(),
        ChatFragmentMusico(),
        PerfilFragmentMusico()
                                  )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}