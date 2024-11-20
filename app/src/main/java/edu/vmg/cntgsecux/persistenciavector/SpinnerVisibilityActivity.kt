package edu.vmg.cntgsecux.persistenciavector

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.vmg.cntgsecux.R

class SpinnerVisibilityActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var spinner: Spinner
    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_spinner_visibility)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val adapterSpinner = ArrayAdapter.createFromResource(this,
            R.array.array_opciones_visibilidad,
            android.R.layout.simple_spinner_item)

        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        this.spinner = findViewById(R.id.spinner)
        this.spinner.adapter = adapterSpinner

        this.spinner.onItemSelectedListener = this

        this.imageView = findViewById(R.id.imagenMuestra)


    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("MIAPP", "Spinner seleccionado")
        val textoActivo = (view as TextView).text.toString()
        Log.d("MIAPP", "Opción tocada = $textoActivo")


        when (textoActivo)
        {
            "VISIBLE" -> this.imageView.visibility = View.VISIBLE
            "INVISIBLE" -> this.imageView.visibility = View.INVISIBLE
            "GONE" -> this.imageView.visibility = View.GONE
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
      //CUANDO EL ADAPTER CAMBIA Y UNA OPCIÓN SELECCIONADA DEJA DE ESTAR DISPONIBLE
    }
}