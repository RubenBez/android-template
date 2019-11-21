package tech.bitcube.template.ui.user.login

import tech.bitcube.template.data.network.response.user.UserLoginRequestDto
import tech.bitcube.template.data.repository.UserRepository
import tech.bitcube.template.ui.user.UserViewModel

class LoginViewModel(
    private val repository: UserRepository,
    private val userId: Int
): UserViewModel(repository, userId) {

    suspend fun loginUser(user: UserLoginRequestDto): String? {
        return repository.loginUser(user)
    }

}
