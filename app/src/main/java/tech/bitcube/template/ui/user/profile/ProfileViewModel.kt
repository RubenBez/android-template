package tech.bitcube.template.ui.user.profile

import tech.bitcube.template.data.network.response.user.UserUpdateDto
import tech.bitcube.template.data.repository.UserRepository
import tech.bitcube.template.ui.user.UserViewModel

class ProfileViewModel(
    private val repository: UserRepository,
    private val userId: Int
): UserViewModel(repository, userId) {

    suspend fun update(user: UserUpdateDto) {
        repository.updateUser(user)
    }


}

