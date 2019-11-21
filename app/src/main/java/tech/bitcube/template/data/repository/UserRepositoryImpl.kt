package tech.bitcube.template.data.repository

import androidx.lifecycle.LiveData
import tech.bitcube.template.data.db.dao.UserDao
import tech.bitcube.template.data.network.TemplateNetworkDataSource
import tech.bitcube.template.data.network.response.user.UserLoginRequestDto
import tech.bitcube.template.data.network.response.user.UserRegisterRequestDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.bitcube.template.data.db.entity.User
import tech.bitcube.template.data.network.response.user.UserUpdateDto

class UserRepositoryImpl(
    // DAOs
    private val userDao: UserDao,

    // CinderNetworkDataSource
    private val templateDataSource: TemplateNetworkDataSource

    // Providers
) : UserRepository {
    init {
        // Observer data source and save when new data is received
        templateDataSource.downloadedUser.observeForever { newUser ->
            persistFencedUserData(newUser)
        }

    }
    //region UserRepository Interface

    override suspend fun loginUser(user: UserLoginRequestDto): String? {
        return withContext(Dispatchers.IO) {
            return@withContext templateDataSource.loginUser(user)
        }
    }

    override suspend fun getUser(id: Int): LiveData<out User> {
        return withContext(Dispatchers.IO) {
            initUserData(id)
            return@withContext userDao.getUser(id)
        }
    }

    override suspend fun registerUser(user: UserRegisterRequestDto): String? {
        return withContext(Dispatchers.IO) {
            return@withContext templateDataSource.registerUser(user)
        }
    }

    override suspend fun updateUser(user: UserUpdateDto) {
        templateDataSource.updateUser(user)
    }


    //endregion

    //region Database
    // Save database data
    private fun persistFencedUserData(userData: User) {
        GlobalScope.launch(Dispatchers.IO) {
            userDao.upsert(userData)
        }
    }
    //endregion

    //region Network
    // Get network data
    //region User
    private suspend fun initUserData(id: Int) {
        val latestUserData = userDao.getUserNonLive(id)
        if (latestUserData == null) {
            fetchUserData()
            return
        }
        fetchUserData()
    }

    private suspend fun fetchUserData() = templateDataSource.fetchUser()
    //endregion

    //endregion
}