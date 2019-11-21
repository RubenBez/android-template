package tech.bitcube.template.ui.user.register

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.navigation.fragment.findNavController

import tech.bitcube.template.R
import tech.bitcube.template.data.network.response.user.UserLoginRequestDto
import tech.bitcube.template.data.network.response.user.UserRegisterRequestDto
import tech.bitcube.template.data.repository.UserRepository
import tech.bitcube.template.internal.getViewModel
import tech.bitcube.template.internal.hideKeyboard
import tech.bitcube.template.ui.base.ScopedFragment
import tech.bitcube.template.ui.user.login.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.register_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import tech.bitcube.template.internal.SharedPref

class RegisterFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()


    private val pref: SharedPref by instance()


    private val registerViewModel: RegisterViewModel by lazy {
        getViewModel {
            val repo: UserRepository by instance()
            RegisterViewModel(repo, pref.sharedPref.getInt("userId", -1))
        }
    }

    private val loginViewModel: LoginViewModel by lazy {
        getViewModel {
            val repo: UserRepository by instance()
            LoginViewModel(repo, pref.sharedPref.getInt("userId", -1))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.register_fragment, container, false)

        val edtPassword = view.findViewById<TextInputEditText>(R.id.edtPasswordConfirm)
        edtPassword.setOnEditorActionListener { _, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (keyEvent != null && keyEvent.action != KeyEvent.ACTION_UP)) btnCreate.callOnClick()
            else false
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        cardError.visibility = View.GONE
        bindUI()
    }

    private fun validateInput(): Boolean {
        val validUser = edtEmail.validEmail {
            cardError.visibility = View.VISIBLE
            txtErrorMessage.text = it
            edtEmail.error = getString(R.string.validation_email)
        }

        if (!validUser) return false

        val p1 = edtPassword
            .validator()
            .nonEmpty()
            .minLength(8)
            .atleastOneLowerCase()
            .atleastOneNumber()
            .atleastOneSpecialCharacters()
            .atleastOneUpperCase()
            .addErrorCallback {
                cardError.visibility = View.VISIBLE
                txtErrorMessage.text = it
                edtPassword.error = it
            }.check()

        if (!p1) return false

        val p2 = edtPasswordConfirm
            .validator()
            .textEqualTo(edtPassword.text.toString())
            .addErrorCallback {
                cardError.visibility = View.VISIBLE
                txtErrorMessage.text = getString(R.string.validation_password_match)
                edtPasswordConfirm.error = getString(R.string.validation_password_match)
            }.check()

        if (!p2) return false

        return true
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        btnCreate.onClick {
            if (!validateInput()) return@onClick

            hideKeyboard()

            var errorMessage = registerViewModel.registerUser(
                UserRegisterRequestDto(
                    edtEmail.text.toString(),
                    edtPassword.text.toString()
                )
            )

            if (errorMessage == null) {
                errorMessage = loginViewModel.loginUser(
                    UserLoginRequestDto(
                        edtEmail.text.toString(),
                        edtPassword.text.toString(),
                        edtEmail.text.toString()
                    )
                )

                if (errorMessage == null) {
                    findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                } else {
                    cardError.visibility = View.VISIBLE
                    txtErrorMessage.visibility = View.VISIBLE
                    txtErrorMessage.text = errorMessage
                }
            } else {
                cardError.visibility = View.VISIBLE
                txtErrorMessage.visibility = View.VISIBLE
                txtErrorMessage.text = errorMessage
            }
        }
    }

}
