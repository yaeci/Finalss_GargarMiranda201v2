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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ReportListActivity : AppCompatActivity() {

    private lateinit var reportRecyclerView: RecyclerView
    private lateinit var reportAdapter: ReportAdapter
    private lateinit var emptyStateTextView: TextView
    private val reportList = mutableListOf<Report>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_list)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        NavigationHelper.setupBottomNavigation(this, bottomNav, 0)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }

        emptyStateTextView = findViewById(R.id.emptyStateTextView)
        reportRecyclerView = findViewById(R.id.reportRecyclerView)
        reportRecyclerView.layoutManager = LinearLayoutManager(this)
        reportAdapter = ReportAdapter(reportList)
        reportRecyclerView.adapter = reportAdapter

        fetchReports()
    }

    private fun fetchReports() {
        db.collection("reports")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("ReportList", "Listen failed.", e)
                    return@addSnapshotListener
                }

                reportList.clear()
                for (doc in snapshots!!) {
                    val report = doc.toObject(Report::class.java)
                    report.id = doc.id
                    reportList.add(report)
                }

                if (reportList.isEmpty()) {
                    emptyStateTextView.visibility = View.VISIBLE
                    reportRecyclerView.visibility = View.GONE
                } else {
                    emptyStateTextView.visibility = View.GONE
                    reportRecyclerView.visibility = View.VISIBLE
                }

                reportAdapter.notifyDataSetChanged()
            }
    }
}