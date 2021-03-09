package ir.rezarasuolzadeh.takhfif.api

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AdvertiserDao {

    @Headers(
        "Content-Type: application/json"
    )
    @POST("advertisements")
    suspend fun setAdvertiser(@Body params: RequestBody): Response<String>

}