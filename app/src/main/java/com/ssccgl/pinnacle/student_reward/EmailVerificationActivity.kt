package com.ssccgl.pinnacle.student_reward

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ssccgl.pinnacle.student_reward.databinding.ActivityEmailVerificationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmailVerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmailVerificationBinding
    private val apiService = RetrofitClient.apiService

    private var mobileNumber: String? = null
    private var scannedBarcode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        // Get the mobile number from the intent
//        val mobileNumber = intent.getStringExtra("mobile_number")
//        otpAuthViewModel.mobileNumber = mobileNumber
//
//        // Get the scanned barcode from the intent
//        val scannedBarcode = barcodeScanViewModel.scannedBarcode

//        // Retrieve the data from the intent
//        val mobileNumber = intent.getStringExtra("mobile_number")
//        val scannedBarcode = intent.getStringExtra("scannedBarcode")
        mobileNumber = intent.getStringExtra("mobile_number")
        scannedBarcode = intent.getStringExtra("scannedBarcode")


        binding.btnVerifyEmail.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val name = binding.editTextName.text.toString()
            if (mobileNumber.isNullOrEmpty()) {
                showToast("Mobile number is missing")
                return@setOnClickListener
            }
            if (scannedBarcode.isNullOrEmpty()) {
                showToast("Last scanned barcode is missing")
                return@setOnClickListener
            }

            verificationRequest(mobileNumber!!, email, name, scannedBarcode!!)
        }
    }

    private fun verificationRequest(mobileNumber: String, emailId: String, name: String, lastScannedBarcode: String) {
        // Create the request object
        val emailVerificationRequest = EmailVerificationRequest(
            mobile_number = mobileNumber,
            email_id = emailId,
            full_name = name,
            lastScannedBarcode = lastScannedBarcode
        )

        // Make the API call
        val call = apiService.emailVerification(emailVerificationRequest)
        call.enqueue(object : Callback<EmailVerificationResponse> {
            override fun onResponse(
                call: Call<EmailVerificationResponse>,
                response: Response<EmailVerificationResponse>
            ) {
//                if (response.isSuccessful) {
//                    // Handle successful response
//                    val verificationResponse = response.body()
//                    verificationResponse?.let {
//                        showToast("Verification successful: ${it.message}")
//                    } ?: showToast("Verification successful, but response is empty")
//                }

                if (response.isSuccessful) {
                    // Handle successful response
                    val verificationResponse = response.body()
                    verificationResponse?.let {
                        showVerificationSuccessDialog(emailId)
                    }
                }
                else {

                    // Handle unsuccessful response
                    showToast("Verification failed, This e-mailId may belongs to some other User: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EmailVerificationResponse>, t: Throwable) {
                // Handle failure
                showToast("Failed to verify email: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showVerificationSuccessDialog(emailId: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_verification_success, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<Button>(R.id.btnDismiss).setOnClickListener {
            dialog.dismiss()
            // You can perform any additional action here if needed
        }

        dialogView.findViewById<TextView>(R.id.tvMessage).text =
            "A verification link has been sent to $emailId. Please go there to complete the further process."

        dialog.show()
    }

//    fun navigateToLoginActivity(view: View) {
//        val intent = Intent(this, LoginActivity::class.java)
//        startActivity(intent)
//    }

    fun navigateToLoginActivity(view: View) {
        val intent = Intent(this, LoginActivity::class.java).apply {
            putExtra("mobile_number", mobileNumber)
//            putExtra("email_id", emailId)
            putExtra("email_id", binding.editTextEmail.text.toString())
//            putExtra("full_name", name)
            putExtra("full_name", binding.editTextName.text.toString())
            putExtra("last_scanned_barcode", scannedBarcode)
        }
        startActivity(intent)
    }
}