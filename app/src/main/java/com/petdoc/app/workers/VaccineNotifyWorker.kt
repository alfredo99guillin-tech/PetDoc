package com.petdoc.app.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.petdoc.app.data.local.dao.PetDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

/**
 * ✅ MÓDULO 2: NotifyWorker — Alertas vacunas Agrocalidad Ecuador
 *
 * Lógica: Verifica las fechas de vacunas y dispara notificación
 * 15 días ANTES del vencimiento (normativa Agrocalidad)
 */
@HiltWorker
class VaccineNotifyWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val petDao: PetDao
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        createNotificationChannel()

        val ahora = System.currentTimeMillis()
        val threshold15Dias = ahora + TimeUnit.DAYS.toMillis(15)

        // Verificar mascotas con vacuna de Rabia próxima a vencer
        val mascotasCriticas = petDao.getPetsWithRabiesExpiringSoon(threshold15Dias)

        mascotasCriticas.forEach { pet ->
            val diasRestantes = ((pet.fechaVacunaRabia - ahora) / TimeUnit.DAYS.toMillis(1)).toInt()

            when {
                diasRestantes <= 0 -> {
                    sendNotification(
                        id = pet.id,
                        title = "⚠️ Vacuna VENCIDA — ${pet.nombreMascota}",
                        message = "La vacuna de Rabia de ${pet.nombreMascota} ha vencido. " +
                                "Obligatorio por Agrocalidad Ecuador. Visita tu veterinario.",
                        priority = NotificationCompat.PRIORITY_HIGH
                    )
                }
                diasRestantes <= 15 -> {
                    sendNotification(
                        id = pet.id + 1000,
                        title = "🐾 Vacuna próxima — ${pet.nombreMascota}",
                        message = "Faltan $diasRestantes días para renovar la vacuna de Rabia " +
                                "de ${pet.nombreMascota}. Obligatoria según Agrocalidad Ecuador.",
                        priority = NotificationCompat.PRIORITY_DEFAULT
                    )
                }
            }
        }

        // Reprogramar para el siguiente mes
        scheduleNextCheck(applicationContext)
        return Result.success()
    }

    private fun sendNotification(id: Int, title: String, message: String, priority: Int) {
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(priority)
            .setAutoCancel(true)
            .build()

        manager.notify(id, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alertas de Vacunas PetDoc",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Recordatorios de vacunas Agrocalidad Ecuador"
            }
            val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "petdoc_vaccines"
        const val WORK_NAME   = "vaccine_check_work"

        /**
         * Programa la verificación cada 30 días usando OneTimeWorkRequest
         * (encadenado para simular periodicidad con control total)
         */
        fun scheduleNextCheck(context: Context) {
            val request = OneTimeWorkRequestBuilder<VaccineNotifyWorker>()
                .setInitialDelay(30, TimeUnit.DAYS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                        .build()
                )
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                request
            )
        }

        /** Programa verificación inmediata (al abrir la app) */
        fun scheduleImmediate(context: Context) {
            val request = OneTimeWorkRequestBuilder<VaccineNotifyWorker>()
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                "${WORK_NAME}_immediate",
                ExistingWorkPolicy.REPLACE,
                request
            )
        }
    }
}
