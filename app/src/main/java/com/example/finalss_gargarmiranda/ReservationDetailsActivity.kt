package com.example.finalss_gargarmiranda

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ReservationDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_details)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }

        val facility = intent.getStringExtra("facility")
        val reservedBy = intent.getStringExtra("reservedBy")
        val position = intent.getStringExtra("position")
        val title = intent.getStringExtra("title")
        val date = intent.getStringExtra("date")
        val time = intent.getStringExtra("time")
        val attendees = intent.getStringExtra("attendees")
        val speaker = intent.getStringExtra("speaker")
        val status = intent.getStringExtra("status")

        val reservedByTextView = findViewById<TextView>(R.id.reservedByTextView)
        if (status == "approved") {
            reservedByTextView.text = "Reserved by: $reservedBy"
        } else {
            reservedByTextView.text = "Submitted by: $reservedBy"
        }

        findViewById<TextView>(R.id.facilityTextView).text = facility
        findViewById<TextView>(R.id.positionTextView).text = "Position: $position"
        findViewById<TextView>(R.id.titleTextView).text = "Title: $title"
        findViewById<TextView>(R.id.dateTextView).text = "Date: $date"
        findViewById<TextView>(R.id.timeTextView).text = "Time: $time"
        findViewById<TextView>(R.id.attendeesTextView).text = "Attendees: $attendees"
        findViewById<TextView>(R.id.speakerTextView).text = "Speaker: $speaker"
        findViewById<TextView>(R.id.statusTextView).text = "Status: $status"
    }
}