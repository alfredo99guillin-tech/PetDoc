package com.petdoc.app.domain.model

import kotlinx.coroutines.flow.Flow

interface PetRepository {
    fun getPets(): Flow<List<Pet>>
    fun getPetById(id: Int): Flow<Pet?>
    suspend fun savePet(pet: Pet)
    suspend fun deletePet(id: Int)
}
