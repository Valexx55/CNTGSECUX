package edu.vmg.cntgsecux.contacto

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.vmg.cntgsecux.R

class SeleccionContacto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seleccion_contacto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        selectContact()
    }


    fun selectContact() {
        //"LANZAME LA APP DE CONTACTOS+
        Log.d("MIAPP", "Lanzando la app de contactos ...")
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = ContactsContract.Contacts.CONTENT_TYPE
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, 454)
        }
    }

    //este método se invoca a la vuelta de la app de contACTOS UNA VEZ Q HE SELECCIONADO UNO
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MIAPP", "A la vuelta de la app de contactos ...")
        if (requestCode == 454 && resultCode == RESULT_OK) {
            val contactUri = data?.data
            Log.d("MIAPP", "La cosa ha ido bien $contactUri")
            //TODO 1 recuperar info del contacto del seleccionado y mostrarlo
            //2 hacer la nueva forma de volver de a la aplicación / quitar el método deprecado
            //3 hacer la seleccion de contactos consultado el content provider directamente
            //(para lo cual hacen falta permisos)

        }
    }
}