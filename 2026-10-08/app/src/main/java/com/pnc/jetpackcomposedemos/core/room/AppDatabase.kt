package com.pnc.jetpackcomposedemos.core.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pnc.jetpackcomposedemos.features.todo.data.ToDoDao
import com.pnc.jetpackcomposedemos.features.todo.data.ToDoEntity
import com.pnc.jetpackcomposedemos.features.orders.data.local.LineItemEntity
import com.pnc.jetpackcomposedemos.features.orders.data.local.OrderDao
import com.pnc.jetpackcomposedemos.features.orders.data.local.OrderEntity

@Database(
    entities = [
        ToDoEntity::class,
        OrderEntity::class,
        LineItemEntity::class
        ],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun toDoDao(): ToDoDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile   // don't let background threads cache the null value
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // fetch or create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


