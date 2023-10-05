package com.example.classetectoykotlin

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.itfast.tectoy.Dispositivo
import br.com.itfast.tectoy.TecToy

class ActivityEscreverNFC : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_escrever_nfc)
        val btnEscreverNFC = findViewById<Button>(R.id.btnEnviarEscritaNFC)
        val editTextoNFC = findViewById<EditText>(R.id.edtTextoNFC)
        btnEscreverNFC.setOnClickListener { v: View? ->
            try {
                tectoy.escreverNFC(editTextoNFC.text.toString())
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