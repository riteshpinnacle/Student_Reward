package com.ssccgl.pinnacle.student_reward

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ssccgl.pinnacle.student_reward.databinding.ActivityOtpAuthBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpAuth : AppCompatActivity() {
    private lateinit var binding: ActivityOtpAuthBinding
    private val apiService = RetrofitClient.apiService
    private var lastGeneratedOTP: String? = null // Property to store the last OTP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSendOTP.setOnClickListener {
            val mobileNumber = binding.editTextMobileNumber.text.toString()
            sendOTP(mobileNumber)
        }

        binding.btnVerifyOTP.setOnClickListener {
            val otp = binding.editTextOTP.text.toString()
            verifyOTP(otp)
        }
    }

    private fun sendOTP(mobileNumber: String) {
        val mobileRequest = MobileRequest(mobileNumber)
        val call = apiService.sendOTP(mobileRequest)
        call.enqueue(object : Callback<OTPResponse> {
            override fun onResponse(call: Call<OTPResponse>, response: Response<OTPResponse>) {
                if (response.isSuccessful) {
                    val otpResponse = response.body()
                    val otp = otpResponse?.otp ?: -1
                    showToast("OTP sent successfully: $otp")
                    lastGeneratedOTP = otp.toString() // Store the last generated OTP
                } else {
                    showToast("Failed to send OTP. Please try again.")
                }
            }

            override fun onFailure(call: Call<OTPResponse>, t: Throwable) {
                showToast("Failed to send OTP: ${t.message}")
            }
        })
    }

    private fun verifyOTP(otp: String) {
        // Retrieve the scanned barcode from the intent or stored variable
        val scannedBarcode = intent.getStringExtra("scannedBarcode")


        val mobileNumber = binding.editTextMobileNumber.text.toString()
        val otpRequest = OtpRequest(mobileNumber, otp)
        val call = apiService.updateMobileOTP(otpRequest)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful && otp == lastGeneratedOTP) {
                    showToast("OTP verified successfully")
                    if (scannedBarcode != null) {
                        navigateToEmailVerification(mobileNumber, scannedBarcode)
                    } // Navigate to EmailVerificationActivity
                } else {
                    showToast("OTP verification failed. Please try again.")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showToast("Failed to verify OTP: ${t.message}")
            }
        })
    }

    private fun navigateToEmailVerification(mobileNumber: String, scannedBarcode: String) {

        // Create intent for EmailVerificationActivity and add both extras
        val emailVerificationIntent = Intent(this, EmailVerificationActivity::class.java)
        emailVerificationIntent.putExtra("mobile_number", mobileNumber)
        emailVerificationIntent.putExtra("scannedBarcode", scannedBarcode)
        startActivity(emailVerificationIntent)
    }
    
    private fun showToast(message: String) {
        Toast.makeText(this@OtpAuth, message, Toast.LENGTH_SHORT).show()
    }
}