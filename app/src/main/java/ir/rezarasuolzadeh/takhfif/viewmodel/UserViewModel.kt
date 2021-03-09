package ir.rezarasuolzadeh.takhfif.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import ir.rezarasuolzadeh.takhfif.service.model.response.SetUserResponseModel
import ir.rezarasuolzadeh.takhfif.service.repository.UserRepository
import ir.rezarasuolzadeh.takhfif.service.utils.Enums
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun setUser(
        email: String,
        firstName: String,
        lastName: String,
        phone: String,
        userStatus: String
    ) = liveData(Dispatchers.IO) {
        runBlocking {
            emit(Enums.DataState.LOADING)
            val response = userRepository.setUser(email, firstName, lastName, phone, userStatus)
            when (response) {
                null -> {
                    emit(Enums.DataState.FAILED)
                }
                is SetUserResponseModel -> {
                    emit(response)
                }
                is Enums.DataState -> {
                    emit(response)
                }
                is String -> {
                    emit(response)
                }
            }
        }
    }

    fun getUser(
        email: String
    ) = liveData(Dispatchers.IO) {
        runBlocking {
            emit(Enums.DataState.LOADING)
            val response = userRepository.getUser(email)
            when (response) {
                null -> {
                    emit(Enums.DataState.FAILED)
                }
                is SetUserResponseModel -> {
                    emit(response)
                }
                is Enums.DataState -> {
                    emit(response)
                }
                is String -> {
                    emit(response)
                }
            }
        }
    }

}