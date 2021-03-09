package ir.rezarasuolzadeh.takhfif.service.repository

import android.util.Log
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ir.rezarasuolzadeh.takhfif.api.AdvertiserDao
import ir.rezarasuolzadeh.takhfif.service.utils.Enums
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class AdvertiserRepository(private val advertiserDao: AdvertiserDao) {

    suspend fun setAdvertiser(
        email : String,
        description : String,
        latitude: Double,
        longitude: Double,
        startTime: String,
        endTime: String,
        tags: String
    ): Any? {
        return try {
            val jsonObject = JSONObject().apply {
                put("email", email)
                put("description", description)
                put("latitude", latitude)
                put("longitude", longitude)
                put("start_time", startTime)
                put("end_time", endTime)
                put("tags", tags)
            }.toString()

            val requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                jsonObject
            )

            val response = advertiserDao.setAdvertiser(requestBody)
            if (response.isSuccessful && response.body() != null) {
                "با موفقیت ثبت شد"
            } else if (!response.isSuccessful && response.code() == 400) {
                "کاربری با این ایمیل وجود ندارد"
            } else {
                null
            }
        } catch (e: Exception) {
            Log.i("fetch data :", e.message.toString())
            Enums.DataState.NOT_INTERNET
        }
    }

}