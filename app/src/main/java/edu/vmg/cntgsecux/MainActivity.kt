package edu.vmg.cntgsecux

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.vmg.cntgsecux.contacto.SeleccionContacto
import edu.vmg.cntgsecux.contacto.SeleccionContactoPermisos
import edu.vmg.cntgsecux.descargacanciones.DescargaCancionesActivity
import edu.vmg.cntgsecux.permisosbasicos.ActividadUno

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //val intent = Intent (this, ActividadUno::class.java)
        //"navego a la actividad 1"
        //startActivity(intent) //lanzo la actividad destino
        //startActivity(Intent(this, SeleccionContacto::class.java))
        //startActivity(Intent(this, SeleccionContactoPermisos::class.java))
        startActivity(Intent(this, DescargaCancionesActivity::class.java))
    }
}