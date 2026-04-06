package com.petdoc.app.data.repository

import com.petdoc.app.data.local.dao.PetDao
import com.petdoc.app.domain.model.Pet
import com.petdoc.app.domain.model.PetRepository
import com.petdoc.app.domain.model.asDomainModel
import com.petdoc.app.domain.model.asEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class OfflineFirstPetRepository @Inject constructor(
    private val petDao: PetDao
) : PetRepository {
    override fun getPets() = petDao.getPetsFlow().map { it.map { e -> e.asDomainModel() } }
    override fun getPetById(id: Int) = petDao.getPetByIdFlow(id).map { it?.asDomainModel() }
    override suspend fun savePet(pet: Pet) = petDao.upsertPet(pet.asEntity())
    override suspend fun deletePet(id: Int) = petDao.deletePetById(id)
}
