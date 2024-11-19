package edu.vmg.cntgsecux.alarmadespertador

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class FinServicioReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        //TODO("FinServicioReceiver.onReceive() is not implemented")
        Log.d("MIAPP", "Servicio finalizado")
        //inspeccionamos el intent y vemos el valor del número generado
        //como si fuera la idea "hay mensajes o no"
        val numeroAleatorio = intent.getIntExtra("NUM_ALEATORIO", -1)
        Log.d("MIAPP", "Número generado = $numeroAleatorio")

        if (numeroAleatorio >= 60)
        {//
            Notificaciones.lanzarNotificacion(context)
            Log.d("MIAPP", "El serivicio, nos da un número mayor de 60, hay mensaje")
        }

        //programo la alarma
        GestorAlarma.programarAlamarma(context)
        Log.d("MIAPP", "Reprogramo alarma")

        //desregistar el receptor
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this)

    }
}