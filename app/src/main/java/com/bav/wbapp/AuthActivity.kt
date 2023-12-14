package com.bav.wbapp

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bav.core.CommonActivity
import com.bav.core.ToolbarActivity

class AuthActivity :
    CommonActivity(R.layout.activity_auth, R.id.nav_host_fragment),
    ToolbarActivity
{
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun setupToolbar(title: String?) {
        val toolbar = findViewById<Toolbar>(R.id.auth_toolbar)
        toolbar.navigationIcon = applicationContext.getDrawable(com.bav.core.R.drawable.back_chevron)
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = title?.let {
            title
        } ?: getNavController().currentDestination?.label
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            if (getNavController().currentDestination?.id == getNavController().graph.startDestinationId) {
                finish()
            } else {
                getNavController().navigateUp()
            }
        }

        if (supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
    }
}