package edu.vmg.cntgsecux.basedatos

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.vmg.cntgsecux.R

class BaseDatosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_base_datos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val baseDatosCoche = BaseDatosCoche(this, "BDCOCHE", null, 1)

        val persona1 = Persona(1, "Conchi")
        val persona2 = Persona(2, "Manolo")
        val persona3 = Persona(3, "Paco")

        baseDatosCoche.insertarPersona(persona1)
        baseDatosCoche.insertarPersona(persona2)
        baseDatosCoche.insertarPersona(persona3)

        Log.d("MIAPP", "Personas insertadas")

        val coche1 = Coche("FERRARI", persona1)
        val coche2 = Coche("SEAT", persona1)
        val coche3 = Coche("RENAULT", persona2)

        baseDatosCoche.insertarCoche(coche1)
        baseDatosCoche.insertarCoche(coche2)
        baseDatosCoche.insertarCoche(coche3)

        Log.d("MIAPP", "Coches insertados")

        val listaCoches =  baseDatosCoche.obtenerCochesPersona(persona1)

        if (listaCoches!=null)
        {
            Log.d("MIAPP", "La consulta recuperó ${listaCoches.size} coches")
            listaCoches.forEach {
                Log.d("MIAPP", " ${it.modelo} ")
            }
        } else {
            Log.d("MIAPP", "La consulta no recuperó datos")
        }



    }
}