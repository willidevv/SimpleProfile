package com.example.simpleaplication

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.simpleaplication.data.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var userRepository: UserRepository
    private lateinit var etNama: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etNohp: EditText
    private lateinit var etAlamat: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val database = profiledatabase.getDatabase(applicationContext)
        userRepository = UserRepository(database.ProfileDao())

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etNama = findViewById(R.id.nama)
        etEmail = findViewById(R.id.email)
        etPassword = findViewById(R.id.password)
        etNohp = findViewById(R.id.nomorhp)
        etAlamat = findViewById(R.id.alamat)
        btnRegister = findViewById(R.id.btnregister)

        btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val nama = etNama.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val nohp = etNohp.text.toString().trim()
        val alamat = etAlamat.text.toString().trim()

        if (!validateInputs(nama, email, password, nohp, alamat)) {
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val exitinguser = userRepository.getUserByEmail(email)
                if (exitinguser != null) {
                    runOnUiThread{
                        etEmail.error = "Email sudah terdaftar"
                        Toast.makeText(this@RegisterActivity, "Email sudah terdaftar", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }
                val newUser = ProfileDataclass(
                    nama = nama,
                    email = email,
                    password = password,
                    nohp = nohp,
                    alamat = alamat
                )

                userRepository.insertUser(newUser)

                runOnUiThread{
                    Toast.makeText(this@RegisterActivity, "Registerasi Berhasil", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                runOnUiThread{
                    Toast.makeText(this@RegisterActivity, "Error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun validateInputs(
        nama: String,
        email: String,
        password: String,
        nohpText: String,
        alamat: String
    ): Boolean {
        var isValid = true

        if (nama.isEmpty()) {
            etNama.error = "Name is required"
            isValid = false
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Valid email is required"
            isValid = false
        }

        if (password.isEmpty() || password.length < 6) {
            etPassword.error = "Password must be at least 6 characters"
            isValid = false
        }

        if (nohpText.isEmpty()) {
            etNohp.error = "Phone number is required"
            isValid = false
        } else if (!nohpText.matches(Regex("\\d+"))) {
            etNohp.error = "Phone number must contain only digits"
            isValid = false
        } else if (nohpText.length < 10) {
            etNohp.error = "Phone number must be at least 10 digits"
            isValid = false
        }

        if (alamat.isEmpty()) {
            etAlamat.error = "Address is required"
            isValid = false
        }

        return isValid
    }
}

