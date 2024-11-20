package edu.vmg.cntgsecux.foto

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.vmg.cntgsecux.Manifest
import edu.vmg.cntgsecux.R
import java.text.SimpleDateFormat
import java.util.Date

class FotoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_foto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun tomarFoto(view: View) {
        pedirPermisos()
    }

    fun  pedirPermisos()
    {
        val array_permisos = arrayOf(android.Manifest.permission.CAMERA)

        requestPermissions(array_permisos, 510)
    }

    fun obtenerRutaFichero(): Uri?
    {
        var uri:Uri? = null

            //generamos el nombre del fichero de la foto con la fecha actual
            val fecha_actual = Date()
            val momento_actual = SimpleDateFormat("yyyyMMdd_HHmmss").format(fecha_actual)
            val nombre_fichero = "FOTO_CNT_$momento_actual"

            Log.d("MIAPP", "NOMBRE FICHERO GENERADO = $nombre_fichero")
            var ruta_foto = getExternalFilesDir(null)?.path+"/"+nombre_fichero
            Log.d("MIAPP", "RUTA COMPLETA FICHERO GENERADA = $ruta_foto")

            //tengo que declarar un Content Provider para almacenar mis fotos
            //de manera que la app de la cámara use esa "ruta pública" - de mentira -
            //y esté asociada a la ruta física/privada, propia de mi app - FileProvider

        return uri
    }

    fun lanzarCamara()
    {
        //1 CREAR UN FICHERO DESTINO (URI)
        //2 INTENT IMPLÍCITO PARA USAR LA APP DE LA CÁMARA
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults [0]== PackageManager.PERMISSION_GRANTED)
        {
            Log.d("MIAPP", "Permiso de la cámara concedido")
            lanzarCamara()
        } else {
            Log.d("MIAPP", "Permiso de la cámara DENEGADO")
            Toast.makeText(this, "Permiso de la cámara DENEGADO", Toast.LENGTH_LONG).show()
        }

    }
}