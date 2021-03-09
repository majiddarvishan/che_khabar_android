package ir.rezarasuolzadeh.takhfif.service.model.response

import com.google.gson.annotations.SerializedName

data class SetUserResponseModel(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("type")
    val type: String = ""
)