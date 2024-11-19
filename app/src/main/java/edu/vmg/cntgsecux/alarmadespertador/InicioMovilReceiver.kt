package edu.vmg.cntgsecux.alarmadespertador

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class InicioMovilReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        //TODO("InicioMovilReceiver.onReceive() is not implemented")
        Log.d("MIAPP", "En INicioMovilReceiver")
        val intents = Intent(context, ServicioNumeroAleatorio::class.java)
        //lanzaremos un servicio en background
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intents)
        } else {
            context.startService(intents)
        }

    }
}