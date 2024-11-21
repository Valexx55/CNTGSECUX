package edu.vmg.cntgsecux.foto

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.vmg.cntgsecux.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class FotoActivity : AppCompatActivity() {

    var uri_foto:Uri? = null

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

    fun pedirPermisos() {
        val array_permisos = arrayOf(android.Manifest.permission.CAMERA)

        requestPermissions(array_permisos, 510)
    }

    /**
     * Genera el fichero destino de la foto tomada
     * y devuelve la uri pública del File Provider
     */
    fun obtenerRutaFichero(): Uri? {
        var uri: Uri? = null

        //generamos el nombre del fichero de la foto con la fecha actual
        val fecha_actual = Date()
        val momento_actual = SimpleDateFormat("yyyyMMdd_HHmmss").format(fecha_actual)
        val nombre_fichero = "FOTO_CNT_$momento_actual"



        Log.d("MIAPP", "NOMBRE FICHERO GENERADO = $nombre_fichero")
        //var ruta_foto = getExternalFilesDir(null)?.path + "/" + nombre_fichero /// ruta externa de la app storage/emulated/0/Android/data/edu.vmg.cntgsecux/files/FOTO_CNT_20241121_175448
        //var ruta_foto = Environment.getExternalStorageDirectory()?.path + "/" + nombre_fichero // ruta exter publica /storage/emulated/0/FOTO_CNT_20241121_175923 da fallo, no puedo guardar las fotos en la zona pública de la memoria externa
        //var ruta_foto = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/" + nombre_fichero // ruta externa publica de directoria de descargas /storage/emulated/0/Download/FOTO_CNT_20241121_180540
        var ruta_foto = filesDir.path + "/" + nombre_fichero //ruta privada memoria interna  /data/user/0/edu.vmg.cntgsecux/files/FOTO_CNT_20241121_181307
        Log.d("MIAPP", "RUTA COMPLETA FICHERO GENERADA = $ruta_foto")

        //tengo que declarar un Content Provider para almacenar mis fotos
        //de manera que la app de la cámara use esa "ruta pública" - de mentira -
        //y esté asociada a la ruta física/privada, propia de mi app - FileProvider
        val file = File(ruta_foto)
        try {
            file.createNewFile() //creo el fichero
            uri = FileProvider.getUriForFile(this, "edu.vmg.cntgsecux", file)
            Log.d("MIAPP", "RUTA uri PÚBLICA = $uri")
        } catch (e: Throwable) {
            Log.e("MIAPP", "error generando el fichero de destino", e)
        }


        return uri
    }

    fun lanzarCamara() {
        //1 CREAR UN FICHERO DESTINO (URI)
        this.uri_foto = obtenerRutaFichero()
        //2 INTENT IMPLÍCITO PARA USAR LA APP DE LA CÁMARA
        if ( this.uri_foto != null) {
            val intent = Intent()
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT,  this.uri_foto)
            startActivityForResult(intent, 33)
        } else {
            Toast.makeText(this, "NO ES POSIBLE LANZAR LA CÁMARA", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK)
        {
            Log.d("MIAPP", "foto bien tirada")
            val imageView = findViewById<ImageView>(R.id.fotoTomada)
            imageView.setImageURI(this.uri_foto)

        } else {
            Log.d("MIAPP", "foto cancelada")
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("MIAPP", "Permiso de la cámara concedido")
            lanzarCamara()
        } else {
            Log.d("MIAPP", "Permiso de la cámara DENEGADO")
            Toast.makeText(this, "Permiso de la cámara DENEGADO", Toast.LENGTH_LONG).show()
        }

    }
}