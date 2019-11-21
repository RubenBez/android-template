package tech.bitcube.template.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tech.bitcube.template.data.db.dao.UserDao
import tech.bitcube.template.data.db.entity.User

@Database(
    entities = [User::class],
    version = 1
)
abstract class TemplateDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var instance: TemplateDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                TemplateDatabase::class.java,
                "template.db")
                .build()
    }
}