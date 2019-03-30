package com.dam18.proyecto.telegram

import java.util.*

/**
 * Para guardar los valores que quiero introducir/actualizar en la base de datos
 * Contiene un HashMap con los datos, ya que las funciones que utilizaré necesitan como parámetro
 * un HashMap
 */
//recogemos los datos de la tabla usuarios
data class Usuarios(var token: String, var usuario: String) {
    // contenedor para actualizar los datos
    val miHashMapUsuarios = HashMap<String, Any>()

    /**
     * Mete los datos del objeto en el HashMap
     */
    fun crearHashMapDatos() {
        miHashMapUsuarios.put("token", token)
        miHashMapUsuarios.put("usuario",usuario)
    }
}