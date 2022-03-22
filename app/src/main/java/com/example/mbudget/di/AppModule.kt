package com.example.mbudget.di

import android.content.Context
import androidx.room.Room
import com.example.mbudget.db.BudgetDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideBudgetDb(@ApplicationContext app: Context) = Room.databaseBuilder(
        app,
        BudgetDatabase::class.java,
        "m_budget"
    ).build() // The reason we can construct a database for the repo

    @Singleton
    @Provides
    fun provideBudgetDao(db: BudgetDatabase) = db.budgetDao()

}