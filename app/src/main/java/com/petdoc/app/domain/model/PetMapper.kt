package com.petdoc.app.domain.model

import com.petdoc.app.data.local.model.PetEntity

fun PetEntity.asDomainModel() = Pet(
    id = id, nombreMascota = nombreMascota, especie = especie, raza = raza,
    peso = peso, color = color, fechaNacimiento = fechaNacimiento,
    codigoMicrochip = codigoMicrochip, fotoUri = fotoUri,
    fechaVacunaRabia = fechaVacunaRabia, fechaVacunaSextuple = fechaVacunaSextuple,
    fechaVacunaTripleFelina = fechaVacunaTripleFelina, qrData = qrData,
    nombreDueno = nombreDueno, cedulaDueno = cedulaDueno,
    contactoEmergencia = contactoEmergencia, emailDueno = emailDueno,
    provincia = provincia, canton = canton, notas = notas, creadoEn = creadoEn
)

fun Pet.asEntity() = PetEntity(
    id = id, nombreMascota = nombreMascota, especie = especie, raza = raza,
    peso = peso, color = color, fechaNacimiento = fechaNacimiento,
    codigoMicrochip = codigoMicrochip, fotoUri = fotoUri,
    fechaVacunaRabia = fechaVacunaRabia, fechaVacunaSextuple = fechaVacunaSextuple,
    fechaVacunaTripleFelina = fechaVacunaTripleFelina, qrData = qrData,
    nombreDueno = nombreDueno, cedulaDueno = cedulaDueno,
    contactoEmergencia = contactoEmergencia, emailDueno = emailDueno,
    provincia = provincia, canton = canton, notas = notas, creadoEn = creadoEn
)
