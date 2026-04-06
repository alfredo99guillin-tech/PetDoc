package com.petdoc.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.petdoc.app.data.local.dao.PetDao
import com.petdoc.app.data.local.model.PetEntity

@Database(
    entities = [PetEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PetDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
}
