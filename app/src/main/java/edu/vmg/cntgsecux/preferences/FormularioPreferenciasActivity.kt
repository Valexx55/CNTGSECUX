package edu.vmg.cntgsecux.preferences

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.vmg.cntgsecux.R
import edu.vmg.cntgsecux.databinding.ActivityFormularioPreferenciasBinding

class FormularioPreferenciasActivity : AppCompatActivity() {

    /**
     * 1
     * TODO completar la clase de Preferencias para guardar
     * todos los datos del formulario
     *
     * y que se carguen en la actividad si están guardados
     *
     * ---
     *
     * 2 tener los datos del usuario en una clase data
     * y guardarlos en una sola clave como JSON
     *
     *
     */

    lateinit var bindingVistas: ActivityFormularioPreferenciasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nombreGuaradado =  PreferenciasUsuario.leerNombre(this)
        if (nombreGuaradado != null)
        {
            Log.d("MIAPP", "El usuario ya rellenó el formulario, existe el nombre")
            val intent = Intent(this , BienvenidaActivity::class.java)
            intent.putExtra("nombre", nombreGuaradado)
            startActivity(intent)
        } else {
            Log.d("MIAPP", "El usuario NO rellenó el formulario, primera vez - sin nombre -")
            bindingVistas = ActivityFormularioPreferenciasBinding.inflate(layoutInflater)
            enableEdgeToEdge()
            val view = bindingVistas.root
            setContentView(view)
            //setContentView(R.layout.activity_formulario_preferencias)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }






    }

    fun botonGuardarFormulario(view: View) {
        Log.d("MIAPP", "Boton guardar formulario tocado")
        val nombre :String = bindingVistas.textnombre.text.toString()
        val edad = bindingVistas.textedad.text.toString()
        val sexoMasculino = bindingVistas.radioButonHombre.isChecked
        val sexoFemenino = bindingVistas.radioButonMujer.isChecked
        val mayorEdad = bindingVistas.checkBox.isChecked

        Log.d("MIAPP" , "INFO persona Nombre = $nombre Edad = $edad Masculino = $sexoMasculino Femenino = $sexoFemenino Mayor de edad = $mayorEdad")

        PreferenciasUsuario.guadarNombre(nombre, this)
    }



}