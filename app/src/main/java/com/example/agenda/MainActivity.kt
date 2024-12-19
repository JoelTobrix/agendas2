package com.example.agenda

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    private lateinit var textName: EditText
    private lateinit var textSurname: EditText
    private lateinit var textNumber: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Vincular vistas
        textName = findViewById(R.id.text_nombre)
        textSurname = findViewById(R.id.text_apellido)
        textNumber = findViewById(R.id.number)
        addButton = findViewById(R.id.añadir_contacto)

        // Configurar acción del botón
        addButton.setOnClickListener {
            guardarDatos()
        }
    }

    private fun guardarDatos() {
        val nombre = textName.text.toString().trim()
        val apellido = textSurname.text.toString().trim()
        val numero = textNumber.text.toString().trim()

        // Validar campos vacíos
        if (nombre.isEmpty() || apellido.isEmpty() || numero.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        // Validación de número: Solo números y longitud adecuada (10 a 15 caracteres)
        if (!numero.matches("[0-9]+".toRegex())) {
            Toast.makeText(this, "Número no válido. Solo números permitidos.", Toast.LENGTH_SHORT).show()
            return
        }

        if (numero.length < 10 || numero.length > 15) {
            Toast.makeText(this, "El número debe tener entre 10 y 15 dígitos.", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "http://192.168.100.126/agenda/insertar.php"

        // Crear una solicitud POST
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                // Verificar respuesta del servidor
                if (response.contains("Contacto registrado correctamente.", ignoreCase = true)) {
                    Toast.makeText(this, "Contacto registrado", Toast.LENGTH_SHORT).show()
                    // Limpiar los campos después de insertar
                    textName.setText("")
                    textSurname.setText("")
                    textNumber.setText("")
                } else {
                    Toast.makeText(this, "Error al registrar: $response", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                val message = error.message ?: "Error desconocido"
                Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                // Enviar parámetros al servidor (coincidiendo con los nombres en PHP)
                val params = HashMap<String, String>()
                params["nombre"] = nombre
                params["apellido"] = apellido
                params["numero"] = numero
                return params
            }
        }

        // Agregar la solicitud a la cola de Volley
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
    fun abrircontactos(view: View){
        val intent= Intent(this,listado:: class.java)
        startActivity(intent)
    }

}





