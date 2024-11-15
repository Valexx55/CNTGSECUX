package edu.vmg.cntgsecux.descargacanciones

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView.Recycler
import edu.vmg.cntgsecux.R
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.appcompat.widget.SearchView

class DescargaCancionesActivity : AppCompatActivity(), OnQueryTextListener {

    lateinit var service: CancionesService
    lateinit var listadoCanciones: ListaCanciones
    lateinit var searchView: SearchView
    lateinit var progressBar: ProgressBar
    lateinit var recycler: Recycler
    lateinit var numResultados: TextView
    lateinit var urlCancion: String
    //lateinit var : ListaCanciones


    override fun onCreate(savedInstanceState: Bundle?) {
        try {


            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.activity_descarga_canciones)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
            iniciarControles()
        } catch (e: Exception) {
            Log.e("MIAPP", e.message, e)
        }
    }

    fun iniciarControles() {
        this.searchView = findViewById(R.id.cajaBuscaCanciones)
        this.searchView.setOnQueryTextListener(this)
        this.progressBar = findViewById(R.id.pb)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        try {

            Log.d("MIAPP", "onQueryTextSubmit")
            //TODO conectarme al API de ITUNES
            //1 comprobar si tengo conexión a INTERNET
            if (RedUtil.hayInternet(this)) {
                Log.d("MIAPP", "Hay internet, a consultar")

                var retrofit = Retrofit.Builder()
                    .baseUrl("https://itunes.apple.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                service = retrofit.create(CancionesService::class.java)

                lifecycleScope.launch {
                    progressBar.visibility = View.VISIBLE
                    listadoCanciones = service.obtenerCanciones(query ?: "")//operador Elvis
                    Log.d("MIAPP", "Listado canciones $listadoCanciones")
                    progressBar.visibility = View.INVISIBLE
                    //TODO pintar la lista / recycler
                }


            } else {
                Log.d("MIAPP", "NO Hay internet")
                Toast.makeText(this, "SIN CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show()

            }
        } catch (e: Exception) {
            Log.e("MIAPP", e.message, e)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d("MIAPP", "onQueryTextChange")
        return false
    }
}