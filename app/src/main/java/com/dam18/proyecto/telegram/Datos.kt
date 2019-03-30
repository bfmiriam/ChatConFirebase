package com.dam18.proyecto.telegram

import java.util.*

/**
 * Para guardar los valores que quiero introducir/actualizar en la base de datos
 * Contiene un HashMap con los datos, ya que las funciones que utilizaré necesitan como parámetro
 * un HashMap
 */
//recogemos los datos de la tabla mensajes
data class Datos(var destinatario:String, var mensaje: String, var hora: Date ) {
    // contenedor para actualizar los datos
    val miHashMapDatos = HashMap<String, Any>()

    /**
     * Mete los datos del objeto en el HashMap
     */
    fun crearHashMapDatos() {
        miHashMapDatos.put("destinatario",destinatario)
        miHashMapDatos.put("mensaje",mensaje)
        miHashMapDatos.put("hora", hora)
    }
}