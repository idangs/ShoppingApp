package hu.aut.android.todorecyclerviewdemo.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import hu.aut.android.todorecyclerviewdemo.R

@Database(entities = [(Lists::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDAO

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, context.getString(R.string.items_db))
                    .build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}