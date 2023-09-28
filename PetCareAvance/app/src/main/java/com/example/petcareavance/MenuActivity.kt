package com.example.petcareavance

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.petcareavance.Fragments.InicioFragment
import com.example.petcareavance.Fragments.PerfilFragment
import com.example.petcareavance.Fragments.PublicarFragment
import com.example.petcareavance.Fragments.SoporteFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, InicioFragment()).commit()
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val selectedFragment: Fragment = when (item.itemId) {
            R.id.nav_inicio -> InicioFragment()
            R.id.nav_publicar -> PublicarFragment()
            R.id.nav_soporte -> SoporteFragment()
            R.id.nav_perfil -> PerfilFragment()
            else -> InicioFragment()
        }

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
        true
    }
}