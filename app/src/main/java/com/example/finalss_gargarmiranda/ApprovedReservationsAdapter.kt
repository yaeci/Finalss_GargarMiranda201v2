package com.example.finalss_gargarmiranda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ApprovedReservationsAdapter(
    private val reservations: List<Reservation>,
    private val listener: OnReservationClickListener
) : RecyclerView.Adapter<ApprovedReservationsAdapter.ViewHolder>() {

    interface OnReservationClickListener {
        fun onReservationClick(reservation: Reservation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_approved_reservation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reservation = reservations[position]
        holder.bind(reservation)
    }

    override fun getItemCount(): Int = reservations.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val facilityTextView: TextView = itemView.findViewById(R.id.facilityTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        private val reservedByTextView: TextView = itemView.findViewById(R.id.reservedByTextView)

        fun bind(reservation: Reservation) {
            facilityTextView.text = reservation.facility
            dateTextView.text = reservation.date
            timeTextView.text = reservation.time
            reservedByTextView.text = reservation.reservedBy

            itemView.setOnClickListener {
                listener.onReservationClick(reservation)
            }
        }
    }
}