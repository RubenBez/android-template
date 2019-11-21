package tech.bitcube.template.data.network

import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.bitcube.template.data.db.entity.User
import tech.bitcube.template.data.network.response.user.UserLoginRequestDto
import tech.bitcube.template.data.network.response.user.UserRegisterRequestDto
import tech.bitcube.template.data.network.response.user.UserUpdateDto
import tech.bitcube.template.internal.NoConnectivityException
import tech.bitcube.template.internal.SharedPref
import java.net.UnknownHostException

// TODO Create proper error responses to use in the UI
class TemplateNetworkDataSourceImpl(
    private val templateApiService: TemplateApiService,
    private val sharedPref: SharedPref
) : TemplateNetworkDataSource {
    private var authToken: String?
        get() = "Bearer ${sharedPref.sharedPref.getString("authToken", "")}"
        set(value) = sharedPref.sharedPref.edit { putString("authToken", value) }

    private val _downloadedUser = MutableLiveData<User>()
    override val downloadedUser: LiveData<User>
        get() = _downloadedUser

    override suspend fun fetchUser() {
        try {
            val response = authToken?.let { templateApiService.getUserAsync(it) }

            if (response!!.isSuccessful) {
                val body = response.body()

                _downloadedUser.postValue(body!!.toEntity())
            } else {
                Log.e("Request","Error while getting user data")
            }
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        } catch (e: UnknownHostException) {
            Log.e("Connectivity", "Server unreachable", e)
        }
    }

    override suspend fun updateUser(user: UserUpdateDto) {
        try {
            val response = templateApiService.updateUserAsync(authToken, user)
            if (response.isSuccessful)
            {

                _downloadedUser.postValue(user.toEntity())
            }
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        } catch (e: UnknownHostException) {
            Log.e("Connectivity", "Server unreachable", e)
        }
    }

    override suspend fun loginUser(userLoginRequest: UserLoginRequestDto): String? {
        try {
            // Login the user
            val response = templateApiService.loginUserAsync(userLoginRequest)
            return when {
                response.isSuccessful -> {
                    val body = response.body()
                    if (body != null) {
                        authToken = body.token
                        sharedPref.sharedPref.edit { putInt("userId", body.id!!) }
                    }
                    _downloadedUser.postValue(body!!.toEntity())
                    null
                }
                response.code() == 401 -> "The username or password is invalid"
                else -> "User invalid"
            }
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
            return "No Internet connection"
        } catch (e: UnknownHostException) {
            Log.e("Connectivity", "Server unreachable", e)
            return "Server unreachable"
        }
    }

    override suspend fun registerUser(user: UserRegisterRequestDto): String? {
        return try {
            val response = templateApiService.registerUserAsync(user)
            when {
                response.isSuccessful -> null
                response.code() == 409 -> "This username or email is already in use already exists"
                response.code() == 406 -> "The password should be at least 8 characters long, contain one of the following symbols[!@#\\$&*] and one uppercase, lowercase, and number"
                else -> "Failed to register"
            }
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
            "No Internet connection"
        } catch (e: UnknownHostException) {
            Log.e("Connectivity", "Server unreachable", e)
            "Server unreachable"
        }
    }

}
