package com.petdoc.app.domain.usecase

import com.petdoc.app.domain.model.Pet
import com.petdoc.app.domain.model.PetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPetsUseCase @Inject constructor(private val repo: PetRepository) {
    operator fun invoke(): Flow<List<Pet>> = repo.getPets()
}

class GetPetByIdUseCase @Inject constructor(private val repo: PetRepository) {
    operator fun invoke(id: Int): Flow<Pet?> = repo.getPetById(id)
}

class SavePetUseCase @Inject constructor(private val repo: PetRepository) {
    suspend operator fun invoke(pet: Pet) = repo.savePet(pet)
}

class DeletePetUseCase @Inject constructor(private val repo: PetRepository) {
    suspend operator fun invoke(id: Int) = repo.deletePet(id)
}
