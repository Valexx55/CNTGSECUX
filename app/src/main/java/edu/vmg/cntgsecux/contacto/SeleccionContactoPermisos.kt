package edu.vmg.cntgsecux.contacto

import android.Manifest
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
        //si no, lo pedimos

        val permisoConcedido =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        if (permisoConcedido == PackageManager.PERMISSION_GRANTED) {
            Log.d("MIAPP", "Permiso de leer contactos concedido")
            leerContactos()
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
        when (requestCode) {
            300 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MIAPP", "Permiso concedido")
                    leerContactos()
                } else {
                    Log.d("MIAPP", "Permiso denegado")
                    Toast.makeText(this, "DENEGADO ", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun consultarTodosLosTelefonos() {
        Log.d("MIAPP", "consultarTodosLosTelefonos")
        val telefonos: Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        telefonos?.use {
            var numColumna = 0
            var number: String? = ""

            while (telefonos.moveToNext()) {
                numColumna = telefonos.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                number = telefonos.getString(numColumna)
                Log.d("MIAPP", "Telefono $number")
            }
        }

    }

    private fun leerContactos() {
        //consultarTodosLosTelefonos()
        //CONSULTA DEL CP DE CONTACTOS MÁS A FONDO
        val uriContactos =
            ContactsContract.Contacts.CONTENT_URI //content://com.android.contacts/contacts

        val cursorContactos = contentResolver.query(
            uriContactos,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " = 'Olga'",
            null,
            null,
        )

        cursorContactos?.use {

            if (cursorContactos.moveToFirst()) {
                Log.d("MIAPP", "NUM CONTACTOS = " + cursorContactos.count)

                val numColId = cursorContactos.getColumnIndex(ContactsContract.Contacts._ID)
                val numColNombre =
                    cursorContactos.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)


                val id = cursorContactos.getLong(numColId)
                val nombre = cursorContactos.getString(numColNombre)

                Log.d("MIAPP", "Nombre = $nombre ID = $id")

                var cursorRaw = contentResolver.query(
                    ContactsContract.RawContacts.CONTENT_URI,
                    null,
                    ContactsContract.RawContacts.CONTACT_ID + " = " + id,
                    null,
                    null
                )

                cursorRaw?.use {

                    if (cursorRaw.moveToFirst()) {
                        do {
                            val columnaIdRaw =
                                cursorRaw.getColumnIndex(ContactsContract.RawContacts._ID)
                            val idRaw = cursorRaw.getLong(columnaIdRaw)

                            val tipoIdRaw =
                                cursorRaw.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE)
                            val tipoRaw = cursorRaw.getString(tipoIdRaw)

                            val nombreCuentaIdRaw =
                                cursorRaw.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME)
                            val nombreCuentaRaw = cursorRaw.getString(nombreCuentaIdRaw)

                            Log.d(
                                "MIAPP",
                                "(RAW) NOMBRE CUENTA = $nombreCuentaRaw TIPO CUENTA = $tipoRaw ID = $idRaw"
                            )

                            val cursorData = contentResolver.query(
                                ContactsContract.Data.CONTENT_URI,
                                null,
                                ContactsContract.Data.RAW_CONTACT_ID + " = " + idRaw,
                                null,
                                null
                            )

                            cursorData?.use {
                                if (cursorData.moveToFirst()) {
                                    do {
                                        val tipoMimeCol =
                                            cursorData.getColumnIndex(ContactsContract.Data.MIMETYPE)
                                        val tipoMime = cursorData.getString(tipoMimeCol)
                                        val dataCol =
                                            cursorData.getColumnIndex(ContactsContract.Data.DATA1)
                                        val data = cursorData.getString(dataCol)

                                        Log.d("MIAPP", "   (DATA) MIME = $tipoMime DATA = $data")

                                    } while (cursorData.moveToNext())
                                }
                            }

                        } while (cursorRaw.moveToNext())
                    }
                }

            }

        }

    }

}