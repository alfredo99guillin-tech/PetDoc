package com.petdoc.app.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ✅ MÓDULO 1: Entidad Room localizada para Ecuador
 * Cumple con requisitos de Agrocalidad e identificación legal
 */
@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // --- Datos básicos mascota ---
    val nombreMascota: String,
    val especie: String,          // Perro, Gato, Ave, etc.
    val raza: String,
    val peso: String,
    val color: String,
    val fechaNacimiento: String,
    val codigoMicrochip: String = "",  // Identificación legal (clínicas Quito/Guayaquil)
    val fotoUri: String = "",

    // --- Vacunas Agrocalidad Ecuador ---
    val fechaVacunaRabia: Long = 0L,          // Obligatoria anual - Agrocalidad
    val fechaVacunaSextuple: Long = 0L,       // Perros: Distemper/Parvovirus/etc.
    val fechaVacunaTripleFelina: Long = 0L,   // Gatos: anual
    val qrData: String = "",                   // JSON para QR del pasaporte

    // --- Datos dueño localizados ---
    val nombreDueno: String,
    val cedulaDueno: String,      // 10 dígitos, validado con Módulo 10 Ecuador
    val contactoEmergencia: String, // Formato +593XXXXXXXXX
    val emailDueno: String = "",
    val provincia: String = "",
    val canton: String = "",

    val notas: String = "",
    val creadoEn: Long = System.currentTimeMillis()
)
