package com.example.finalss_gargarmiranda

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        val FirstName = findViewById<EditText>(R.id.ET_FirstName)
        val LastName = findViewById<EditText>(R.id.ET_LastName)
        val Email = findViewById<EditText>(R.id.ET_EmailSignup)
        val Password = findViewById<EditText>(R.id.ET_PassowordSignup)
        val Register = findViewById<Button>(R.id.BTN_Register)
        val BackLogin = findViewById<TextView>(R.id.TV_BackLogin)
        val Username = findViewById<EditText>(R.id.ET_UsernameSignup)


        Register.setOnClickListener {

            val firstName = FirstName.text.toString().trim()
            val lastName = LastName.text.toString().trim()
            val username = Username.text.toString().trim()
            val email = Email.text.toString().trim()
            val password = Password.text.toString().trim()

            if (firstName.isEmpty() || lastName.isEmpty() ||
                username.isEmpty() || email.isEmpty() || password.isEmpty()
            ) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {

                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                    // Go back to Login screen
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }

            BackLogin.setOnClickListener {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)

            }
        }
    }