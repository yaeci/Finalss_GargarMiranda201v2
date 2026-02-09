package com.example.finalss_gargarmiranda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        NavigationHelper.setupBottomNavigation(this, bottomNav, R.id.navigation_profile)

        val logoutButton = findViewById<Button>(R.id.btn_logout)
        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val currentUser = auth.currentUser
        val fullNameTextView = findViewById<TextView>(R.id.tv_full_name)
        val emailTextView = findViewById<TextView>(R.id.tv_email)
        val userTypeTextView = findViewById<TextView>(R.id.tv_user_type)

        if (currentUser != null) {
            emailTextView.text = currentUser.email
            val userId = currentUser.uid
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val firstName = document.getString("firstName")
                        val lastName = document.getString("lastName")
                        val userType = document.getString("userType")

                        if (!firstName.isNullOrEmpty() && !lastName.isNullOrEmpty()) {
                            fullNameTextView.text = "$firstName $lastName".trim()
                        }

                        if (!userType.isNullOrEmpty()) {
                            userTypeTextView.text = userType
                        }
                    }
                }
        }
    }
}