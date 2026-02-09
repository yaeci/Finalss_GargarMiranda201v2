package com.example.finalss_gargarmiranda

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val tvUsernameDisplay = findViewById<TextView>(R.id.tv_username_display)
        val llPending = findViewById<LinearLayout>(R.id.ll_pending)
        val btnPending = findViewById<ImageButton>(R.id.btn_pending_reservations)
        val tvReportLabel = findViewById<TextView>(R.id.tv_report_label)
        var currentUserType: String? = null

        // Check user type from Firestore
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val firstName = document.getString("firstName")
                        val userType = document.getString("userType")
                        currentUserType = userType

                        tvUsernameDisplay.text = firstName ?: "User"

                        if (userType == "Faculty") {
                            llPending.visibility = View.VISIBLE
                            tvReportLabel.text = "LIST OF\nREPORTS"
                        } else {
                            llPending.visibility = View.INVISIBLE
                            tvReportLabel.text = "REPORT\nFACILITY"
                        }
                    }
                }
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        NavigationHelper.setupBottomNavigation(this, bottomNav, R.id.navigation_home)

        val btnReserve: ImageButton = findViewById(R.id.btn_reserve)
        btnReserve.setOnClickListener {
            val intent = Intent(this, ReserveActivity::class.java)
            startActivity(intent)
        }

        val btnList: ImageButton = findViewById(R.id.btn_reservation_list)
        btnList.setOnClickListener {
            val intent = Intent(this, ReservationListActivity::class.java)
            startActivity(intent)
        }

        val btnReport: ImageButton = findViewById(R.id.btn_report_facility)
        btnReport.setOnClickListener {
            if (currentUserType == "Faculty") {
                // Navigate to List of Reports Activity (to be created)
                val intent = Intent(this, ReportListActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, ReportFacilityActivity::class.java)
                startActivity(intent)
            }
        }

        btnPending.setOnClickListener {
            val intent = Intent(this, PendingReservationsActivity::class.java)
            startActivity(intent)
        }
    }
}