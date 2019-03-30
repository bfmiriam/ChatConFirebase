package com.dam18.proyecto.telegram

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat2.*
import java.util.*

class ActivityChat : AppCompatActivity() {

    // para filtrar los logs
    val TAG = "Servicio"

    // referencia de la base de datos
    private var database: DatabaseReference? = null
    // Token del dispositivo
    //  private var FCMToken: String? = null
    // key unica creada automaticamente al añadir un child
    // lateinit var misDatos: Datos
    lateinit var key: String
    lateinit var misDatos: Datos
    // para actualizar los datos necesito un hash map
    val miHashMapChild = HashMap<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat2)
        enviar.setOnClickListener {


            // referencia a la base de datos del proyecto en firebase
            database = FirebaseDatabase.getInstance().getReference("/mensajes")


            // solo lo llamo cuando arranco la app
            // evito que cuando se pasa por el onCreate vuelva a ejecutarse
            if (savedInstanceState == null) try {

                // creamos una entrada nueva en el child "mensajes" con un key unico automatico
                key = database!!.push().key!!
                // guardamos el destinatario,el mensaje y la fecha en la data class
                misDatos = Datos(
                    destinatario.text.toString(), mensaje.text.toString(), Date()
                )
                // creamos el hash map
                misDatos.crearHashMapDatos()
                // guardamos los datos en el hash map para la key creada anteriormente
                miHashMapChild.put(key.toString(), misDatos.miHashMapDatos)
                // actualizamos el child
                database!!.updateChildren(miHashMapChild)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG, "Error escribiendo datos ${e}")
            }
            // inicializo el listener para los eventos de la basededatos
            initListener()
            destinatario.text.clear()
            mensaje.text.clear()
        }
    }
    /**
     * Listener para los distintos eventos de la base de datos
     */
    private fun initListener() {
        val childEventListener = object : ChildEventListener {
            override fun onChildRemoved(p0: DataSnapshot) {
                Log.d(TAG, "Datos borrados: " + p0.key)
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                Log.d(TAG, "Datos cambiados: " + (p0.getValue() as HashMap<*, *>).toString())
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                Log.d(TAG, "Datos movidos")
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                // onChildAdded() capturamos la key
                Log.d(TAG, "Datos añadidos: " + p0.key)

            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d(TAG, "Error cancelacion")
            }
        }
        // attach el evenListener a la basededatos
        database!!.addChildEventListener(childEventListener)
        }
}
