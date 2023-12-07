package com.bav.wbapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bav.core.Navigation

class MainActivity : AppCompatActivity(), Navigation {

    private var navHostFragment: NavHostFragment? = null
    private val _navController: NavController? get() = navHostFragment?.navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    }

    override fun onStart() {
        super.onStart()
        if (_navController == null) {
            setupNavHost()
        }
    }

    private fun setupNavHost() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    }

    override fun getNavController(): NavController = _navController!!
}