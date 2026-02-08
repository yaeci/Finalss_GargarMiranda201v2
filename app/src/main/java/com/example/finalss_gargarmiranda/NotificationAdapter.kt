package com.example.finalss_gargarmiranda

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotificationAdapter(private val notifications: List<Reservation>) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = notifications[position]
        holder.bind(notification)
    }

    override fun getItemCount(): Int = notifications.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val facilityNameTextView: TextView = itemView.findViewById(R.id.facilityNameTextView)
        private val statusMessageTextView: TextView = itemView.findViewById(R.id.statusMessageTextView)

        fun bind(reservation: Reservation) {
            facilityNameTextView.text = reservation.facility
            
            val status = reservation.status.lowercase()
            if (status == "approved") {
                statusMessageTextView.text = "Reservation has been Approved"
                statusMessageTextView.setTextColor(Color.parseColor("#2ECC71")) // Green
            } else if (status == "denied") {
                statusMessageTextView.text = "Reservation has been Denied"
                statusMessageTextView.setTextColor(Color.parseColor("#E74C3C")) // Red
            } else {
                statusMessageTextView.text = "Reservation is Pending"
                statusMessageTextView.setTextColor(Color.GRAY)
            }
        }
    }
}