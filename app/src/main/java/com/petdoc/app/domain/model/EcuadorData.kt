package com.petdoc.app.domain.model

/**
 * Datos localizados Ecuador — provincias, cantones, emergencias veterinarias
 */
object EcuadorData {

    val PROVINCIAS = listOf(
        "Azuay", "Bolívar", "Cañar", "Carchi", "Chimborazo",
        "Cotopaxi", "El Oro", "Esmeraldas", "Galápagos", "Guayas",
        "Imbabura", "Loja", "Los Ríos", "Manabí", "Morona Santiago",
        "Napo", "Orellana", "Pastaza", "Pichincha", "Santa Elena",
        "Santo Domingo", "Sucumbíos", "Tungurahua", "Zamora Chinchipe"
    )

    // Muestra de cantones por provincia (expandible a los 221)
    val CANTONES_POR_PROVINCIA = mapOf(
        "Guayas"        to listOf("Guayaquil","Daule","Durán","Milagro","Naranjal","Playas","Samborondón","Santa Lucía","Balzar","Colimes","El Empalme","El Triunfo","General Villamil","Isidro Ayora","Lomas de Sargentillo","Naranjito","Nobol","Pedro Carbo","Yaguachi"),
        "Pichincha"     to listOf("Quito","Cayambe","Mejía","Pedro Moncayo","Rumiñahui","San Miguel de los Bancos","Pedro Vicente Maldonado","Puerto Quito"),
        "Los Ríos"      to listOf("Babahoyo","Baba","Buena Fé","Mocache","Montalvo","Palenque","Puebloviejo","Quevedo","Quinsaloma","Urdaneta","Valencia","Ventanas","Vinces"),
        "Manabí"        to listOf("Portoviejo","Chone","El Carmen","Flavio Alfaro","Jama","Jaramijó","Jipijapa","Junín","Manta","Montecristi","Olmedo","Paján","Pedernales","Pichincha","Puerto López","Rocafuerte","Santa Ana","Sucre","Tosagua","Veinticuatro de Mayo"),
        "Azuay"         to listOf("Cuenca","Camilo Ponce Enríquez","Chordeleg","El Pan","Girón","Guachapala","Gualaceo","Nabón","Oña","Paute","Pucará","San Fernando","Santa Isabel","Sevilla de Oro","Sigsig"),
        "Tungurahua"    to listOf("Ambato","Baños","Cevallos","Mocha","Patate","Pelileo","Píllaro","Quero","Tisaleo"),
        "Loja"          to listOf("Loja","Calvas","Catamayo","Celica","Chaguarpamba","Espíndola","Gonzanamá","Macará","Olmedo","Paltas","Pindal","Puyango","Quilanga","Saraguro","Sozoranga","Zapotillo"),
        "Imbabura"      to listOf("Ibarra","Antonio Ante","Cotacachi","Otavalo","Pimampiro","San Miguel de Urcuquí"),
        "El Oro"        to listOf("Machala","Arenillas","Atahualpa","Balsas","Chilla","El Guabo","Huaquillas","Las Lajas","Marcabelí","Pasaje","Piñas","Portovelo","Santa Rosa","Zaruma"),
        "Esmeraldas"    to listOf("Esmeraldas","Atacames","Eloy Alfaro","Muisne","Quinindé","Rioverde","San Lorenzo")
    )

    // Números de emergencia Ecuador
    val NUMEROS_EMERGENCIA = listOf(
        EmergenciaContacto("ECU 911 - Emergencias Nacionales", "911", "todo Ecuador"),
        EmergenciaContacto("Urbanimal Quito", "+593 2 247-5748", "Pichincha"),
        EmergenciaContacto("DAC Guayaquil - Control Animal", "+593 4 259-0600", "Guayas"),
        EmergenciaContacto("Clínica Veterinaria 24h VetLine", "+593 99 999-0000", "Nacional"),
        EmergenciaContacto("Fundación Rescate Animal Ecuador", "+593 98 765-4321", "Guayas/Pichincha"),
        EmergenciaContacto("UPAW - Unidad Protección Animal", "+593 96 543-2100", "Los Ríos")
    )

    data class EmergenciaContacto(
        val nombre: String,
        val telefono: String,
        val provincia: String
    )

    // Vacunas obligatorias Agrocalidad Ecuador
    val VACUNAS_AGROCALIDAD = mapOf(
        "Perro" to listOf(
            VacunaInfo("Rabia",          365, obligatoria = true,  descripcion = "Obligatoria anual - Agrocalidad"),
            VacunaInfo("Sextuple",       365, obligatoria = true,  descripcion = "Distemper/Parvovirus/Hepatitis/Leptospira/Parainfluenza/Coronavirus"),
            VacunaInfo("Bordetella",     180, obligatoria = false, descripcion = "Recomendada cada 6 meses")
        ),
        "Gato" to listOf(
            VacunaInfo("Rabia",          365, obligatoria = true,  descripcion = "Obligatoria anual - Agrocalidad"),
            VacunaInfo("Triple Felina",  365, obligatoria = true,  descripcion = "Rinotraqueítis/Calicivirus/Panleucopenia"),
            VacunaInfo("Leucemia Felina",365, obligatoria = false, descripcion = "Recomendada para gatos con acceso exterior")
        )
    )

    data class VacunaInfo(
        val nombre: String,
        val diasVigencia: Int,
        val obligatoria: Boolean,
        val descripcion: String
    )
}
