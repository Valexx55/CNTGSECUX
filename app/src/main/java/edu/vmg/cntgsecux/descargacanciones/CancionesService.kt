package edu.vmg.cntgsecux.descargacanciones

import retrofit2.http.GET
import retrofit2.http.Query

interface CancionesService {

    //https://itunes.apple.com/search?media=music&term=fary
    @GET("search?media=music")
    suspend fun obtenerCanciones(@Query("term") busqueda:String):ListaCanciones
}