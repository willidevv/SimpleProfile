package com.example.simpleaplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class profileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val database = profiledatabase.getDatabase(this)
        val userId = intent.getIntExtra("USER_ID", -1)
        if (userId == -1) {
            Toast.makeText(this, "ID tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        lifecycleScope.launch {
            val user = withContext(Dispatchers.IO) {
                database.ProfileDao().getUserById(userId)
            }

            if (user != null) {
                findViewById<TextView>(R.id.namaprofile).text = "Nama: ${user.nama}"
                findViewById<TextView>(R.id.emailprofile).text = "Email: ${user.email}"
                findViewById<TextView>(R.id.nomorhp_profile).text = "No HP: ${user.nohp}"
                findViewById<TextView>(R.id.alamatprofile).text = "Alamat: ${user.alamat}"
            } else {
                Toast.makeText(this@profileActivity, "User tidak ditemukan", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        val btnupdate = findViewById<Button>(R.id.edit_profile)
        btnupdate.setOnClickListener{
            val intent = Intent(this, updateseasonActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

    }
}