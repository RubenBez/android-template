package tech.bitcube.template.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tech.bitcube.template.data.db.entity.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserNonLive(id: Int): User

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: Int): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(user: User)

}