package com.example.classetectoykotlin

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.itfast.tectoy.Dispositivo
import br.com.itfast.tectoy.TecToy

class ActivityImprimir() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imprimir)


        val btnImprimir = findViewById<Button>(R.id.btnImprimirTxt)
        val editText = findViewById<EditText>(R.id.edtTexto)



        btnImprimir.setOnClickListener { _: View? ->
            try {
                 tectoy.imprimir(editText.text.toString())
               Toast.makeText(applicationContext, "Comando enviado com sucesso", Toast.LENGTH_SHORT).show()
            }
            catch (ex: Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}