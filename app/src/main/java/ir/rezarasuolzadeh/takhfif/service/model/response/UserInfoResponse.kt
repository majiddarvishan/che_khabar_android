package ir.rezarasuolzadeh.takhfif.service.model.response

import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
    @SerializedName("distance")
    val distance: Int = 0,
    @SerializedName("email")
    val email: String = "",
    @SerializedName("first_name")
    val firstName: String = "",
    @SerializedName("last_name")
    val lastName: String = "",
    @SerializedName("mobile")
    val mobile: String = ""
)