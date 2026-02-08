package com.example.finalss_gargarmiranda

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotificationActivity : AppCompatActivity() {

    private lateinit var notificationRecyclerView: RecyclerView
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var emptyStateTextView: TextView
    private val notificationList = mutableListOf<Reservation>()

    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        // Setup Bottom Navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        NavigationHelper.setupBottomNavigation(this, bottomNav, R.id.navigation_notifications)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }

        emptyStateTextView = findViewById(R.id.emptyStateTextView)
        notificationRecyclerView = findViewById(R.id.notificationRecyclerView)
        notificationRecyclerView.layoutManager = LinearLayoutManager(this)
        notificationAdapter = NotificationAdapter(notificationList)
        notificationRecyclerView.adapter = notificationAdapter

        fetchNotifications()
    }

    private fun fetchNotifications() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            firestore.collection("reservations")
                .whereEqualTo("submittedBy.email", currentUser.email)
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.w("Notifications", "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    notificationList.clear()
                    for (doc in snapshots!!) {
                        val status = doc.getString("status") ?: ""
                        // Only show notifications for Approved or Denied reservations
                        if (status.lowercase() == "approved" || status.lowercase() == "denied") {
                            val submittedBy = doc.get("submittedBy") as? Map<*, *>
                            val activityDetails = doc.get("activityDetails") as? Map<*, *>
                            val reservationSlot = doc.get("reservationSlot") as? Map<*, *>

                            val reservation = Reservation(
                                id = doc.id,
                                facility = doc.getString("facilityName") ?: "",
                                date = reservationSlot?.get("date")?.toString() ?: "",
                                time = reservationSlot?.get("time")?.toString() ?: "",
                                reservedBy = submittedBy?.get("name")?.toString() ?: "",
                                position = submittedBy?.get("position")?.toString() ?: "",
                                title = activityDetails?.get("title")?.toString() ?: "",
                                attendees = activityDetails?.get("attendees")?.toString() ?: "",
                                speaker = activityDetails?.get("speaker")?.toString() ?: "",
                                status = status
                            )
                            notificationList.add(reservation)
                        }
                    }

                    if (notificationList.isEmpty()) {
                        emptyStateTextView.visibility = View.VISIBLE
                        notificationRecyclerView.visibility = View.GONE
                    } else {
                        emptyStateTextView.visibility = View.GONE
                        notificationRecyclerView.visibility = View.VISIBLE
                    }

                    notificationAdapter.notifyDataSetChanged()
                }
        }
    }
}