package edu.vmg.cntgsecux.descargacanciones

import retrofit.http.GET
import retrofit.http.Query

interface CancionesService {

    //https://itunes.apple.com/search?media=music&term=fary
    @GET("search?media=music")
    fun obtenerCanciones(@Query("term") busqueda:String):ListaCanciones
}