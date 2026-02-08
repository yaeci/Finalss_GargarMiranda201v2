package com.example.finalss_gargarmiranda

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ReservationListActivity : AppCompatActivity(), ApprovedReservationsAdapter.OnReservationClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ApprovedReservationsAdapter
    private val reservations = mutableListOf<Reservation>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_list)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.approvedReservationsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ApprovedReservationsAdapter(reservations, this)
        recyclerView.adapter = adapter

        fetchApprovedReservations()
    }

    private fun fetchApprovedReservations() {
        db.collection("reservations")
            .whereEqualTo("status", "approved")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("ReservationList", "Listen failed.", e)
                    return@addSnapshotListener
                }

                reservations.clear()
                for (doc in snapshots!!) {
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
                        status = doc.getString("status") ?: ""
                    )

                    Log.d("ReservationList", "Parsed reservation: $reservation")
                    reservations.add(reservation)
                }

                adapter.notifyDataSetChanged()
            }
    }

    override fun onReservationClick(reservation: Reservation) {
        val intent = Intent(this, ReservationDetailsActivity::class.java)
        intent.putExtra("facility", reservation.facility)
        intent.putExtra("reservedBy", reservation.reservedBy)
        intent.putExtra("position", reservation.position)
        intent.putExtra("title", reservation.title)
        intent.putExtra("date", reservation.date)
        intent.putExtra("time", reservation.time)
        intent.putExtra("attendees", reservation.attendees)
        intent.putExtra("speaker", reservation.speaker)
        intent.putExtra("status", reservation.status)
        startActivity(intent)
    }
}