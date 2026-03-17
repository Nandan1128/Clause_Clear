package com.example.clauseclear.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.clauseclear.data.local.dao.DocumentDao
import com.example.clauseclear.data.local.entity.*

@Database(
    entities = [DocumentEntity::class,
                ClauseEntity::class,
                KeyPointEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun documentDao(): DocumentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "clauseclear_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()


                INSTANCE = instance
                instance
            }
        }
    }
}
