package com.petdoc.app.ui.home

import com.petdoc.app.domain.model.Pet

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val pets: List<Pet>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

sealed interface AddPetUiState {
    data object Idle : AddPetUiState
    data object Saving : AddPetUiState
    data object Saved : AddPetUiState
    data class Error(val message: String) : AddPetUiState
}
