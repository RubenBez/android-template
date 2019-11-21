package tech.bitcube.template.data.repository

import androidx.lifecycle.LiveData
import tech.bitcube.template.data.db.entity.User
import tech.bitcube.template.data.network.response.user.UserLoginRequestDto
import tech.bitcube.template.data.network.response.user.UserRegisterRequestDto
import tech.bitcube.template.data.network.response.user.UserUpdateDto

interface UserRepository {

    suspend fun loginUser(user: UserLoginRequestDto): String?
    suspend fun getUser(id: Int): LiveData<out User>
    suspend fun registerUser(user: UserRegisterRequestDto): String?
    suspend fun updateUser(user: UserUpdateDto)

}