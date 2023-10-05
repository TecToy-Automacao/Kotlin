package com.example.classetectoykotlin

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.itfast.tectoy.Dispositivo
import br.com.itfast.tectoy.TecToy

class ActivityQrCodeDisplay: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code_display)
        val btnEnviarQrCodeDisplay = findViewById<Button>(R.id.btnEnviarQrCodeDisplay)
        val txtValorQrCodeDisplay = findViewById<EditText>(R.id.txtValorQrCodeDisplay)
        btnEnviarQrCodeDisplay.setOnClickListener { v: View? ->
            try {
                tectoy.qrCodeDisplay(txtValorQrCodeDisplay.text.toString())
                Toast.makeText(
                    applicationContext,
                    "Comando enviado com sucesso",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}