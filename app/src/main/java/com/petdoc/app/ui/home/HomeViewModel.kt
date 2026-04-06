package com.petdoc.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petdoc.app.domain.model.Pet
import com.petdoc.app.domain.usecase.DeletePetUseCase
import com.petdoc.app.domain.usecase.GetPetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPetsUseCase: GetPetsUseCase,
    private val deletePetUseCase: DeletePetUseCase
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> =
        getPetsUseCase()
            .map<List<Pet>, HomeUiState> { HomeUiState.Success(it) }
            .catch { emit(HomeUiState.Error(it.message ?: "Error")) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState.Loading)

    fun deletePet(id: Int) = viewModelScope.launch { deletePetUseCase(id) }
}
