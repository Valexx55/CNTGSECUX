package edu.vmg.cntgsecux.alarmadespertador

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class ServicioNumeroAleatorio : Service() {

    var numeroAleatorio: Int = 0;
    //este método es el que el ejecuta el servicio
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //return super.onStartCommand(intent, flags, startId)

        //programamos la escucha del final del serivicio
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        val finServicioReceiver = FinServicioReceiver()
        val intentFilter = IntentFilter("SERV_ALEATORIO_FINAL")
        //"nuestro receptor está pendiente de esa señal intent filter"
        localBroadcastManager.registerReceiver(finServicioReceiver, intentFilter)

        val notificacionSegundoPlano = Notificaciones.crearNotificacionSegundoPlano(this)
        startForeground(65, notificacionSegundoPlano)

        try{
            //simulamos que consumimos un API
            Thread.sleep(5000)
            numeroAleatorio = (Math.random()*100+1).toInt()

        }catch (e:Exception)
        {
            Log.e("MIAPP", e.message, e)
        }

        stopForeground(false)
        stopSelf()//detenemos el servicio

        return START_NOT_STICKY//se ejecuta el servicio no se reinicia
    }

    //este método se ejecuta el finalizar el sericio
    override fun onDestroy() {

        val intent_fin = Intent("SERV_ALEATORIO_FINAL")
        intent_fin.putExtra("NUM_ALEATORIO", this.numeroAleatorio)
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        //lanzamos la señal, como diciendo que ha acabado nuestro servicio
        localBroadcastManager.sendBroadcast(intent_fin)

        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")

    }
}