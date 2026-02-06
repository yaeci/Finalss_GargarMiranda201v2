package com.example.finalss_gargarmiranda

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class ReserveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reserve)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.roomScrollView)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        NavigationHelper.setupBottomNavigation(this, bottomNav, 0)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val PEArea = findViewById<MaterialButton>(R.id.BTN_peArea)
        val AVR = findViewById<MaterialButton>(R.id.BTN_AVR)
        val caseRoom = findViewById<MaterialButton>(R.id.BTN_caseRoom)
        val LRC1 = findViewById<MaterialButton>(R.id.BTN_LRCD1)
        val LRC2 = findViewById<MaterialButton>(R.id.BTN_LRCD2)
        val sportsComplex = findViewById<MaterialButton>(R.id.BTN_sportsComplex)
        val computerLab = findViewById<MaterialButton>(R.id.BTN_computerLab)
        val others = findViewById<EditText>(R.id.ET_Others)

        val buttons = listOf(PEArea, AVR, caseRoom, LRC1, LRC2, sportsComplex, computerLab)

        buttons.forEach { button ->
            button.setOnClickListener {
                val intent = Intent(this, FillUpActivity::class.java)
                intent.putExtra("FACILITY_NAME", button.text.toString())
                startActivity(intent)
            }
        }
    }
}