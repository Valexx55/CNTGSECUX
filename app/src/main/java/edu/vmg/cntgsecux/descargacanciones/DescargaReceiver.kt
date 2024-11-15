package edu.vmg.cntgsecux.descargacanciones

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Esta clase "escuchará el final de la descarga de nuestra canción
 */
class DescargaReceiver : BroadcastReceiver() {

    var idDescarga:Long = -1 //el id de la descarga
    lateinit var context: Context //la actividad donde se inicia la descarga

    override fun onReceive(context: Context, intent: Intent) {
        //este método se invoca al acabar una descargar
        Log.d("MIAPP", "Descarga Finalizada")
        var downloadmanager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val consulta = DownloadManager.Query()//consulta
        consulta.setFilterById(idDescarga)//
        var cursor = downloadmanager.query(consulta)
        cursor?.use {
            if (cursor.moveToFirst())
            {
                val ncol = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val status = cursor.getInt(ncol)
                (context as DescargaCancionesActivity).actualizarEstadoDescarga(status)
                context.unregisterReceiver(this)//hay que deregistrar / deje de escuchar
            }
        }
    }
}