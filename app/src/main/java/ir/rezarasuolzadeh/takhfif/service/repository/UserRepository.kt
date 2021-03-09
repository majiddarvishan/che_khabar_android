package ir.rezarasuolzadeh.takhfif.service.repository

import android.util.Log
import ir.rezarasuolzadeh.takhfif.api.UserDao
import ir.rezarasuolzadeh.takhfif.service.utils.Enums
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class UserRepository(private val userDao: UserDao) {

    suspend fun setUser(
        email: String,
        firstName: String,
        lastName: String,
        phone: String,
        userStatus: String
    ): Any? {
        return try {
            val jsonObject = JSONObject().apply {
                put("first_name", firstName)
                put("last_name", lastName)
                put("email", email)
                put("mobile", phone)
                put("distance", 100)
                put("tags", userStatus)
            }.toString()

            val requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonObject
            )

            val response = userDao.setUser(requestBody)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else if (!response.isSuccessful && response.code() == 400) {
                "کاربری با این ایمیل قبلا ثبت شده است"
            } else {
                null
            }
        } catch (e: Exception) {
            Log.i("fetch data :", e.message.toString())
            Enums.DataState.NOT_INTERNET
        }
    }

    suspend fun getUser(email: String): Any? {
        return try {
            val response = userDao.getUser(email)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else if (!response.isSuccessful && response.code() == 400) {
                "کاربری با این مشخصات وجود ندارد"
            } else {
                null
            }
        } catch (e: Exception) {
            Log.i("fetch data :", e.message.toString())
            Enums.DataState.NOT_INTERNET
        }
    }

}