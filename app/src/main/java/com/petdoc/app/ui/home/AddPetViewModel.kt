package com.petdoc.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petdoc.app.domain.model.*
import com.petdoc.app.domain.usecase.SavePetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPetViewModel @Inject constructor(
    private val savePetUseCase: SavePetUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddPetUiState>(AddPetUiState.Idle)
    val uiState: StateFlow<AddPetUiState> = _uiState.asStateFlow()

    fun savePet(
        nombre: String, especie: String, raza: String, peso: String,
        color: String, fechaNacimiento: String, microchip: String,
        nombreDueno: String, cedula: String, telefono: String,
        email: String, provincia: String, canton: String,
        fechaRabia: Long, fechaSextuple: Long, fechaTripleFelina: Long,
        notas: String
    ) {
        // Validaciones Ecuador
        if (nombre.isBlank()) { _uiState.value = AddPetUiState.Error("El nombre de la mascota es obligatorio"); return }
        if (nombreDueno.isBlank()) { _uiState.value = AddPetUiState.Error("El nombre del dueño es obligatorio"); return }
        if (cedula.isNotBlank() && !validarCedulaEcuatoriana(cedula)) {
            _uiState.value = AddPetUiState.Error("Cédula ecuatoriana inválida (verificá los 10 dígitos)"); return
        }
        if (telefono.isNotBlank() && !validarTelefonoEcuador(telefono)) {
            _uiState.value = AddPetUiState.Error("Teléfono inválido. Formato: 09XXXXXXXX"); return
        }

        viewModelScope.launch {
            _uiState.value = AddPetUiState.Saving
            try {
                savePetUseCase(Pet(
                    nombreMascota = nombre, especie = especie, raza = raza,
                    peso = peso, color = color, fechaNacimiento = fechaNacimiento,
                    codigoMicrochip = microchip, nombreDueno = nombreDueno,
                    cedulaDueno = cedula,
                    contactoEmergencia = if (telefono.isNotBlank()) formatearTelefonoEcuador(telefono) else "",
                    emailDueno = email, provincia = provincia, canton = canton,
                    fechaVacunaRabia = fechaRabia, fechaVacunaSextuple = fechaSextuple,
                    fechaVacunaTripleFelina = fechaTripleFelina, notas = notas
                ))
                _uiState.value = AddPetUiState.Saved
            } catch (e: Exception) {
                _uiState.value = AddPetUiState.Error(e.message ?: "Error al guardar")
            }
        }
    }
}
