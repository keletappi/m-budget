package com.example.mbudget.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mbudget.db.converters.BigDecimalConverter
import com.example.mbudget.db.converters.CurrencyConverter
import com.example.mbudget.db.converters.InstantConverter
import com.example.mbudget.db.converters.UuidConverter
import com.example.mbudget.db.model.DbBudget
import com.example.mbudget.db.model.DbExpense

@Database(entities = [DbBudget::class, DbExpense::class], version = 1)
@TypeConverters(InstantConverter::class, UuidConverter::class, BigDecimalConverter::class, CurrencyConverter::class)
abstract class BudgetDatabase: RoomDatabase() {
    abstract fun budgetDao(): BudgetDao
}