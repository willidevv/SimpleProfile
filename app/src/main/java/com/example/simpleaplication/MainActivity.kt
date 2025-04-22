package com.example.simpleaplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.simpleaplication.data.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val loginbutton = findViewById<Button>(R.id.Loginbutton)
        val emailtext = findViewById<EditText>(R.id.emaillogin)
        val passwordtext = findViewById<EditText>(R.id.passwordlogin)
        val registerButton = findViewById<Button>(R.id.RegisterButton)
        val userRepository = UserRepository(profiledatabase.getDatabase(applicationContext).ProfileDao())

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginbutton.setOnClickListener{
            val email = emailtext.text.toString().trim()
            val password = passwordtext.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            CoroutineScope(Dispatchers.IO).launch {
                val user = userRepository.getUserByEmail(email)

                runOnUiThread{
                    if (user == null || user.password != password) {
                        Toast.makeText(this@MainActivity, "akun tidak ditemukan, silahkan daftar terlebih dahulu", Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(this@MainActivity, profileActivity::class.java)
                        intent.putExtra("USER_ID", user.id)
                        startActivity(intent)
                        Log.d("ProfileActivity", "Mengirim USER_ID: ${user.id}")

                    }
                }
            }

        }
    }

}