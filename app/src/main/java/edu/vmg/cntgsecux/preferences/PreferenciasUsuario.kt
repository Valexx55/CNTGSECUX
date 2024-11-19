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

    /**
     * Recuepero el nombre del fichero de propiedades
     * @return un string con el valor o null si no existe
     */
   fun leerNombre(context: Context) :String?
   {
       var nombre:String? = null

        val fichero = context.getSharedPreferences(NOMBRE_ARCHIVO_PREFERENCIAS, Context.MODE_PRIVATE)
        nombre = fichero.getString("nombre_usuario", null)

       return nombre;

   }


}