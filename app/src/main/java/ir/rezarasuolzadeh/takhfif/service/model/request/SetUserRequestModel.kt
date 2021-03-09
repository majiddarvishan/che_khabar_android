package ir.rezarasuolzadeh.takhfif.service.model.request

data class SetUserRequestModel(
    val firstName: String,
    val lastName: String,
    val email: String,
    val mobile: String,
    val distance: Int,
    val tags: String
)