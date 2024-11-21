package edu.vmg.cntgsecux.basedatos

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatosCoche (context:Context, name:String, factory: SQLiteDatabase.CursorFactory?, version:Int) : SQLiteOpenHelper(context, name, factory, version) {

    val SQL_CREAR_TABLA_PERSONAS: String =
        "CREATE TABLE PERSONA (id INTEGER PRIMARY KEY, nombre TEXT)"
    val SQL_CREAR_TABLA_COCHES: String =
        "CREATE TABLE COCHE (id INTEGER PRIMARY KEY AUTOINCREMENT, modelo TEXT, idpersona INTEGER, FOREIGN KEY (idpersona) REFERENCES PERSONA (id))"

    override fun onCreate(db: SQLiteDatabase) {
        //TODO("Not yet implemented")
        //ejecutar las sentencias DDL (data definition lenguage)
        db.execSQL(SQL_CREAR_TABLA_PERSONAS)
        db.execSQL(SQL_CREAR_TABLA_COCHES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //TODO("Not yet implemented")
        //recuperacion, creacion de las tablas nuevas y el volcado
    }

    fun insertarPersona (persona: Persona)
    {
        val insertarPersona: String = "INSERT INTO PERSONA (id, nombre) VALUES ( ${persona.id} , '${persona.nombre}')"
        val baseDatos =  this.writableDatabase

        baseDatos.execSQL(insertarPersona)
        baseDatos.close()
    }

    fun insertarCoche (coche: Coche)
    {

        val insertarCoche: String = "INSERT INTO COCHE (modelo, idpersona) VALUES ( '${coche.modelo}' , ${coche.persona?.id})"
        val baseDatos =  this.writableDatabase

        baseDatos.execSQL(insertarCoche)
        baseDatos.close()
    }

    fun obtenerCochesPersona (persona:Persona):List<Coche>?
    {
        var cocheList: MutableList<Coche>? = null
        val instruccionConsulta : String = "SELECT modelo FROM COCHE WHERE idpersona = ${persona.id}"
        var modelo_aux:String
        var coche_aux:Coche

            var sqLiteDatabase = this.readableDatabase//accedo en modo lectura
            var cursor =  sqLiteDatabase.rawQuery(instruccionConsulta, null)
            sqLiteDatabase.use {
                cursor.use {
                    if (cursor != null && cursor.count > 0)
                    {
                        cursor.moveToFirst()
                        cocheList = mutableListOf<Coche>()//creo la lista vacía
                        do {
                            modelo_aux = cursor.getString(0)
                            coche_aux = Coche(modelo_aux, persona)
                            cocheList?.add(coche_aux)

                        }while (cursor.moveToNext())
                    }
                }
            }

        return cocheList
    }

}