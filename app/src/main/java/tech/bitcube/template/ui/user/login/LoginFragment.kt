package tech.bitcube.template.ui.user.login

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.navigation.fragment.findNavController

import tech.bitcube.template.R
import tech.bitcube.template.data.network.response.user.UserLoginRequestDto
import tech.bitcube.template.data.repository.UserRepository
import tech.bitcube.template.internal.SharedPref
import tech.bitcube.template.internal.getViewModel
import tech.bitcube.template.internal.hideKeyboard
import tech.bitcube.template.ui.base.ScopedFragment
import com.google.android.material.textfield.TextInputEditText
import com.wajahatkarim3.easyvalidation.core.view_ktx.contains
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.login_fragment.edtPassword
import kotlinx.android.synthetic.main.login_fragment.txtErrorMessage
import kotlinx.android.synthetic.main.login_fragment.cardError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class LoginFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val pref: SharedPref by instance()

    private val viewModel: LoginViewModel by lazy {
        getViewModel {
            val repo: UserRepository by instance()
            LoginViewModel(repo, pref.sharedPref.getInt("userId", -1))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.login_fragment, container, false)

        val edtPassword = view.findViewById<TextInputEditText>(R.id.edtPassword)
        edtPassword.setOnEditorActionListener { _, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (keyEvent != null && keyEvent.action != KeyEvent.ACTION_UP)) btnLogin.callOnClick()
            else false
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        cardError.visibility = View.GONE// TODO Make this a method

        if (pref.sharedPref.getString("authToken", "")!!.isNotEmpty()) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            return
        }

        bindUI()
    }

    private fun validateInput(): Boolean {
        val validUser = if (edtUsername.contains("@"))
            edtUsername.validEmail {
                cardError.visibility = View.VISIBLE // TODO Make this a method
                txtErrorMessage.text = getString(R.string.validation_email)
                edtUsername.error = getString(R.string.validation_email)
            }
        else {
            edtUsername.validator()
                .nonEmpty()
                .addErrorCallback {
                    cardError.visibility = View.VISIBLE// TODO Make this a method
                    txtErrorMessage.text = it
                    edtUsername.error = it
                }.check()
        }

        val validPassword = edtPassword
            .validator()
            .nonEmpty()
            .minLength(8)
            .atleastOneLowerCase()
            .atleastOneNumber()
            .atleastOneSpecialCharacters()
            .atleastOneUpperCase()
            .addErrorCallback {
                cardError.visibility = View.VISIBLE// TODO Make this a method
                txtErrorMessage.text = it
                edtPassword.error = it
            }.check()

        return validUser && validPassword
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        btnLogin.onClick {
            if (!validateInput()) return@onClick

            val email = if (edtUsername.contains("@")) edtUsername.text.toString() else ""

            val errorMessage = viewModel.loginUser(
                UserLoginRequestDto(
                    email,
                    edtPassword.text.toString(),
                    edtUsername.text.toString()
                )
            )

            if (errorMessage == null) {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                cardError.visibility = View.VISIBLE// TODO Make this a method
                txtErrorMessage.text = errorMessage
            }

            hideKeyboard()
        }
    }


}
