package edu.vmg.cntgsecux.contacto

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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

    private fun leerContactos() {
        //TODO("Not yet implemented")
    }


}