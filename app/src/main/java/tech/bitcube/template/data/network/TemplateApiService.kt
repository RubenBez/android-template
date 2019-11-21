package tech.bitcube.template.data.network

import tech.bitcube.template.data.network.interceptors.ConnectivityInterceptor
import tech.bitcube.template.data.network.response.user.UserDto
import tech.bitcube.template.data.network.response.user.UserLoginRequestDto
import tech.bitcube.template.data.network.response.user.UserRegisterRequestDto
import tech.bitcube.template.data.network.response.user.UserUpdateDto
import tech.bitcube.template.internal.util.UrlHelper
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface TemplateApiService {

    @POST("auth/login")
    suspend fun loginUserAsync(@Body userLoginRequestDto: UserLoginRequestDto): Response<UserDto>

    @POST("auth/register")
    suspend fun registerUserAsync(@Body user: UserRegisterRequestDto): Response<UserDto>

    @GET("auth/me")
    suspend fun getUserAsync(@Header("Authorization") authToken: String): Response<UserDto>

    @PATCH("user/")
    suspend fun updateUserAsync(@Header("Authorization") authToken: String?, @Body userDto: UserUpdateDto): Response<String>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): TemplateApiService {
            val requestInterceptor = Interceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(UrlHelper.URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TemplateApiService::class.java)
        }
    }
}
