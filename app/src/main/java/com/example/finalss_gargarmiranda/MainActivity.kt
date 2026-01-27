package com.example.finalss_gargarmiranda

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        val emailET = findViewById<EditText>(R.id.ET_Username)
        val passwordET = findViewById<EditText>(R.id.ET_Password)
        val Username = findViewById<EditText>(R.id.ET_Username)
        val login = findViewById<Button>(R.id.BTN_Login)
        val Signup = findViewById<Button>(R.id.BTN_Signup)

        login.setOnClickListener {

            val email = emailET.text.toString().trim()
            val password = passwordET.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val i = Intent(this, HomeActivity::class.java)
                    startActivity(i)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Login failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        Signup.setOnClickListener {
            val i = Intent(this, SignupActivity::class.java)
            startActivity(i)
        }
    }
}