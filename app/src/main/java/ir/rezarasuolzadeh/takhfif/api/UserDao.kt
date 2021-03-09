package ir.rezarasuolzadeh.takhfif.api

import ir.rezarasuolzadeh.takhfif.service.model.response.SetUserResponseModel
import ir.rezarasuolzadeh.takhfif.service.model.response.UserInfoResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface UserDao {

    @Headers(
        "Content-Type: application/json"
    )
    @POST("users")
    suspend fun setUser(@Body params: RequestBody): Response<SetUserResponseModel>

    @GET("users/{user_email}")
    suspend fun getUser(@Path("user_email") userEmail: String): Response<UserInfoResponse>

}