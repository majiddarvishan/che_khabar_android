package ir.rezarasuolzadeh.takhfif.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import ir.rezarasuolzadeh.takhfif.service.model.response.SetUserResponseModel
import ir.rezarasuolzadeh.takhfif.service.repository.AdvertiserRepository
import ir.rezarasuolzadeh.takhfif.service.utils.Enums
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class AdvertiserViewModel(private val advertiserRepository: AdvertiserRepository) : ViewModel() {

    fun setAdvertiser(
        email: String,
        description: String,
        latitude: Double,
        longitude: Double,
        startTime: String,
        endTime: String,
        tags: String
    ) = liveData(Dispatchers.IO) {
        runBlocking {
            emit(Enums.DataState.LOADING)
            val response = advertiserRepository.setAdvertiser(
                email,
                description,
                latitude,
                longitude,
                startTime,
                endTime,
                tags
            )
            when (response) {
                null -> {
                    emit(Enums.DataState.FAILED)
                }
                is String -> {
                    emit(response)
                }
                is Enums.DataState -> {
                    emit(response)
                }
            }
        }
    }

}