package com.example.classetectoykotlin

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.itfast.tectoy.Dispositivo
import br.com.itfast.tectoy.TecToy

class ActivityEscreverDisplay : AppCompatActivity(){


     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_escrever_display)
        val edtLinhaUnica: EditText = findViewById<EditText>(R.id.editLinhaUnica)
        val edtLinha1: EditText = findViewById<EditText>(R.id.editLinha1)
        val edtLinha2: EditText = findViewById<EditText>(R.id.editLinha2)
        val btnEscreverDisplayLinhaUnica: Button =
            findViewById<Button>(R.id.btnEscreverDisplay1Linha)
        val btnEscreverDisplay2Linhas: Button = findViewById<Button>(R.id.btnEscreverDisplay2Linhas)
        btnEscreverDisplayLinhaUnica.setOnClickListener { v: View? ->
            try {
                tectoy.escreveDisplay(edtLinhaUnica.text.toString())
                Toast.makeText(
                    getApplicationContext(),
                    "Comando enviado com sucesso",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: Exception) {
                Toast.makeText(getApplicationContext(), ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        btnEscreverDisplay2Linhas.setOnClickListener { v: View? ->
            try {
                tectoy.escreveDisplay(
                    edtLinha1.text.toString(),
                    edtLinha2.text.toString()
                )
                Toast.makeText(
                    getApplicationContext(),
                    "Comando enviado com sucesso",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: Exception) {
                Toast.makeText(getApplicationContext(), ex.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}