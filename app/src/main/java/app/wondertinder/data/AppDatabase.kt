package app.wondertinder.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.wondertinder.data.daos.HeroDao
import app.wondertinder.data.entities.Hero

@Database(entities = [Hero::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun heroDao(): HeroDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hero_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}