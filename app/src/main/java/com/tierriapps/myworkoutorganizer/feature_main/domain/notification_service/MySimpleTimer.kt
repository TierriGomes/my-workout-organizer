package com.tierriapps.myworkoutorganizer.feature_main.domain.notification_service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.PendingIntentCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MySimpleTimer(val context: Context, val action: MyBackGroundService.Actions) {
    private var currentTime = 0
    private val requestCode = action.toString().length+999
    private var job: Job? = null

    fun start() {
        job = CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                delay(1000) // Aguarda 1 segundo
                currentTime++
                Intent(context, MyBackGroundService::class.java).also {
                    it.action = action.toString()
                    it.putExtra("current_time", secondsToTime(currentTime))
                    val timerPendingIntent = PendingIntentCompat.getService(context, requestCode, it,
                        PendingIntent.FLAG_UPDATE_CURRENT, true)
                    timerPendingIntent.send()
                }
            }
        }
    }

    fun stop() {
        job?.cancel()
    }

    fun secondsToTime(seconds: Int): String {
        val minutos = seconds / 60
        val segundosRestantes = seconds % 60

        val minutosFormatados = if (minutos < 10) "0$minutos" else minutos.toString()
        val segundosFormatados = if (segundosRestantes < 10) "0$segundosRestantes" else segundosRestantes.toString()

        return "$minutosFormatados:$segundosFormatados"
    }
}