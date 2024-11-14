package com.example.loginvolley

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import org.json.JSONObject

class jsonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_json)

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

        val btnSalir = findViewById<Button>(R.id.btnSalir)
        btnSalir.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun mostrarDatos(jsonData: JSONObject) {
        try {
            val nombreCompletoTextView = findViewById<TextView>(R.id.nombreCompletoTextView)
            val emailTextView = findViewById<TextView>(R.id.emailTextView)
            val generoTextView = findViewById<TextView>(R.id.generoTextView)
            val fotoImageView = findViewById<ImageView>(R.id.fotoImageView)

            nombreCompletoTextView.text = "${jsonData.getString("firstName")} ${jsonData.getString("lastName")}"
            emailTextView.text = jsonData.getString("email")
            generoTextView.text = jsonData.getString("gender")

            Picasso.get().load(jsonData.getString("image")).into(fotoImageView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun cerrarSesion() {
        val sharedPreferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("DatosJson") // Aquí corregimos la clave
        editor.apply()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
