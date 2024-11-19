package edu.vmg.cntgsecux.alarmadespertador

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class AlarmaReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Log.d("MIAPP", "En AlarmaReceiver")
        val intent_service = Intent(context, ServicioNumeroAleatorio::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent_service)
        } else {
            context.startService(intent_service)
        }
    }
}