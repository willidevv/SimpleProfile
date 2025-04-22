package com.example.simpleaplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class updateseasonActivity : AppCompatActivity() {

    private lateinit var namaUpdate: EditText
    private lateinit var emailUpdate: EditText
    private lateinit var passwordUpdate: EditText
    private lateinit var nomorhpUpdate: EditText
    private lateinit var alamatUpdate: EditText
    private lateinit var updateBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_updateseason)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userId = intent.getIntExtra("USER_ID", -1)
        if(userId == -1) {
            Toast.makeText(this, "user tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        namaUpdate = findViewById(R.id.nama_update)
        emailUpdate = findViewById(R.id.email_update)
        passwordUpdate = findViewById(R.id.password_update)
        nomorhpUpdate = findViewById(R.id.nomorhp_update)
        alamatUpdate = findViewById(R.id.alamat_update)
        updateBtn = findViewById(R.id.update)

        val db = profiledatabase.getDatabase(this)

        lifecycleScope.launch {
            val user = withContext(Dispatchers.IO) {
                db.ProfileDao().getUserById(userId)
            }
            if (user != null) {
                namaUpdate.setText(user.nama)
                emailUpdate.setText(user.email)
                passwordUpdate.setText(user.password)
                nomorhpUpdate.setText(user.nohp)
                alamatUpdate.setText(user.alamat)
            } else {
                Toast.makeText(this@updateseasonActivity, "Data user tidak ditemukan", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        updateBtn.setOnClickListener{
            val updatedUSer = ProfileDataclass(
                id = userId,
                nama = namaUpdate.text.toString(),
                email = emailUpdate.text.toString(),
                password = passwordUpdate.text.toString(),
                nohp = nomorhpUpdate.text.toString(),
                alamat = alamatUpdate.text.toString()
            )

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    db.ProfileDao().updateUser(updatedUSer)
                }
                Toast.makeText(this@updateseasonActivity, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@updateseasonActivity, profileActivity::class.java)
                intent.putExtra("USER_ID", userId)
                startActivity(intent)
                finish()
            }
        }
    }
}