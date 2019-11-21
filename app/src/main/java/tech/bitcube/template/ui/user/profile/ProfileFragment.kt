package tech.bitcube.template.ui.user.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import tech.bitcube.template.R
import tech.bitcube.template.data.repository.UserRepository
import tech.bitcube.template.internal.getViewModel
import tech.bitcube.template.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import tech.bitcube.template.data.network.response.user.UserUpdateDto
import tech.bitcube.template.internal.SharedPref

class ProfileFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val pref: SharedPref by instance()

    private val viewModel: ProfileViewModel by lazy {
        getViewModel {
            val repo: UserRepository by instance()
            ProfileViewModel(repo, pref.sharedPref.getInt("userId", -1))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindUI()
    }

    private fun bindUI() = launch {
        val user = viewModel.user.await()

        btnSave.onClick {
            updateUser(
                UserUpdateDto(
                    firstName = edtFirstName.text.toString(),
                    lastName = edtLastName.text.toString(),
                    email = edtEmail.text.toString(),
                    username = edtUsername.text.toString()
                )
            )
        }
        user.observe(viewLifecycleOwner, Observer { u ->
            edtFirstName.setText(u.firstName)
            edtLastName.setText(u.lastName)
            edtUsername.setText(u.username)
            edtEmail.setText(u.email)
        })
    }

    private suspend fun updateUser(user: UserUpdateDto) {
        viewModel.update(user)
    }

}
