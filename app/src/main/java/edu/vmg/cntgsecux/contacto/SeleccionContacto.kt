package edu.vmg.cntgsecux.contacto

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.vmg.cntgsecux.R

class SeleccionContacto : AppCompatActivity() {

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            Log.d("MIAPP", "A la vuelta de la app de contactos ...")
            if (result.resultCode == RESULT_OK) {
                mostrarDatosConsultaContacto(result.data)
            } else {
                Log.d("MIAPP", "La selección de contacto fue mal")
            }
        }

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
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)

        //forma antigua
        /*if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, 454)
        }*/

        //forma nueva
        if (intent.resolveActivity(packageManager) != null) { //hay alguna app de contactos??
            startForResult.launch(intent)
        } else {
            Log.d("MIAPP", "No hay una app compatible con este Intent")
        }

    }

    //2 hacer la nueva forma de volver de a la aplicación / quitar el método deprecado
    //3 hacer la seleccion de contactos consultado el content provider directamente
    //(para lo cual hacen falta permisos)

    //este método se invoca a la vuelta de la app de contACTOS UNA VEZ Q HE SELECCIONADO UNO
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MIAPP", "A la vuelta de la app de contactos ...")
        if (requestCode == 454 && resultCode == RESULT_OK) {
            mostrarDatosConsultaContacto(data)
        } else {
            Log.d("MIAPP", "La selección de contacto fue mal")
        }
    }

    fun mostrarDatosConsultaContacto(intentResultado: Intent?) {
        val contactUri = intentResultado?.data
        Log.d("MIAPP", "La cosa ha ido bien $contactUri")
        val cursor = contentResolver.query(contactUri!!, null, null, null, null)

        cursor?.use {
            cursor.moveToFirst()
            val columnanumero = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val columnanombre = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            val nombre = cursor.getString(columnanombre)
            val numero = cursor.getString(columnanumero)
            Log.d("MIAPP", "Teléfono Seleccionado $nombre $numero")
        }

    }
}