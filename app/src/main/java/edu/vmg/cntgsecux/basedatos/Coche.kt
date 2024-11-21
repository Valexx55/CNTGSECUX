package edu.vmg.cntgsecux.basedatos

data class Coche(val id:Int, val modelo:String, val persona:Persona?)
{
    constructor(modelo: String) : this (0, modelo, null)
    constructor(modelo: String, persona: Persona?) : this (0, modelo, persona)

}
