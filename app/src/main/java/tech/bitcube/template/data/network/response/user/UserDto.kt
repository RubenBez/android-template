package tech.bitcube.template.data.network.response.user

import tech.bitcube.template.data.db.entity.User
import tech.bitcube.template.data.network.response.BaseDto

class UserDto(
    val id: Int?,
    val email: String?,
    val username: String?,
    val firstName: String?,
    val lastName: String?,
    val mobileNo: String?,
    val token: String
): BaseDto<User> {

    override fun toEntity(): User = User(
        id = id,
        email = email,
        username = username,
        firstName = firstName,
        lastName = lastName,
        mobileNo = mobileNo
    )
}