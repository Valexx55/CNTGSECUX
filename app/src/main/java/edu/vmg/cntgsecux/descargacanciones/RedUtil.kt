package edu.vmg.cntgsecux.descargacanciones

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.util.Log

object RedUtil {

    fun hayInternet (context: Context):Boolean
    {
        var hay_internet = false

            //servicio que me permite comprobar el estado de la conectividad
            val connectiviyManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            {
                Log.d("MIAPP", "Comprobando conectividad versión >= 30" )
                val network = connectiviyManager.activeNetwork
                hay_internet = (network != null)
            } else {
                Log.d("MIAPP", "Comprobando conectividad versión < 30" )
                val networkInfo = connectiviyManager.activeNetworkInfo
                if (networkInfo!=null)
                {
                    hay_internet = networkInfo.isAvailable && networkInfo.isConnected
                }
            }

        return hay_internet
    }

    fun isWifiAvailable(context: Context): Boolean {
        var br = false
            var cm: ConnectivityManager? = null
            var ni: NetworkInfo? = null
            cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            ni = cm!!.activeNetworkInfo
            br = null != ni && ni.isConnected && ni.type == ConnectivityManager.TYPE_WIFI
        return br
    }
}