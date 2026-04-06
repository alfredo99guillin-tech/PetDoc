package com.petdoc.app.data.local.dao

import androidx.room.*
import com.petdoc.app.data.local.model.PetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Query("SELECT * FROM pets ORDER BY creadoEn DESC")
    fun getPetsFlow(): Flow<List<PetEntity>>

    @Query("SELECT * FROM pets WHERE id = :id")
    fun getPetByIdFlow(id: Int): Flow<PetEntity?>

    // Mascotas con vacuna de rabia próxima a vencer (para NotifyWorker)
    @Query("""
        SELECT * FROM pets 
        WHERE fechaVacunaRabia > 0 
        AND fechaVacunaRabia <= :thresholdMs
    """)
    suspend fun getPetsWithRabiesExpiringSoon(thresholdMs: Long): List<PetEntity>

    @Upsert
    suspend fun upsertPet(entity: PetEntity)

    @Query("DELETE FROM pets WHERE id = :id")
    suspend fun deletePetById(id: Int)
}
