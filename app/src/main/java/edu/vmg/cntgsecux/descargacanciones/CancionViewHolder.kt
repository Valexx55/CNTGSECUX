package edu.vmg.cntgsecux.descargacanciones

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import edu.vmg.cntgsecux.R

class CancionViewHolder (fila:View) : ViewHolder(fila) {

    val tituloCancion:TextView = itemView.findViewById(R.id.tituloCancion)
    val nombreArtista:TextView = itemView.findViewById(R.id.nombreArtista)
    val iconoReproducir:ImageView = itemView.findViewById(R.id.icreproducirCancion)
    val iconoDescargar:ImageView = itemView.findViewById(R.id.icdescargarCancion)

    fun cargarCancionEnHolder (cancion: Cancion)
    {
        tituloCancion.text = cancion.trackName //TODO ajsutar el tamanño máximo
        nombreArtista.text = cancion.artistName
        iconoDescargar.tag = cancion.previewUrl
        iconoReproducir.tag = cancion.previewUrl

    }

}