package tech.bitcube.template.data.network

import androidx.lifecycle.LiveData
import tech.bitcube.template.data.db.entity.User
import tech.bitcube.template.data.network.response.user.UserLoginRequestDto
import tech.bitcube.template.data.network.response.user.UserRegisterRequestDto
import tech.bitcube.template.data.network.response.user.UserUpdateDto


interface TemplateNetworkDataSource {

    val downloadedUser: LiveData<User>

    suspend fun fetchUser()
    suspend fun loginUser(userLoginRequest: UserLoginRequestDto): String?
    suspend fun registerUser(user: UserRegisterRequestDto): String?
    suspend fun updateUser(user: UserUpdateDto)

}