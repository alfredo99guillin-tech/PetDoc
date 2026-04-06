package com.petdoc.app.domain.model

data class Pet(
    val id: Int = 0,
    val nombreMascota: String,
    val especie: String,
    val raza: String,
    val peso: String,
    val color: String,
    val fechaNacimiento: String,
    val codigoMicrochip: String = "",
    val fotoUri: String = "",
    val fechaVacunaRabia: Long = 0L,
    val fechaVacunaSextuple: Long = 0L,
    val fechaVacunaTripleFelina: Long = 0L,
    val qrData: String = "",
    val nombreDueno: String,
    val cedulaDueno: String,
    val contactoEmergencia: String,
    val emailDueno: String = "",
    val provincia: String = "",
    val canton: String = "",
    val notas: String = "",
    val creadoEn: Long = System.currentTimeMillis()
)

// ✅ Validación cédula ecuatoriana — Algoritmo Módulo 10 (Registro Civil)
fun validarCedulaEcuatoriana(cedula: String): Boolean {
    if (cedula.length != 10) return false
    val provincia = cedula.substring(0, 2).toIntOrNull() ?: return false
    if (provincia < 1 || provincia > 24) return false

    val coeficientes = intArrayOf(2, 1, 2, 1, 2, 1, 2, 1, 2)
    var suma = 0
    for (i in 0..8) {
        var digito = cedula[i].digitToInt() * coeficientes[i]
        if (digito >= 10) digito -= 9
        suma += digito
    }
    val residuo = suma % 10
    val digitoVerificador = if (residuo == 0) 0 else 10 - residuo
    return digitoVerificador == cedula[9].digitToInt()
}

// ✅ Validación teléfono Ecuador (09XXXXXXXX)
fun validarTelefonoEcuador(telefono: String): Boolean {
    val limpio = telefono.replace("+593", "0").replace(" ", "")
    return limpio.matches(Regex("^09\\d{8}$"))
}

// ✅ Formatear teléfono al estándar internacional +593
fun formatearTelefonoEcuador(telefono: String): String {
    val limpio = telefono.replace(" ", "").replace("-", "")
    return when {
        limpio.startsWith("+593") -> limpio
        limpio.startsWith("0")    -> "+593${limpio.substring(1)}"
        else                      -> "+593$limpio"
    }
}
