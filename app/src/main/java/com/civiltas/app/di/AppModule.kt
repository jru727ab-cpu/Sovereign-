package com.civiltas.app.di

import android.content.Context
import androidx.room.Room
import com.civiltas.app.data.local.AppDatabase
import com.civiltas.app.data.local.dao.*
import com.civiltas.app.data.sync.LocalFirstSyncImpl
import com.civiltas.app.data.sync.SyncRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "civiltas.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun providePlayerDao(db: AppDatabase): PlayerDao = db.playerDao()
    @Provides fun provideResourceDao(db: AppDatabase): ResourceDao = db.resourceDao()
    @Provides fun provideBuildingDao(db: AppDatabase): BuildingDao = db.buildingDao()
    @Provides fun provideDailyTaskDao(db: AppDatabase): DailyTaskDao = db.dailyTaskDao()
    @Provides fun provideDiagnosticLogDao(db: AppDatabase): DiagnosticLogDao = db.diagnosticLogDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SyncModule {
    @Binds
    @Singleton
    abstract fun bindSyncRepository(impl: LocalFirstSyncImpl): SyncRepository
}
