package edu.vmg.cntgsecux

import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.vmg.cntgsecux.contacto.SeleccionContacto
import edu.vmg.cntgsecux.contacto.SeleccionContactoPermisos
import edu.vmg.cntgsecux.descargacanciones.DescargaCancionesActivity
import edu.vmg.cntgsecux.permisosbasicos.ActividadUno
import edu.vmg.cntgsecux.preferences.FormularioPreferenciasActivity

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
        //startActivity(Intent(this, DescargaCancionesActivity::class.java))
        //startActivity(Intent(this, FormularioPreferenciasActivity::class.java))
        solicitarInicioAutomatico()
    }

    fun solicitarInicioAutomatico ()
    {
        val manufacutrer = Build.MANUFACTURER
        try {
            Log.d("MIAPP", "Fabricante $manufacutrer")
            if("xiaomi".equals (manufacutrer, ignoreCase = true))
            {
                val intent = Intent ()
                intent.setComponent(ComponentName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.autostart.AutoStartManagementActivity"
                ))
                startActivityForResult(intent, 303) //para que me lleve a la actividad de ajuests y permita el inicio automático de esta app
            }

        }catch (e: Exception)
        {
            Log.e("MIAPP", e.message, e)
        }
    }
}