package com.civiltas.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.civiltas.app.data.local.dao.*
import com.civiltas.app.data.local.entity.*

@Database(
    entities = [
        PlayerEntity::class,
        ResourceEntity::class,
        BuildingEntity::class,
        DailyTaskEntity::class,
        DiagnosticLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun resourceDao(): ResourceDao
    abstract fun buildingDao(): BuildingDao
    abstract fun dailyTaskDao(): DailyTaskDao
    abstract fun diagnosticLogDao(): DiagnosticLogDao
}
