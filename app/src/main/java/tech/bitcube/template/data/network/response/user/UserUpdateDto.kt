package tech.bitcube.template.data.network.response.user

import com.google.gson.annotations.SerializedName
import tech.bitcube.template.data.db.entity.User
import tech.bitcube.template.data.network.response.BaseDto

data class UserUpdateDto(
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("lastName")
    val lastName: String?,
    val username: String?,
    val email: String?
): BaseDto<User> {

    override fun toEntity(): User = User(
        email = email,
        username = username,
        firstName = firstName,
        lastName = lastName
     )

}
