package com.petdoc.app.ui.passport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petdoc.app.domain.model.Pet
import com.petdoc.app.domain.usecase.GetPetByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface PassportUiState {
    data object Loading : PassportUiState
    data class Success(val pet: Pet) : PassportUiState
    data object NotFound : PassportUiState
}

@HiltViewModel
class PassportViewModel @Inject constructor(
    private val getPetByIdUseCase: GetPetByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PassportUiState>(PassportUiState.Loading)
    val uiState: StateFlow<PassportUiState> = _uiState.asStateFlow()

    fun loadPet(id: Int) {
        viewModelScope.launch {
            getPetByIdUseCase(id)
                .catch { _uiState.value = PassportUiState.NotFound }
                .collect { pet ->
                    _uiState.value = if (pet != null) PassportUiState.Success(pet)
                    else PassportUiState.NotFound
                }
        }
    }
}
