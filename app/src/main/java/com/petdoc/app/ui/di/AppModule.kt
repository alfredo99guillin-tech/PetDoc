package com.petdoc.app.ui.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.petdoc.app.data.local.PetDatabase
import com.petdoc.app.data.local.dao.PetDao
import com.petdoc.app.data.repository.OfflineFirstPetRepository
import com.petdoc.app.domain.model.PetRepository
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
    fun provideDatabase(@ApplicationContext ctx: Context): PetDatabase =
        Room.databaseBuilder(ctx, PetDatabase::class.java, "petdoc_ecuador.db")
            .fallbackToDestructiveMigration().build()

    @Provides fun provideDao(db: PetDatabase): PetDao = db.petDao()

    @Provides @Singleton
    fun provideWorkManager(@ApplicationContext ctx: Context): WorkManager =
        WorkManager.getInstance(ctx)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton
    abstract fun bindPetRepository(impl: OfflineFirstPetRepository): PetRepository
}
