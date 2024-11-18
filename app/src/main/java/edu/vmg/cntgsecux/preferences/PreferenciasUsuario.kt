package edu.vmg.cntgsecux.preferences

import android.content.Context

object PreferenciasUsuario {

    val NOMBRE_ARCHIVO_PREFERENCIAS = "usuario"

    fun guadarNombre (nombre:String, context: Context)
    {
        val fichero = context.getSharedPreferences(NOMBRE_ARCHIVO_PREFERENCIAS, Context.MODE_PRIVATE)
        val editor = fichero.edit()
        editor.putString("nombre_usuario", nombre)
        editor.commit()
    }

   fun leerNombre(context: Context) :String?
   {
       var nombre:String? = ""

        val fichero = context.getSharedPreferences(NOMBRE_ARCHIVO_PREFERENCIAS, Context.MODE_PRIVATE)
        nombre = fichero.getString("nombre_usuario", "")

       return nombre;

   }


}