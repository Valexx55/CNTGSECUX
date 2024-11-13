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
    private fun leerContactos() {
        //consultarTodosLosTelefonos()
        //CONSULTA DEL CP DE CONTACTOS MÁS A FONDO
        val uri_contactos = ContactsContract.Contacts.CONTENT_URI//content://com.android.contacts/contacts

        val cursor_contactos = contentResolver.query(
            uri_contactos,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " = 'Olga'",
            null,
            null,
        )

        if (cursor_contactos?.moveToFirst() == true)
        {
            Log.d("MIAPP", "NUM CONTACTOS = " + cursor_contactos.count)

            val numColId = cursor_contactos.getColumnIndex(ContactsContract.Contacts._ID)
            val numColNombre = cursor_contactos.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)


            val id = cursor_contactos.getLong(numColId)
            val nombre = cursor_contactos.getString(numColNombre)

            Log.d("MIAPP", "Nombre = $nombre ID = $id")

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
                    val tipo_raw = cursor_raw.getLong(tipoIdRaw)

                    val nombreCuentaIdRaw = cursor_raw.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME)
                    val nombreCuenta_raw = cursor_raw.getLong(nombreCuentaIdRaw)

                    Log.d("MIAPP", "(RAW) NOMBRE CUENTA = $nombreCuenta_raw TIPO CUENTA = $tipo_raw ID = $id_raw")

                    val cursor_data = contentResolver.query(
                        ContactsContract.Data.CONTENT_URI,
                        null,
                        ContactsContract.Data.RAW_CONTACT_ID +" = " + id_raw,
                        null,
                        null
                    )

                    if (cursor_data?.moveToFirst()==true)
                    {
                        do {
                            val tipoMimeCol = cursor_data.getColumnIndex(ContactsContract.Data.MIMETYPE)
                            val tipoMime = cursor_data.getString(tipoMimeCol)
                            val dataCol = cursor_data.getColumnIndex(ContactsContract.Data.DATA1)
                            val data = cursor_data.getString(dataCol)

                            Log.d("MIAPP", "   (DATA) MIME = $tipoMime DATA = $data")

                        } while (cursor_data.moveToNext())
                    }
                    cursor_data?.close()


                } while (cursor_raw.moveToNext())
            }
            cursor_raw?.close()

        }
        cursor_contactos?.close()
    }


}