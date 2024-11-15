package edu.vmg.cntgsecux.descargacanciones

import android.app.DownloadManager
import android.app.DownloadManager.Request
import android.content.Context
import android.content.IntentFilter
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.VERTICAL

class DescargaCancionesActivity : AppCompatActivity(), OnQueryTextListener {

    lateinit var service: CancionesService
    lateinit var listadoCanciones: ListaCanciones
    lateinit var searchView: SearchView
    lateinit var progressBar: ProgressBar
    lateinit var recycler: RecyclerView
    lateinit var numResultados: TextView
    lateinit var urlCancion: String
    lateinit var adapter: RecyclerView.Adapter<CancionViewHolder>


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
        this.recycler = findViewById(R.id.recView)
        this.numResultados = findViewById(R.id.nresultados)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        try {

            Log.d("MIAPP", "onQueryTextSubmit")

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
                    actualizarListaCanciones(listadoCanciones)

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
        //TODO si newText es vacío, limpio la lista
        if (newText?.isEmpty()==true)
        {
            ponerListaVacia()
        }
        return false
    }

    fun actualizarListaCanciones (listaCanciones: ListaCanciones)
    {
        if (listaCanciones.resultCount > 0)
        {
            this.numResultados.text = "${listaCanciones.resultCount} resultados"
            this.adapter = AdapterCanciones(listaCanciones)
            this.recycler.adapter = this.adapter //los datos se representan automáticamente
            this.recycler.layoutManager= LinearLayoutManager(this, VERTICAL, false)//estilo
        } else {
            Toast.makeText(this, "CONSULTA sin resultados", Toast.LENGTH_LONG).show()
            ponerListaVacia()
        }
        this.numResultados.text = "${listaCanciones.resultCount} resultados"

    }

    fun ponerListaVacia()
    {
        this.adapter = AdapterCanciones(ListaCanciones(0, mutableListOf()))
        this.recycler.adapter = this.adapter //los datos se representan automáticamente
        this.numResultados.text = "0 resultados"
    }

    fun reproducirCancion(view: View) {

        val urlcancion =  view.tag as String//obtengo la canción asociada a ese botón
        Log.d("MIAPP", "Toca reproducir canción $urlcancion")
        val uri = Uri.parse(urlcancion)
        val mediaPlayer = MediaPlayer.create(this, uri)
        mediaPlayer.start()//reproduzco la canción
    }
    fun descargarCancion(view: View) {
        val urlcancion =  view.tag as String//obtengo la canción asociada a ese botón
        //1 preparar la descarga
        Log.d("MIAPP", "preparo descarga")
        var peticion = prepararDescarga(urlcancion)
        //1.5 preparar el receiver
        Log.d("MIAPP", "preparo receptor")
        var descargaReceiver = DescargaReceiver()
        var intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)//evento del que está pendiente el receptor

        ContextCompat.registerReceiver(this, descargaReceiver, intentFilter,
            ContextCompat.RECEIVER_EXPORTED
        )

        //2 solitar la descarga
        Log.d("MIAPP", "solicito descarga")
        var downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        var idDescarga:Long =  downloadManager.enqueue(peticion)//descargate esto (peticion)

        descargaReceiver.idDescarga = idDescarga//dentro del receptor guaardo el id de descarga para saber que es el mío
        descargaReceiver.context = this//paso la pantalla, para luego vovler a ella e informar del resultado de la descarga
    }

    fun actualizarEstadoDescarga (estadoDescarga:Int)
    {
        if (estadoDescarga == DownloadManager.STATUS_FAILED)
        {
            Log.d("MIAPP", "La descarga fue mal")
        } else {
            Log.d("MIAPP", "La descarga fue bien")
        }
    }

    fun prepararDescarga (url: String): Request
    {
        var peticion: Request

                peticion = DownloadManager.Request(Uri.parse(url))//qué descargo
                peticion.setMimeType("audio/mp3")//tipo de info/archivo descargada
                peticion.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)//nos muestra la notificación mientras y al final de la descarga
                peticion.setTitle("cancionitunes") //TODO guardar con el nombre de la canción elegida
                peticion.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "cancion1.mp3")

        return peticion
    }
}