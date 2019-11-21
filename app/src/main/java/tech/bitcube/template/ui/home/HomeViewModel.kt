package tech.bitcube.template.ui.home

import androidx.lifecycle.ViewModel
import tech.bitcube.template.data.repository.UserRepository
import tech.bitcube.template.internal.lazyDeferred
import tech.bitcube.template.ui.user.UserViewModel

class HomeViewModel(
    private val repository: UserRepository,
    private val userId: Int
) : UserViewModel(repository, userId) {
}

