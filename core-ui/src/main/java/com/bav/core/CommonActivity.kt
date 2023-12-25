package com.bav.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

abstract class CommonActivity(
    private val layoutId: Int,
    private val navHostId: Int
) : AppCompatActivity(), Navigation {

    private var navHostFragment: NavHostFragment? = null
    private val _navController: NavController? get() = navHostFragment?.navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        setupNavHost()
    }

     override fun onStart() {
        super.onStart()
        if (_navController == null) {
            setupNavHost()
        }
    }

    private fun setupNavHost() {
        navHostFragment = supportFragmentManager.findFragmentById(navHostId) as NavHostFragment
    }

    override fun getNavController(): NavController = _navController!!
}