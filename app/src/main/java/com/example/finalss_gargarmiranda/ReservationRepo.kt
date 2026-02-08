package com.example.finalss_gargarmiranda

import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

object ReservationRepo {

    private val db = FirebaseFirestore.getInstance()

    fun addReservation(reservation: Map<String, Any>) {
        db.collection("reservations").add(reservation)
    }

    fun isReservationExisting(
        facility: String,
        date: String,
        time: String,
        callback: (Boolean) -> Unit
    ) {
        db.collection("reservations")
            .whereEqualTo("facilityName", facility)
            .whereEqualTo("reservationSlot.date", date)
            .whereIn("status", listOf("approved", "pending"))
            .get()
            .addOnSuccessListener { documents ->
                try {
                    val newTime = parseTimeRange(time)

                    for (document in documents) {
                        val existingTimeStr = document.getString("reservationSlot.time") ?: continue
                        val existingTime = parseTimeRange(existingTimeStr)

                        if (newTime.first.before(existingTime.second) && newTime.second.after(existingTime.first)) {
                            callback(true)
                            return@addOnSuccessListener
                        }
                    }
                    callback(false)
                } catch (e: Exception) {
                    callback(false)
                }
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    private fun parseTimeRange(timeRange: String): Pair<java.util.Date, java.util.Date> {
        val parts = timeRange.split(" - ")
        val sdf = SimpleDateFormat("hh:mma", Locale.US)
        return Pair(sdf.parse(parts[0]), sdf.parse(parts[1]))
    }
}