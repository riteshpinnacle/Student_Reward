//////package com.ssccgl.pinnacle.student_reward
//////
//////import android.os.Bundle
//////import android.view.View
//////import android.widget.AdapterView
//////import android.widget.ArrayAdapter
//////import android.widget.Toast
//////import androidx.appcompat.app.AppCompatActivity
//////import androidx.constraintlayout.widget.ConstraintLayout
//////import androidx.lifecycle.lifecycleScope
//////import com.ssccgl.pinnacle.student_reward.databinding.ActivityBookDetailsBinding
//////import kotlinx.coroutines.launch
//////import retrofit2.Response
//////
//////class BookDetailsActivity : AppCompatActivity() {
//////
//////    private lateinit var binding: ActivityBookDetailsBinding
//////    private val apiService = RetrofitClient.apiService
//////    private var emailId: String? = null
//////
//////    override fun onCreate(savedInstanceState: Bundle?) {
//////        super.onCreate(savedInstanceState)
//////        binding = ActivityBookDetailsBinding.inflate(layoutInflater)
//////        setContentView(binding.root)
//////
//////        // Retrieve the lastScannedBarcode from the intent
//////        val lastScannedBarcode = intent.getStringExtra("last_scanned_barcode")
//////        emailId = intent.getStringExtra("email_id")
//////
//////        // Display the lastScannedBarcode value in the TextView
//////        binding.txtLastScannedBarcode.text = lastScannedBarcode
//////
//////        // Setup Spinner for Online Mode
//////        val onlineModes = arrayOf("Amazon", "FlipKart", "Other Websites")
//////        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, onlineModes)
//////        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//////        binding.spinnerOnlineMode.adapter = adapter
//////
//////        binding.spinnerOnlineMode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//////            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//////                // Handle item selection if needed
//////            }
//////
//////            override fun onNothingSelected(parent: AdapterView<*>) {
//////                // Handle case where nothing is selected if needed
//////            }
//////        }
//////
//////        // Handle RadioGroup selection
//////        binding.radioGroupPurchaseLocation.setOnCheckedChangeListener { _, checkedId ->
//////            when (checkedId) {
//////                R.id.radioOnline -> showOnlinePurchaseFields()
//////                R.id.radioLocalBookshop -> showLocalBookshopFields()
//////            }
//////        }
//////
//////        // Handle form submission and API call here
//////        binding.btnSave.setOnClickListener {
//////            lastScannedBarcode?.let{
//////            saveBookDetails(it)
//////        }
//////        }
//////    }
//////
//////    private fun showOnlinePurchaseFields() {
//////        binding.tvOnlineMode.visibility = View.VISIBLE
//////        binding.spinnerOnlineMode.visibility = View.VISIBLE
//////        binding.editTextOrderNumber.visibility = View.VISIBLE
//////        binding.editTextSellerName.visibility = View.VISIBLE
//////
//////        binding.editTextBookshopName.visibility = View.GONE
//////        binding.editTextBookshopAddress.visibility = View.GONE
//////        binding.editTextPincode.visibility = View.GONE
//////        binding.tvPlace.visibility = View.GONE
//////        binding.tvState.visibility = View.GONE
//////
//////        // Update constraints for btnSave
//////        binding.btnSave.layoutParams = (binding.btnSave.layoutParams as ConstraintLayout.LayoutParams).apply {
//////            topToBottom = binding.editTextSellerName.id
//////        }
//////    }
//////
//////    private fun showLocalBookshopFields() {
//////        binding.tvOnlineMode.visibility = View.GONE
//////        binding.spinnerOnlineMode.visibility = View.GONE
//////        binding.editTextOrderNumber.visibility = View.GONE
//////        binding.editTextSellerName.visibility = View.GONE
//////
//////        binding.editTextBookshopName.visibility = View.VISIBLE
//////        binding.editTextBookshopAddress.visibility = View.VISIBLE
//////        binding.editTextPincode.visibility = View.VISIBLE
//////        binding.tvPlace.visibility = View.VISIBLE
//////        binding.tvState.visibility = View.VISIBLE
//////
//////        // Update constraints for btnSave
//////        binding.btnSave.layoutParams = (binding.btnSave.layoutParams as ConstraintLayout.LayoutParams).apply {
//////            topToBottom = binding.tvState.id
//////        }
//////    }
//////
//////    private fun saveBookDetails(lastScannedBarcode: String) {
//////        emailId?.let { email ->
//////            if (binding.radioLocalBookshop.isChecked) {
//////                val pincode = binding.editTextPincode.text.toString()
//////                if (pincode.length != 6 || pincode.toIntOrNull() == null) {
//////                    showToast("Enter valid Pincode")
//////                    return
//////                }
//////            }
//////
//////            val book = if (binding.radioOnline.isChecked) {
//////                Book(
//////                    searchBarcode = lastScannedBarcode,
//////                    pincode = null,
//////                    city = "",
//////                    OnlineMode = binding.spinnerOnlineMode.selectedItem.toString(),
//////                    OrderNum = binding.editTextOrderNumber.text.toString(),
//////                    OnlineSeller = binding.editTextSellerName.text.toString(),
//////                    BookShop = "",
//////                    BookshopAddress = ""
//////                )
//////            } else {
//////                Book(
//////                    searchBarcode = lastScannedBarcode,
//////                    pincode = binding.editTextPincode.text.toString().toIntOrNull(),
//////                    city = binding.tvPlace.text.toString(),
//////                    OnlineMode = "",
//////                    OrderNum = "",
//////                    OnlineSeller = "",
//////                    BookShop = binding.editTextBookshopName.text.toString(),
//////                    BookshopAddress = binding.editTextBookshopAddress.text.toString()
//////                )
//////            }
//////
//////            val request = BookDetailsRequest(Books_Purchased = listOf(book))
//////
//////            lifecycleScope.launch {
//////                try {
//////                    val response: Response<BookDetailsResponse> = apiService.updateStudentBooks(email, request)
//////                    if (response.isSuccessful) {
//////                        // Handle successful response
//////                        showToast("Book details updated successfully")
//////                    } else {
//////                        // Handle unsuccessful response
//////                        showToast("Failed to update book details: ${response.message()}")
//////                    }
//////                } catch (e: Exception) {
//////                    // Handle failure
//////                    showToast("Failed to update book details: ${e.message}")
//////                }
//////            }
//////        } ?: showToast("Email ID is missing")
//////    }
//////
//////    private fun showToast(message: String) {
//////        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//////    }
//////}
////
////package com.ssccgl.pinnacle.student_reward
////
////import android.os.Bundle
////import android.view.View
////import android.widget.AdapterView
////import android.widget.ArrayAdapter
////import android.widget.Toast
////import androidx.appcompat.app.AppCompatActivity
////import androidx.lifecycle.lifecycleScope
////import com.ssccgl.pinnacle.student_reward.databinding.ActivityBookDetailsBinding
////import kotlinx.coroutines.launch
////import retrofit2.Response
////
////class BookDetailsActivity : AppCompatActivity() {
////
////    private lateinit var binding: ActivityBookDetailsBinding
////    private val apiService = RetrofitClient.apiService
////    private var emailId: String? = null
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        binding = ActivityBookDetailsBinding.inflate(layoutInflater)
////        setContentView(binding.root)
////
////        // Retrieve the lastScannedBarcode and emailId from the intent
////        val lastScannedBarcode = intent.getStringExtra("last_scanned_barcode")
////        emailId = intent.getStringExtra("email_id")
////
////        // Display the lastScannedBarcode value in the TextView
////        binding.txtLastScannedBarcode.text = lastScannedBarcode
////
////        // Setup Spinner for Online Mode
////        val onlineModes = arrayOf("Amazon", "FlipKart", "Other Websites")
////        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, onlineModes)
////        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
////        binding.spinnerOnlineMode.adapter = adapter
////
////        binding.spinnerOnlineMode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
////            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
////                // Handle item selection if needed
////            }
////
////            override fun onNothingSelected(parent: AdapterView<*>) {
////                // Handle case where nothing is selected if needed
////            }
////        }
////
////        // Handle RadioGroup selection
////        binding.radioGroupPurchaseLocation.setOnCheckedChangeListener { _, checkedId ->
////            when (checkedId) {
////                R.id.radioOnline -> showOnlinePurchaseFields()
////                R.id.radioLocalBookshop -> showLocalBookshopFields()
////            }
////        }
////
////        // Handle form submission and API call here
////        binding.btnSave.setOnClickListener {
////            lastScannedBarcode?.let {
////                saveBookDetails(it)
////            }
////        }
////    }
////
////    private fun showOnlinePurchaseFields() {
////        binding.tvOnlineMode.visibility = View.VISIBLE
////        binding.spinnerOnlineMode.visibility = View.VISIBLE
////        binding.editTextOrderNumber.visibility = View.VISIBLE
////        binding.editTextSellerName.visibility = View.VISIBLE
////
////        binding.editTextBookshopName.visibility = View.GONE
////        binding.editTextBookshopAddress.visibility = View.GONE
////        binding.editTextPincode.visibility = View.GONE
////        binding.tvPlace.visibility = View.GONE
////        binding.tvState.visibility = View.GONE
////
////        // Update constraints for btnSave
////        binding.btnSave.layoutParams = (binding.btnSave.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams).apply {
////            topToBottom = binding.editTextSellerName.id
////        }
////    }
////
////    private fun showLocalBookshopFields() {
////        binding.tvOnlineMode.visibility = View.GONE
////        binding.spinnerOnlineMode.visibility = View.GONE
////        binding.editTextOrderNumber.visibility = View.GONE
////        binding.editTextSellerName.visibility = View.GONE
////
////        binding.editTextBookshopName.visibility = View.VISIBLE
////        binding.editTextBookshopAddress.visibility = View.VISIBLE
////        binding.editTextPincode.visibility = View.VISIBLE
////        binding.tvPlace.visibility = View.VISIBLE
////        binding.tvState.visibility = View.VISIBLE
////
////        // Update constraints for btnSave
////        binding.btnSave.layoutParams = (binding.btnSave.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams).apply {
////            topToBottom = binding.tvState.id
////        }
////    }
////
////    private fun saveBookDetails(lastScannedBarcode: String) {
////        emailId?.let { email ->
////            if (binding.radioLocalBookshop.isChecked) {
////                val pincode = binding.editTextPincode.text.toString()
////                if (pincode.length != 6 || pincode.toIntOrNull() == null) {
////                    showToast("Enter valid Pincode")
////                    return
////                }
////            }
////
////            val book = if (binding.radioOnline.isChecked) {
////                Book(
////                    searchBarcode = lastScannedBarcode,
////                    pincode = null,
////                    city = "",
////                    OnlineMode = binding.spinnerOnlineMode.selectedItem.toString(),
////                    OrderNum = binding.editTextOrderNumber.text.toString(),
////                    OnlineSeller = binding.editTextSellerName.text.toString(),
////                    BookShop = "",
////                    BookshopAddress = ""
////                )
////            } else {
////                Book(
////                    searchBarcode = lastScannedBarcode,
////                    pincode = binding.editTextPincode.text.toString().toIntOrNull(),
////                    city = binding.tvPlace.text.toString(),
////                    OnlineMode = "",
////                    OrderNum = "",
////                    OnlineSeller = "",
////                    BookShop = binding.editTextBookshopName.text.toString(),
////                    BookshopAddress = binding.editTextBookshopAddress.text.toString()
////                )
////            }
////
////            val request = BookDetailsRequest(Books_Purchased = listOf(book))
////
////            lifecycleScope.launch {
////                try {
////                    val response: Response<BookDetailsResponse> = apiService.updateStudentBooks(email, request)
////                    if (response.isSuccessful) {
////                        // Handle successful response
////                        showToast("Book details updated successfully")
////                        // Call the new API
////                        updateBooksAndCoins(email, lastScannedBarcode)
////                    } else {
////                        // Handle unsuccessful response
////                        showToast("Failed to update book details: ${response.message()}")
////                    }
////                } catch (e: Exception) {
////                    // Handle failure
////                    showToast("Failed to update book details: ${e.message}")
////                }
////            }
////        } ?: showToast("Email ID is missing")
////    }
////
////    private suspend fun updateBooksAndCoins(email: String, lastScannedBarcode: String) {
////        val request = BarcodeRequest(searchBarcode = lastScannedBarcode)
////        try {
////            val response: Response<Void> = apiService.updateStudentBooksAndCoins(email, request)
////            if (response.isSuccessful) {
////                showToast("Books and coins updated successfully")
////            } else {
////                showToast("Failed to update books and coins: ${response.message()}")
////            }
////        } catch (e: Exception) {
////            showToast("Failed to update books and coins: ${e.message}")
////        }
////    }
////
////    private fun showToast(message: String) {
////        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
////    }
////}
//
//package com.ssccgl.pinnacle.student_reward
//
//import android.os.Bundle
//import android.view.View
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.lifecycleScope
//import com.ssccgl.pinnacle.student_reward.databinding.ActivityBookDetailsBinding
//import kotlinx.coroutines.launch
//import okhttp3.ResponseBody
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class BookDetailsActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityBookDetailsBinding
//    private val apiService = RetrofitClient.apiService
//    private var emailId: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityBookDetailsBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Retrieve the lastScannedBarcode and emailId from the intent
//        val lastScannedBarcode = intent.getStringExtra("last_scanned_barcode")
//        emailId = intent.getStringExtra("email_id")
//
//        // Display the lastScannedBarcode value in the TextView
//        binding.txtLastScannedBarcode.text = lastScannedBarcode
//
//        // Setup Spinner for Online Mode
//        val onlineModes = arrayOf("Amazon", "FlipKart", "Other Websites")
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, onlineModes)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        binding.spinnerOnlineMode.adapter = adapter
//
//        binding.spinnerOnlineMode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                // Handle item selection if needed
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // Handle case where nothing is selected if needed
//            }
//        }
//
//        // Handle RadioGroup selection
//        binding.radioGroupPurchaseLocation.setOnCheckedChangeListener { _, checkedId ->
//            when (checkedId) {
//                R.id.radioOnline -> showOnlinePurchaseFields()
//                R.id.radioLocalBookshop -> showLocalBookshopFields()
//            }
//        }
//
//        // Handle form submission and API call here
//        binding.btnSave.setOnClickListener {
//            lastScannedBarcode?.let {
//                saveBookDetails(it)
//            }
//        }
//    }
//
//    private fun showOnlinePurchaseFields() {
//        binding.tvOnlineMode.visibility = View.VISIBLE
//        binding.spinnerOnlineMode.visibility = View.VISIBLE
//        binding.editTextOrderNumber.visibility = View.VISIBLE
//        binding.editTextSellerName.visibility = View.VISIBLE
//
//        binding.editTextBookshopName.visibility = View.GONE
//        binding.editTextBookshopAddress.visibility = View.GONE
//        binding.editTextPincode.visibility = View.GONE
//        binding.tvPlace.visibility = View.GONE
//        binding.tvState.visibility = View.GONE
//
//        // Update constraints for btnSave
//        binding.btnSave.layoutParams = (binding.btnSave.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams).apply {
//            topToBottom = binding.editTextSellerName.id
//        }
//    }
//
//    private fun showLocalBookshopFields() {
//        binding.tvOnlineMode.visibility = View.GONE
//        binding.spinnerOnlineMode.visibility = View.GONE
//        binding.editTextOrderNumber.visibility = View.GONE
//        binding.editTextSellerName.visibility = View.GONE
//
//        binding.editTextBookshopName.visibility = View.VISIBLE
//        binding.editTextBookshopAddress.visibility = View.VISIBLE
//        binding.editTextPincode.visibility = View.VISIBLE
//        binding.tvPlace.visibility = View.VISIBLE
//        binding.tvState.visibility = View.VISIBLE
//
//        // Update constraints for btnSave
//        binding.btnSave.layoutParams = (binding.btnSave.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams).apply {
//            topToBottom = binding.tvState.id
//        }
//    }
//
//    private fun saveBookDetails(lastScannedBarcode: String) {
//        emailId?.let { email ->
//            if (binding.radioLocalBookshop.isChecked) {
//                val pincode = binding.editTextPincode.text.toString()
//                if (pincode.length != 6 || pincode.toIntOrNull() == null) {
//                    showToast("Enter valid Pincode")
//                    return
//                }
//            }
//
//            val book = if (binding.radioOnline.isChecked) {
//                Book(
//                    searchBarcode = lastScannedBarcode,
//                    pincode = null,
//                    city = "",
//                    OnlineMode = binding.spinnerOnlineMode.selectedItem.toString(),
//                    OrderNum = binding.editTextOrderNumber.text.toString(),
//                    OnlineSeller = binding.editTextSellerName.text.toString(),
//                    BookShop = "",
//                    BookshopAddress = ""
//                )
//            } else {
//                Book(
//                    searchBarcode = lastScannedBarcode,
//                    pincode = binding.editTextPincode.text.toString().toIntOrNull(),
//                    city = binding.tvPlace.text.toString(),
//                    OnlineMode = "",
//                    OrderNum = "",
//                    OnlineSeller = "",
//                    BookShop = binding.editTextBookshopName.text.toString(),
//                    BookshopAddress = binding.editTextBookshopAddress.text.toString()
//                )
//            }
//
//            val request = BookDetailsRequest(Books_Purchased = listOf(book))
//
//            lifecycleScope.launch {
//                try {
//                    val response: Response<BookDetailsResponse> = apiService.updateStudentBooks(email, request)
//                    if (response.isSuccessful) {
//                        // Handle successful response
//                        showToast("Book details updated successfully")
//                        // Call the GET API if the update is successful
//                        getBookDetails(lastScannedBarcode)
//                    } else {
//                        // Handle unsuccessful response
//                        showToast("Failed to update book details: ${response.message()}")
//                    }
//                } catch (e: Exception) {
//                    // Handle failure
//                    showToast("Failed to update book details: ${e.message}")
//                }
//            }
//        } ?: showToast("Email ID is missing")
//    }
//
//    private fun getBookDetails(lastScannedBarcode: String) {
//        apiService.searchBarcode(lastScannedBarcode).enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                if (response.isSuccessful) {
//                    val result = response.body()?.string() ?: "No response"
//                    when (result) {
//                        "Not Found" -> showToast("Book details: Not Found")
//                        "Already Exist" -> showToast("Book details: Already Exist")
//                        else -> showToast("Unexpected response: $result")
//                    }
//                } else {
//                    showToast("Failed to retrieve book details: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                showToast("Failed to retrieve book details: ${t.message}")
//            }
//        })
//    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
//}

