package com.example.finalss_gargarmiranda

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FillUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_up)

        val facilityName = intent.getStringExtra("FACILITY_NAME")

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        NavigationHelper.setupBottomNavigation(this, bottomNav, 0)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val nameEditText = findViewById<EditText>(R.id.ETFillUp_Name)
        val positionEditText = findViewById<EditText>(R.id.ETFillUp_Position)
        val titleEditText = findViewById<EditText>(R.id.ETFillUp_Title)
        val datesEditText = findViewById<EditText>(R.id.ETFillUp_Dates)
        val daysEditText = findViewById<EditText>(R.id.ETFillUp_Days)
        val timeFromEditText = findViewById<EditText>(R.id.ETFillUp_TimeFrom)
        val timeToEditText = findViewById<EditText>(R.id.ETFillUp_TimeTo)
        val attendeesEditText = findViewById<EditText>(R.id.ETFillUp_Attendees)
        val speakerEditText = findViewById<EditText>(R.id.ETFillUp_Speaker)
        val submitButton = findViewById<Button>(R.id.BTN_Submit)

        datesEditText.isFocusable = false
        datesEditText.isClickable = true
        datesEditText.setOnClickListener {
            showDatePickerDialog(datesEditText)
        }

        timeFromEditText.isFocusable = false
        timeFromEditText.isClickable = true
        timeFromEditText.setOnClickListener {
            showTimePickerDialog(timeFromEditText)
        }

        timeToEditText.isFocusable = false
        timeToEditText.isClickable = true
        timeToEditText.setOnClickListener {
            showTimePickerDialog(timeToEditText)
        }

        submitButton.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser == null) {
                Toast.makeText(this, "You must be logged in to make a reservation.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val name = nameEditText.text.toString().trim()
            val position = positionEditText.text.toString().trim()
            val title = titleEditText.text.toString().trim()
            val date = datesEditText.text.toString().trim()
            val days = daysEditText.text.toString().trim()
            val timeFrom = timeFromEditText.text.toString().trim()
            val timeTo = timeToEditText.text.toString().trim()
            val attendees = attendeesEditText.text.toString().trim()

            if (name.isEmpty() || position.isEmpty() || title.isEmpty() || date.isEmpty() || days.isEmpty() || timeFrom.isEmpty() || timeTo.isEmpty() || attendees.isEmpty()) {
                Toast.makeText(this, "Please fill up all required fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val time = "$timeFrom - $timeTo"

            if (facilityName != null) {
                ReservationRepo.isReservationExisting(facilityName, date, time) { isExisting ->
                    if (isExisting) {
                        Toast.makeText(this, "This facility is already reserved or pending at the selected date and time.", Toast.LENGTH_SHORT).show()
                    } else {
                        val reservationDetails = hashMapOf(
                            "facilityName" to facilityName,
                            "status" to "pending",
                            "submittedBy" to hashMapOf(
                                "name" to name,
                                "position" to position,
                                "email" to currentUser.email
                            ),
                            "activityDetails" to hashMapOf(
                                "title" to title,
                                "attendees" to attendees,
                                "speaker" to speakerEditText.text.toString().trim()
                            ),
                            "reservationSlot" to hashMapOf(
                                "date" to date,
                                "days" to days,
                                "time" to time
                            )
                        )

                        ReservationRepo.addReservation(reservationDetails)

                        Toast.makeText(this, "Reservation submitted for approval!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Error: Facility name not found.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            val sdf = SimpleDateFormat("dd/MM/yy", Locale.US)
            editText.setText(sdf.format(selectedDate.time))
        }, year, month, day)
        datePickerDialog.show()
    }

    private fun showTimePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val selectedTime = Calendar.getInstance()
            selectedTime.set(Calendar.HOUR_OF_DAY, selectedHour)
            selectedTime.set(Calendar.MINUTE, selectedMinute)
            val sdf = SimpleDateFormat("hh:mma", Locale.US)
            editText.setText(sdf.format(selectedTime.time).lowercase())
        }, hour, minute, false)
        timePickerDialog.show()
    }
}
