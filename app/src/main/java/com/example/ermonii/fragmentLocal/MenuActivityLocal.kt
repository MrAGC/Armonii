package com.example.ermonii.fragmentLocal

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.ermonii.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MenuActivityLocal : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var navigation: BottomNavigationView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_local)


        // Obtenemos el usuario que ha iniciado sesión
        val usuarioId = intent.getIntExtra("usuarioId", -1)

        // Inicializar ViewPager2
        viewPager = findViewById(R.id.viewPagerLocal)
        viewPager.adapter = ViewPagerAdapterLocal(this, usuarioId)

        // Inicializar BottomNavigationView
        navigation = findViewById(R.id.navMenu)
        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.itemHomeFragmentLocal -> {
                    viewPager.currentItem = 0
                    true
                }

                R.id.itemCrearFragmentLocal -> {
                    viewPager.currentItem = 1
                    true
                }

                R.id.itemChatFragmentLocal -> {
                    viewPager.currentItem = 2
                    true
                }

                R.id.itemPerfilFragmentLocal -> {
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
                    0 -> navigation.selectedItemId = R.id.itemHomeFragmentLocal
                    1 -> navigation.selectedItemId = R.id.itemCrearFragmentLocal
                    2 -> navigation.selectedItemId = R.id.itemChatFragmentLocal
                    3 -> navigation.selectedItemId = R.id.itemPerfilFragmentLocal
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
