package tech.bitcube.template.ui.user.forgotPassword

import androidx.lifecycle.ViewModel
import tech.bitcube.template.data.repository.UserRepository
import tech.bitcube.template.internal.lazyDeferred
import tech.bitcube.template.ui.user.UserViewModel

class ForgotPasswordViewModel(
    private val repository: UserRepository,
    private val userId: Int
): UserViewModel(repository, userId) {

}
