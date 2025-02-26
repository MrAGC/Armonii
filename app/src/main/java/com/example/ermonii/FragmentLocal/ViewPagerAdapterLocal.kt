package com.example.ermonii.FragmentLocal

import com.example.ermonii.FragmentMusico.ChatFragmentMusico
import com.example.ermonii.FragmentMusico.HomeFragmentMusico
import com.example.ermonii.FragmentMusico.MapaFragmentMusico
import com.example.ermonii.FragmentMusico.PerfilFragmentMusico
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapterLocal(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    // Lista de fragmentos que se mostrarán en el ViewPager2
    private val fragments = listOf(
        HomeFragmentLocal(),
        ChatFragmentLocal(),
        ChatFragmentLocal(),
        PerfilFragmentLocal()
                                  )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}