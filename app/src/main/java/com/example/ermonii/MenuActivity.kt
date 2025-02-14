package com.example.ermonii

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView

class MenuActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var navegation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Inicializar ViewPager2
        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = ViewPagerAdapter(this)

        // Inicializar BottomNavigationView
        navegation = findViewById(R.id.navMenu)
        navegation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.itemHomeFragment -> {
                    viewPager.currentItem = 0
                    true
                }
                R.id.itemMapaFragment -> {
                    viewPager.currentItem = 1
                    true
                }
                R.id.itemChatFragment -> {
                    viewPager.currentItem = 2
                    true
                }
                R.id.itemPerfilFragment -> {
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
                    0 -> navegation.selectedItemId = R.id.itemHomeFragment
                    1 -> navegation.selectedItemId = R.id.itemMapaFragment
                    2 -> navegation.selectedItemId = R.id.itemChatFragment
                    3 -> navegation.selectedItemId = R.id.itemPerfilFragment
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