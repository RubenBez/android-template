package tech.bitcube.template.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    var id: Int? = 0,
    var firstName: String? = "",
    var lastName: String? = "",
    var email: String? = "",
    var username: String? = "",
    var mobileNo: String? = ""
//TODO: User profile picture
)