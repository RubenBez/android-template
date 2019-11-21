package tech.bitcube.template.ui.user

import androidx.lifecycle.ViewModel
import tech.bitcube.template.data.repository.UserRepository
import tech.bitcube.template.internal.lazyDeferred

abstract class UserViewModel(
    private val repository: UserRepository,
    private val userId: Int
): ViewModel() {
    val user by lazyDeferred {
        repository.getUser(userId)
    }
}