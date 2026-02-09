package com.example.finalss_gargarmiranda

import android.content.Context
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView

object NavigationHelper {

    fun setupBottomNavigation(context: Context, bottomNavigationView: BottomNavigationView, currentItemId: Int) {
        if (currentItemId != 0) {
            bottomNavigationView.selectedItemId = currentItemId
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            if (currentItemId != 0 && item.itemId == currentItemId) {
                return@setOnItemSelectedListener true
            }

            val intent = when (item.itemId) {
                R.id.navigation_notifications -> Intent(context, NotificationActivity::class.java)
                R.id.navigation_home -> Intent(context, HomeActivity::class.java)
                R.id.navigation_profile -> Intent(context, ProfileActivity::class.java)
                else -> null
            }

            intent?.let {
                it.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                context.startActivity(it)
            }

            true
        }
    }
}