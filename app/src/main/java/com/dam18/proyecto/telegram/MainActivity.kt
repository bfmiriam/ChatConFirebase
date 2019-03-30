package com.dam18.proyecto.telegram

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import java.util.*

import com.google.firebase.database.ChildEventListener



class MainActivity : AppCompatActivity() {

    // para filtrar los logs
    val TAG = "Servicio"

    // referencia de la base de datos
    private var database: DatabaseReference? = null
    // Token del dispositivo
    private var FCMToken: String? = null
    // key unica creada automaticamente al añadir un child
   // lateinit var misDatos: Datos
    lateinit var key: String
    lateinit var MisUsuarios : Usuarios
    // para actualizar los datos necesito un hash map
    val miHashMapChild = HashMap<String, Any>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        conectarse.setOnClickListener {


            // referencia a la base de datos del proyecto en firebase
            database = FirebaseDatabase.getInstance().getReference("/usuarios")


            // solo lo llamo cuando arranco la app
            // evito que cuando se pasa por el onCreate vuelva a ejecutarse
            if (savedInstanceState == null) {
                try {
                    // Obtengo el token del dispositivo.
                    FCMToken = FirebaseInstanceId.getInstance().token
                    // creamos una entrada nueva en el child "usuarios" con un key unico automatico
                    key = database!!.push().key!!
                    // guardamos el token y el usuario en la data class
                    MisUsuarios = Usuarios(
                        FCMToken.toString(), usuario.text.toString()
                    )
                    // creamos el hash map
                    MisUsuarios.crearHashMapDatos()
                    // guardamos los datos en el hash map para la key creada anteriormente
                    miHashMapChild.put(key.toString(), MisUsuarios.miHashMapUsuarios)
                    // actualizamos el child
                    database!!.updateChildren(miHashMapChild)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d(TAG, "Error escribiendo datos ${e}")
                }
            }
            // inicializo el listener para los eventos de la basededatos
            initListener()
            //llamamos a la clase ActivityChat, donde enviamos mensajes
            startActivity(intentFor<ActivityChat>("id" to 5).singleTop())



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


