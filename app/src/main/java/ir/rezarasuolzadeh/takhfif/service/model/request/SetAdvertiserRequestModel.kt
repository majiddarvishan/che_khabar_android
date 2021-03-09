package ir.rezarasuolzadeh.takhfif.service.model.request

data class SetAdvertiserRequestModel(
    val email: String = "",
    val description: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val startTime: Int = 0,
    val endTime: String = "",
    val tags: String = "",
)