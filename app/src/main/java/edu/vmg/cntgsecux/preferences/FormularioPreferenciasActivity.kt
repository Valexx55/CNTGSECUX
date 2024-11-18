package edu.vmg.cntgsecux.preferences

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

    lateinit var bindingVistas: ActivityFormularioPreferenciasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingVistas = ActivityFormularioPreferenciasBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formulario_preferencias)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }

    fun botonGuardarFormulario(view: View) {
        Log.d("MIAPP", "Boton guardar formulario tocado")
        val nombre = bindingVistas.tilnombre.editText?.text.toString()
        val edad = bindingVistas.tiledad.editText?.text.toString()
        val sexoMasculino = bindingVistas.radioButonHombre.isChecked
        val sexoFemenino = bindingVistas.radioButonMujer.isChecked
        val mayorEdad = bindingVistas.checkBox.isChecked

        Log.d("MIAPP" , "INFO persona Nombre = $nombre Edad = $edad Masculino = $sexoMasculino Femenino = $sexoFemenino Mayor de edad = $mayorEdad")

        PreferenciasUsuario.guadarNombre(nombre, this)
    }



}