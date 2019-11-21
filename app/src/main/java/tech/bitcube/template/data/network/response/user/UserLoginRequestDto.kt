package tech.bitcube.template.data.network.response.user

data class UserLoginRequestDto(
    val email: String,
    val password: String,
    val username: String
)