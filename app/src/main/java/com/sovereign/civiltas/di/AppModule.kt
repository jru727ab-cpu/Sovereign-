package com.sovereign.civiltas.di

import android.content.Context
import androidx.room.Room
import com.sovereign.civiltas.account.AccountModule
import com.sovereign.civiltas.account.LocalGuestAccountModule
import com.sovereign.civiltas.data.local.db.CiviltasDatabase
import com.sovereign.civiltas.data.local.db.dao.GameStateDao
import com.sovereign.civiltas.sync.NoOpSyncModule
import com.sovereign.civiltas.sync.SyncModule
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
    @Provides @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): CiviltasDatabase =
        Room.databaseBuilder(ctx, CiviltasDatabase::class.java, "civiltas.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideGameStateDao(db: CiviltasDatabase): GameStateDao = db.gameStateDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AccountBindingModule {
    @Binds @Singleton
    abstract fun bindAccountModule(impl: LocalGuestAccountModule): AccountModule
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SyncModuleBinding {
    @Binds @Singleton
    abstract fun bindSyncModule(impl: NoOpSyncModule): SyncModule
}
