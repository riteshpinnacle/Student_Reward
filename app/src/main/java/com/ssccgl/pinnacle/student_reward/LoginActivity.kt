package com.ssccgl.pinnacle.student_reward

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ssccgl.pinnacle.student_reward.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val apiService = RetrofitClient.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve values from intent extras
        val lastScannedBarcode = intent.getStringExtra("last_scanned_barcode")

        // Display the lastScannedBarcode value in the TextView
        findViewById<TextView>(R.id.txtLastScannedBarcode).text = lastScannedBarcode

        // Handle login button click
        binding.btnLogin.setOnClickListener {
            loginUser(lastScannedBarcode)
        }
    }

    private fun loginUser(lastScannedBarcode: String?) {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            showToast("Please enter email and password")
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Please enter a valid email address")
            return
        }

        val loginRequest = LoginRequest(userid = email, pass = password)
        val call = apiService.login(loginRequest)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.status == "success") {
                        showToast("Login successful")
                        // Navigate to BookDetailsActivity
                        val intent = Intent(this@LoginActivity, BookDetailsActivity::class.java).apply {
                            putExtra("last_scanned_barcode", lastScannedBarcode)
                            putExtra("email_id", email)
                        }
                        startActivity(intent)
                        finish() // Finish the current activity
                    } else {
                        showToast("Login failed. Please try again.")
                    }
                } else {
                    showToast("Login failed. Please try again.")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showToast("Login failed: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
