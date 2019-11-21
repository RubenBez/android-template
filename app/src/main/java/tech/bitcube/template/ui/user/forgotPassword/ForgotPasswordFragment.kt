package tech.bitcube.template.ui.user.forgotPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import tech.bitcube.template.R
import tech.bitcube.template.data.repository.UserRepository
import tech.bitcube.template.internal.getViewModel
import tech.bitcube.template.ui.base.ScopedFragment
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import tech.bitcube.template.internal.SharedPref

class ForgotPasswordFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val pref: SharedPref by instance()

    private val viewModel: ForgotPasswordViewModel by lazy {
        getViewModel {
            val repo: UserRepository by instance()
            ForgotPasswordViewModel(repo, pref.sharedPref.getInt("userId", -1))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.forgot_password_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindUI()
    }

    private fun bindUI() = launch {
    }


}
