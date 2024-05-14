package com.ssccgl.pinnacle.student_reward

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssccgl.pinnacle.student_reward.databinding.ActivityOtpAuthBinding

class OtpAuth : AppCompatActivity() {
    private lateinit var binding: ActivityOtpAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve scanned barcode data from intent extras
        val scannedBarcode = intent.getStringExtra("scannedBarcode")

        // Use the scanned barcode data as needed
        binding.textViewScannedBarcode.text = "Scanned Barcode: $scannedBarcode"
    }
}