package com.example.finalss_gargarmiranda

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ReportFacilityActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_report_facility)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        
        val mainView = findViewById<android.view.View>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        // Setup Bottom Navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        NavigationHelper.setupBottomNavigation(this, bottomNav, 0)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val etIssueDescription = findViewById<EditText>(R.id.ET_IssueDescription)
        val btnSubmit = findViewById<MaterialButton>(R.id.BTN_SubmitReport)

        btnSubmit.setOnClickListener {
            val description = etIssueDescription.text.toString().trim()

            if (description.isEmpty()) {
                Toast.makeText(this, "Please describe the issue", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentUser = auth.currentUser
            if (currentUser != null) {
                // Get user's name from Firestore first
                firestore.collection("users").document(currentUser.uid).get()
                    .addOnSuccessListener { document ->
                        val firstName = document.getString("firstName") ?: "Anonymous"
                        val lastName = document.getString("lastName") ?: ""
                        val fullName = "$firstName $lastName".trim()

                        val report = hashMapOf(
                            "description" to description,
                            "reportedBy" to fullName,
                            "timestamp" to System.currentTimeMillis()
                        )

                        firestore.collection("reports").add(report)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Report submitted successfully", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to submit report", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to get user data", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
