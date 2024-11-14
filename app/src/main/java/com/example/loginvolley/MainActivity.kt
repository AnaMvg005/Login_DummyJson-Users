package com.example.loginvolley

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isUserLoggedIn()) {
            // MANTIENE LA SESION INCIADA
            val sharedPreferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE)
            val jsonDataString = sharedPreferences.getString("DatosJson", null)
            jsonDataString?.let {
                try {
                    val jsonData = JSONObject(it)
                    mostrarDatos(jsonData)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun isUserLoggedIn(): Boolean {
        //VERIFICACION DE DATOS GUARDADOS
        val sharedPreferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE)
        return sharedPreferences.contains("DatosJson")
    }

    fun pasar(view: View) {
        val usuario = findViewById<EditText>(R.id.txtusuario).text.toString()
        val clave = findViewById<EditText>(R.id.txtclave).text.toString()
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        progressBar.visibility = View.VISIBLE

        val jsonBody = JSONObject().apply {
            put("username", usuario)
            put("password", clave)
        }

        val url = "https://dummyjson.com/auth/login"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                progressBar.visibility = View.GONE
                mostrarDatos(response)
            },
            { error ->
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                findViewById<EditText>(R.id.txtclave).text.clear()
            }
        )

        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }

    private fun mostrarDatos(jsonData: JSONObject) {
        //GUARDA DATOS EN EL
        val sharedPreferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("DatosJson", jsonData.toString())
        editor.apply()

        // INICIA LA ACTIVIDAD
        val intent = Intent(this, jsonActivity::class.java)
        intent.putExtra("DatosJson", jsonData.toString())
        startActivity(intent)
    }
}