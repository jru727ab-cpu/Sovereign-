package com.sovereign.civiltas.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sovereign.civiltas.data.local.db.dao.GameStateDao
import com.sovereign.civiltas.data.local.db.entity.GameStateEntity

@Database(entities = [GameStateEntity::class], version = 1, exportSchema = false)
abstract class CiviltasDatabase : RoomDatabase() {
    abstract fun gameStateDao(): GameStateDao
}
