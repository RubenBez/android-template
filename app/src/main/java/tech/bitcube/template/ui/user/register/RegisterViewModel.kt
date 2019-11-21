package tech.bitcube.template.ui.user.register

import tech.bitcube.template.data.network.response.user.UserRegisterRequestDto
import tech.bitcube.template.data.repository.UserRepository
import tech.bitcube.template.ui.user.UserViewModel

class RegisterViewModel(
    private val repository: UserRepository,
    private val userId: Int
) : UserViewModel(repository, userId) {
    
    suspend fun registerUser(user: UserRegisterRequestDto): String? {
        return repository.registerUser(user)
    }
}
