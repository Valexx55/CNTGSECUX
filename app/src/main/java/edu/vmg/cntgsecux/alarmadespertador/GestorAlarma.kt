package edu.vmg.cntgsecux.alarmadespertador

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar

object GestorAlarma {

    fun programarAlamarma (context: Context)
    {
        //calculo el tiempo
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val tiempo = Calendar.getInstance().timeInMillis + 10000 //tiempo actual + 10 segundos

        //programo el receptor - el que escuha la alarma
        val intent = Intent(context, AlarmaReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,55, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        //programo la alarma
        alarmManager.set(AlarmManager.RTC_WAKEUP, tiempo, pendingIntent)



        //mensaje informativo

        val dateformater =  SimpleDateFormat("E dd/MM/yyyy ' a las ' hh:mm:ss")
        val mensaje =  dateformater.format(tiempo)
        Log.d("MIAPP", mensaje)
        Toast.makeText(context, "Alarma programada para " +mensaje, Toast.LENGTH_LONG).show()


    }
}