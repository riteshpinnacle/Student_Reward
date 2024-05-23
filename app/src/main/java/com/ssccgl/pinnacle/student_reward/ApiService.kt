package com.ssccgl.pinnacle.student_reward

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Define a data class to represent the JSON request body
data class MobileRequest(val mobile_number: String)

// Define a data class to represent the JSON response
data class OTPResponse(val otp: Int)

interface ApiService {
    @POST("login-with-mobile")
    fun sendOTP(@Body mobileNumber: MobileRequest): Call<OTPResponse>

    @POST("update-mobile-otp")
    fun updateMobileOTP(@Body otpRequest: OtpRequest): Call<Void>

    @POST("email-verification-mobile-fullname")
    fun emailVerification(@Body emailVerificationRequest : EmailVerificationRequest):Call<EmailVerificationResponse>

    @POST("update-pass")
    fun updatePassword(@Body request: UpdatePasswordRequest): Call<UpdatePasswordResponse>

//    Login Api for already register user using email and mobile number

    @POST("login-with-email-pass")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @PUT("updateStudentBooks/{email}")
    suspend fun updateStudentBooks(
        @Path("email") email: String,
        @Body bookDetails: BookDetailsRequest
    ): Response<BookDetailsResponse>

    // Api for updating coins

    @PUT("updateStudentBooksAndCoins/{email}")
    suspend fun updateStudentBooksAndCoins(
        @Path("email") email: String,
        @Body request: BarcodeRequest
    ): Response<Void>

    // Api to find duplicate barcode
    @GET("searchBarcode/{Barcode}")
    fun searchBarcode(@Path("Barcode") barcode: String): Call<ResponseBody>


}

data class OtpRequest(val mobile_number: String, val otp: String)

object RetrofitClient {
    private const val BASE_URL = "https://auth.ssccglpinnacle.com/"
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)
}


data class EmailVerificationRequest(
    val mobile_number: String,
    val email_id: String,
    val full_name: String,
    val lastScannedBarcode: String
)


data class EmailVerificationResponse(
    val status: String,
    val message: String
)

// updatePassword

data class UpdatePasswordRequest(val userid: String, val pass: String)
data class UpdatePasswordResponse(val status: String)


// Login with Mobile and Email

data class LoginRequest(
    val userid: String,
    val pass: String
)

data class LoginResponse(
    val status: String,
    val details: List<Detail>,
    val token_id: String
)

data class Detail(
    val _id: String,
    val mobile_number: String,
    val otp: String,
    val email_id: String,
    val full_name: String,
    val token: String,
    val token_status: String,
    val password: String
)

//

data class BookDetailsRequest(
    val Books_Purchased: List<Book>
)

data class Book(
    val searchBarcode: String,
    val pincode: Int?,
    val city: String,
    val OnlineMode: String,
    val OrderNum: String,
    val OnlineSeller: String,
    val BookShop: String,
    val BookshopAddress: String
)

data class BookDetailsResponse(
    val _id: String,
    val mobile_number: String,
    val otp: String,
    val purchasedCourses: List<String>,
    val addToCart: List<String>,
    val WishList: List<String>,
    val goal: List<String>,
    val ts: String,
    val Books_Purchased: List<Book>,
    val updated_ts: String,
    val jwtokens: List<Token>,
    val createdAt: String,
    val updatedAt: String,
    val email_id: String,
    val full_name: String,
    val token: String,
    val token_status: String,
    val password: String
)

data class Token(
    val token: String,
    val _id: String
)

// Data class for the new request
data class BarcodeRequest(val searchBarcode: String)