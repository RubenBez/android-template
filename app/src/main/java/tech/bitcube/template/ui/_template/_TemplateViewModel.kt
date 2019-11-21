package tech.bitcube.template.ui._template

import androidx.lifecycle.ViewModel
import tech.bitcube.template.data.repository.UserRepository
import tech.bitcube.template.internal.lazyDeferred

class _TemplateViewModel(
    private val repository: UserRepository,
    private val userId: Int
): ViewModel() {

    val test by lazyDeferred {
        repository.getUser(userId)
    }

}