package com.ssccgl.pinnacle.student_reward

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ssccgl.pinnacle.student_reward.databinding.ActivityBookDetailsBinding
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailsBinding
    private val apiService = RetrofitClient.apiService
    private var emailId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the lastScannedBarcode and emailId from the intent
        val lastScannedBarcode = intent.getStringExtra("last_scanned_barcode")
        emailId = intent.getStringExtra("email_id")

        // Display the lastScannedBarcode value in the TextView
        binding.txtLastScannedBarcode.text = lastScannedBarcode

        // Setup Spinner for Online Mode
        val onlineModes = arrayOf("Amazon", "FlipKart", "Other Websites")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, onlineModes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerOnlineMode.adapter = adapter

        binding.spinnerOnlineMode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Handle item selection if needed
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case where nothing is selected if needed
            }
        }

        // Handle RadioGroup selection
        binding.radioGroupPurchaseLocation.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioOnline -> showOnlinePurchaseFields()
                R.id.radioLocalBookshop -> showLocalBookshopFields()
            }
        }

        // Handle form submission and API call here
        binding.btnSave.setOnClickListener {
            lastScannedBarcode?.let {
                saveBookDetails(it)
            }
        }
    }

    private fun showOnlinePurchaseFields() {
        binding.tvOnlineMode.visibility = View.VISIBLE
        binding.spinnerOnlineMode.visibility = View.VISIBLE
        binding.editTextOrderNumber.visibility = View.VISIBLE
        binding.editTextSellerName.visibility = View.VISIBLE

        binding.editTextBookshopName.visibility = View.GONE
        binding.editTextBookshopAddress.visibility = View.GONE
        binding.editTextPincode.visibility = View.GONE
        binding.tvPlace.visibility = View.GONE
        binding.tvState.visibility = View.GONE

        // Update constraints for btnSave
        binding.btnSave.layoutParams = (binding.btnSave.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams).apply {
            topToBottom = binding.editTextSellerName.id
        }
    }

    private fun showLocalBookshopFields() {
        binding.tvOnlineMode.visibility = View.GONE
        binding.spinnerOnlineMode.visibility = View.GONE
        binding.editTextOrderNumber.visibility = View.GONE
        binding.editTextSellerName.visibility = View.GONE

        binding.editTextBookshopName.visibility = View.VISIBLE
        binding.editTextBookshopAddress.visibility = View.VISIBLE
        binding.editTextPincode.visibility = View.VISIBLE
        binding.tvPlace.visibility = View.VISIBLE
        binding.tvState.visibility = View.VISIBLE

        // Update constraints for btnSave
        binding.btnSave.layoutParams = (binding.btnSave.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams).apply {
            topToBottom = binding.tvState.id
        }
    }

    private fun saveBookDetails(lastScannedBarcode: String) {
        emailId?.let { email ->
            if (binding.radioLocalBookshop.isChecked) {
                val pincode = binding.editTextPincode.text.toString()
                if (pincode.length != 6 || pincode.toIntOrNull() == null) {
                    showToast("Enter valid Pincode")
                    return
                }
            }

            val book = if (binding.radioOnline.isChecked) {
                Book(
                    searchBarcode = lastScannedBarcode,
                    pincode = null,
                    city = "",
                    OnlineMode = binding.spinnerOnlineMode.selectedItem.toString(),
                    OrderNum = binding.editTextOrderNumber.text.toString(),
                    OnlineSeller = binding.editTextSellerName.text.toString(),
                    BookShop = "",
                    BookshopAddress = ""
                )
            } else {
                Book(
                    searchBarcode = lastScannedBarcode,
                    pincode = binding.editTextPincode.text.toString().toIntOrNull(),
                    city = binding.tvPlace.text.toString(),
                    OnlineMode = "",
                    OrderNum = "",
                    OnlineSeller = "",
                    BookShop = binding.editTextBookshopName.text.toString(),
                    BookshopAddress = binding.editTextBookshopAddress.text.toString()
                )
            }

            val request = BookDetailsRequest(Books_Purchased = listOf(book))

            lifecycleScope.launch {
                try {
                    val response: Response<BookDetailsResponse> = apiService.updateStudentBooks(email, request)
                    if (response.isSuccessful) {
                        // Handle successful response
                        showToast("Book details updated successfully")
                        // Call the GET API if the update is successful
                        getBookDetails(lastScannedBarcode, email)
                    } else {
                        // Handle unsuccessful response
                        showToast("Failed to update book details: ${response.message()}")
                    }
                } catch (e: Exception) {
                    // Handle failure
                    showToast("Failed to update book details: ${e.message}")
                }
            }
        } ?: showToast("Email ID is missing")
    }

    private fun getBookDetails(lastScannedBarcode: String, email: String) {
        apiService.searchBarcode(lastScannedBarcode).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val result = response.body()?.string() ?: "No response"
                    when (result) {
                        "Not Found" -> {
                            showToast("Book details: Not Found")
                            updateStudentBooksAndCoins(email, lastScannedBarcode)
                        }
                        "Already Exist" -> showToast("Book details: Already Exist")
                        else -> showToast("Unexpected response: $result")
                    }
                } else {
                    showToast("Failed to retrieve book details: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showToast("Failed to retrieve book details: ${t.message}")
            }
        })
    }

    private fun updateStudentBooksAndCoins(email: String, lastScannedBarcode: String) {
        val request = BarcodeRequest(searchBarcode = lastScannedBarcode)

        lifecycleScope.launch {
            try {
                val response: Response<Void> = apiService.updateStudentBooksAndCoins(email, request)
                if (response.isSuccessful) {
                    showToast("Student books and coins updated successfully")
                } else {
                    showToast("Failed to update student books and coins: ${response.message()}")
                }
            } catch (e: Exception) {
                showToast("Failed to update student books and coins: ${e.message}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
