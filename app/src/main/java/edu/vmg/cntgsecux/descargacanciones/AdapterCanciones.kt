package edu.vmg.cntgsecux.descargacanciones

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import edu.vmg.cntgsecux.R

class AdapterCanciones (var listaCanciones: ListaCanciones): Adapter<CancionViewHolder>() {



        //"Creo la caja" - fila vacía
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CancionViewHolder {
        var cancionViewHolder: CancionViewHolder

            val layoutInflater = LayoutInflater.from(parent.context)
            val filanueva =  layoutInflater.inflate(R.layout.fila_cancion, parent, false)
            cancionViewHolder = CancionViewHolder(filanueva)


        return cancionViewHolder
    }


    //"Relleno la caja" - la fila con la información
    override fun onBindViewHolder(holder: CancionViewHolder, position: Int) {

        val cancion = listaCanciones.results[position]
        holder.cargarCancionEnHolder(cancion)

    }

    override fun getItemCount(): Int {
       return listaCanciones.resultCount//indico cuántas filas voy a pintar
    }
}