package com.example.ermonii.FragmentMusico

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.ermonii.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MenuActivityMusico : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var navegation: BottomNavigationView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_musico)

        // Inicializar ViewPager2
        viewPager = findViewById(R.id.viewPagerMusico)
        viewPager.adapter = ViewPagerAdapterMusico(this)

        // Inicializar BottomNavigationView
        navegation = findViewById(R.id.navMenu)
        navegation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.itemHomeFragmentMusico -> {
                    viewPager.currentItem = 0
                    true
                }
                R.id.itemMapaFragmentMusico -> {
                    viewPager.currentItem = 1
                    true
                }
                R.id.itemChatFragmentMusico -> {
                    viewPager.currentItem = 2
                    true
                }
                R.id.itemPerfilFragmentMusico -> {
                    viewPager.currentItem = 3
                    true
                }
                else -> false
            }
        }

        // Sincronizar ViewPager2 con BottomNavigationView
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> navegation.selectedItemId = R.id.itemHomeFragmentMusico
                    1 -> navegation.selectedItemId = R.id.itemMapaFragmentMusico
                    2 -> navegation.selectedItemId = R.id.itemChatFragmentMusico
                    3 -> navegation.selectedItemId = R.id.itemPerfilFragmentMusico
                }
            }
        })
    }

    // Métodos para habilitar/deshabilitar gestos de ViewPager2
    fun disableViewPagerGestures() {
        viewPager.isUserInputEnabled = false
    }

    fun enableViewPagerGestures() {
        viewPager.isUserInputEnabled = true
    }

}