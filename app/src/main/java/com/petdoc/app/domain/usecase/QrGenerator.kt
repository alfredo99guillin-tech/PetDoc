package com.petdoc.app.domain.usecase

import android.graphics.Bitmap
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.petdoc.app.domain.model.Pet
import java.text.SimpleDateFormat
import java.util.*

/**
 * ✅ MÓDULO 3: Generador de QR con JSON localizado Ecuador
 *
 * El QR codifica JSON con:
 * - Nombre mascota, especie
 * - owner_id (cédula ecuatoriana)
 * - emergency (formato +593)
 * - Última vacuna Rabia (Agrocalidad)
 * - status de vacunas
 */
object QrGenerator {

    private val gson = Gson()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("es", "EC"))

    data class PetQrData(
        val app: String = "PetDoc Ecuador",
        val version: String = "2.1",
        val pet: String,
        val especie: String,
        val raza: String,
        val microchip: String,
        val owner_id: String,           // Cédula ecuatoriana
        val owner_name: String,
        val emergency: String,          // +593XXXXXXXXX
        val ultima_vacuna_rabia: String, // Agrocalidad obligatoria
        val status: String,
        val provincia: String,
        val canton: String
    )

    fun generateBitmap(pet: Pet, size: Int = 400): Bitmap? {
        return try {
            val ahora = System.currentTimeMillis()
            val vacunaOk = pet.fechaVacunaRabia > ahora
            val statusVacuna = if (vacunaOk) "Vacunas al día ✓" else "⚠️ Vacuna Rabia vencida"

            val fechaRabia = if (pet.fechaVacunaRabia > 0)
                dateFormat.format(Date(pet.fechaVacunaRabia))
            else "No registrada"

            val qrData = PetQrData(
                pet            = pet.nombreMascota,
                especie        = pet.especie,
                raza           = pet.raza,
                microchip      = pet.codigoMicrochip.ifEmpty { "N/A" },
                owner_id       = pet.cedulaDueno,
                owner_name     = pet.nombreDueno,
                emergency      = pet.contactoEmergencia,
                ultima_vacuna_rabia = fechaRabia,
                status         = statusVacuna,
                provincia      = pet.provincia,
                canton         = pet.canton
            )

            val json = gson.toJson(qrData)
            BarcodeEncoder().encodeBitmap(json, BarcodeFormat.QR_CODE, size, size)
        } catch (e: Exception) {
            null
        }
    }
}
