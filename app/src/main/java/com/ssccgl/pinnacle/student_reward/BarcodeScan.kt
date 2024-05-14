package com.ssccgl.pinnacle.student_reward

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.ssccgl.pinnacle.student_reward.databinding.ActivityBarcodeScanBinding


class BarcodeScan : AppCompatActivity() {
    private lateinit var binding: ActivityBarcodeScanBinding
    private lateinit var viewModel: BarcodeScanViewModel

    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private var isCameraStarted = false
    private var lastScannedBarcode: String? = null
    private var barcodeAdded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarcodeScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(BarcodeScanViewModel::class.java)
        initBarcodeScanner()

    }

    private fun initBarcodeScanner() {
        try {
            barcodeDetector = BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.CODE_128)
                .build()
            cameraSource = CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build()

        } catch (e: Exception) {
            e.printStackTrace()
            showToast("Error initializing barcode scanner")
            return
        }
        binding.surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                if (!isCameraStarted) {
                    try {
                        cameraSource.start(binding.surfaceView.holder) // suppressed
                        isCameraStarted = true
                    } catch (e: Exception) {
                        e.printStackTrace()
                        showToast("Error starting camera")
                    }
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                try {
                    cameraSource.stop()
                    isCameraStarted = false
                } catch (e: Exception) {
                    e.printStackTrace()
                    showToast("Error stopping camera")
                }
            }
        })
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                showToast("Barcode scanner has been stopped")
            }



//            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
//                val barcodes = detections.detectedItems
//                for (i in 0 until barcodes.size()) {
//                    val scannedBarcode = barcodes.valueAt(i).displayValue
//                    if (scannedBarcode != viewModel.scannedBarcode) {
//
//                        barcodeAdded = true
//                        lastScannedBarcode = scannedBarcode
//
////                        val intent = Intent(this@BarcodeScan, OtpAuth::class.java)
////                        startActivity(intent)
//
//                        binding.txtDetectedItems.text = scannedBarcode
//                        binding.txtDetectedItems.visibility = View.VISIBLE
//
//
//                        val scannedBarcode = scannedBarcode
//                        intent.putExtra(scannedBarcode, scannedBarcode)
//                        startActivity(intent)
//
//                        vibrate()
//                    }
//                }
//            }

//            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
//                val barcodes = detections.detectedItems
//                for (i in 0 until barcodes.size()) {
//                    val scannedBarcode = barcodes.valueAt(i).displayValue
//                    if (scannedBarcode != viewModel.scannedBarcode) {
//                        viewModel.scannedBarcode = scannedBarcode
//
//                        val intent = Intent(this@BarcodeScan, OtpAuth::class.java)
//                        startActivity(intent)
//
//                        runOnUiThread {
//                            binding.txtDetectedItems.text = scannedBarcode
//                            binding.txtDetectedItems.visibility = View.VISIBLE
//                        }
//                        vibrate()
//                    }
//                }
//            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                for (i in 0 until barcodes.size()) {
                    val scannedBarcode = barcodes.valueAt(i).displayValue
                    if (scannedBarcode != viewModel.scannedBarcode) {
                        viewModel.scannedBarcode = scannedBarcode

                        val intent = Intent(this@BarcodeScan, OtpAuth::class.java)
                        intent.putExtra("scannedBarcode", scannedBarcode)
                        startActivity(intent)

                        runOnUiThread {
                            binding.txtDetectedItems.text = scannedBarcode
                            binding.txtDetectedItems.visibility = View.VISIBLE
                        }
                        vibrate()
                    }
                }
            }

        })
    }


    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(500)
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }




    override fun onResume() {
        super.onResume()
        if (!isCameraStarted) {
            initBarcodeScanner()
        }
    }

    override fun onPause() {
        super.onPause()
        cameraSource.stop()
        isCameraStarted = false
    }

}