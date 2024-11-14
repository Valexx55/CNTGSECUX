package edu.vmg.cntgsecux.contacto

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.vmg.cntgsecux.R

/**
 * Esta activitidad, recupera información de Conctactos, a través de la aplicación
 * de Contactos del Sistema. No necesita permisos.
 *
 */
class SeleccionContactoPermisos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seleccion_contacto_permisos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //comprobamos que el permiso está concecido
        //si está concecido, le los contactos, usando una URI del Content Provider
        //si no , lo pedimos


        val permisoConcedido = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (permisoConcedido == PackageManager.PERMISSION_GRANTED)
        {
            Log.d("MIAPP", "Permiso de leer contactos concedido")
            leerContactos ()
        } else {
            Log.d("MIAPP", "Permiso de leer contactos NO concedido, pedimos permisos")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 300)
        }

       // leerContactos() // SI ACCEDO DIRECTAMENTE A LA CONSULTA DEL CONTENTO PROVIDER DE CONTACTOS, LA APP FALLARÁ. ES UN PERMISO UNO O PELIGROSO, DEBO DELCARLO EN EL MANIFEST Y ADEMÁS, PEDIRLO EN EJEUCICIÓN



    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("MIAPP", "A la vuelta de pedir el permiso")
        when (requestCode)
        {
            300 -> {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    Log.d("MIAPP", "Permiso concedido")
                    leerContactos()
                } else {
                    Log.d("MIAPP", "Permiso denegado")
                    Toast.makeText(this, "DENEGADO ", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun consultarTodosLosTelefonos () :Unit
    {
        Log.d("MIAPP", "consultarTodosLosTelefonos")
        val telefonos : Cursor? =  contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        var numcolumna = 0
        var number : String ? = ""

        while (telefonos?.moveToNext()==true)
        {
            numcolumna = telefonos.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            number = telefonos.getString(numcolumna)
            Log.d("MIAPP", "Telefono $number")
        }
        telefonos?.close()//Cierro el cursor!

    }
    //TODO
    // 1 mejorar con la sintaxis de use (opcional )
    // 2 hacer el código más pequeño mejor estructurado (más funciones) refactorizarlo
    // 3 haced que detalle las cuentas y el data (detalle) para más de un contacto / iterar cursor contactos
    private fun leerContactos() {
        //consultarTodosLosTelefonos()
        //CONSULTA DEL CP DE CONTACTOS MÁS A FONDO
        //String[] prefijo = {"M%"};
        //Cursor cursor_contactos = contentResolver.query(uri_contactos, null, ContactsContract.Contacts.DISPLAY_NAME +" LIKE ?" ,prefijo, null); //Selecciono todas las columnas, de todos


        //mostrarContactos("Olga")
        mostrarContactos("M%")

    }

    fun mostrarContactos (prefijo : String)
    {
        val uri_contactos = ContactsContract.Contacts.CONTENT_URI//content://com.android.contacts/contacts
        val cursor_contactos = contentResolver.query(
            uri_contactos,
            null,
            ContactsContract.Contacts.DISPLAY_NAME +" LIKE ?",
            arrayOf(prefijo),
            null
        )
        cursor_contactos.use {
            if (it?.moveToFirst() == true)
            {
                do {
                    Log.d("MIAPP", "NUM CONTACTOS = " + it.count)

                    val numColId = it.getColumnIndex(ContactsContract.Contacts._ID)
                    val numColNombre = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

                    val id = it.getLong(numColId)
                    val nombre = it.getString(numColNombre)

                    Log.d("MIAPP", "Nombre = $nombre ID = $id")
                    mostrarCuentaRaw(id)
                } while (it.moveToNext())
            }
        }

    }


    fun mostrarCuentaRaw (id:Long):Unit
    {
        var cursor_raw = contentResolver.query(
            ContactsContract.RawContacts.CONTENT_URI,
            null,
            ContactsContract.RawContacts.CONTACT_ID + " = " +id,
            null,
            null
        )
        if (cursor_raw?.moveToFirst()==true){
            do {
                val columnaIdRaw = cursor_raw.getColumnIndex(ContactsContract.RawContacts._ID)
                val id_raw = cursor_raw.getLong(columnaIdRaw)

                val tipoIdRaw = cursor_raw.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE)
                val tipo_raw = cursor_raw.getString (tipoIdRaw)

                val nombreCuentaIdRaw = cursor_raw.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME)
                val nombreCuenta_raw = cursor_raw.getString(nombreCuentaIdRaw)

                Log.d("MIAPP", "(RAW) NOMBRE CUENTA = $nombreCuenta_raw TIPO CUENTA = $tipo_raw ID = $id_raw")

                mostrarDetalle(id_raw)


            } while (cursor_raw.moveToNext())
        }
        cursor_raw?.close()
    }

    fun mostrarDetalle (id_raw :Long):Unit
    {
        val cursor_data = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            ContactsContract.Data.RAW_CONTACT_ID +" = " + id_raw,
            null,
            null
        )

        cursor_data?.use {
            //it es el cursor

            if (it.moveToFirst()==true)
            {
                do {
                    val tipoMimeCol = it.getColumnIndex(ContactsContract.Data.MIMETYPE)
                    val tipoMime = it.getString(tipoMimeCol)
                    val dataCol = it.getColumnIndex(ContactsContract.Data.DATA1)
                    val data = it.getString(dataCol)

                    Log.d("MIAPP", "   (DATA) MIME = $tipoMime DATA = $data")

                } while (it.moveToNext())
            }
        } //se cierra el cursor automáticamente si lo uso con use

    }

}