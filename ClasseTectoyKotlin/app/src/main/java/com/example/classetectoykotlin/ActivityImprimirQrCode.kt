package com.example.classetectoykotlin

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.itfast.tectoy.Dispositivo
import br.com.itfast.tectoy.TecToy

class ActivityImprimirQrCode : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imprimir_qr_code)
        val txtNivelCorrecao = findViewById<EditText>(R.id.txtNivelCorrecaoQrCodeImpressao)
        val txtConteudoQrCode = findViewById<EditText>(R.id.txtConteudoQrCodeImpressao)
        val txtLarguraModulo = findViewById<EditText>(R.id.txtLarguraModuloQrCodeImpressao)
        val btnEnviarCmd = findViewById<Button>(R.id.btnEnviarImprimirQrCode)
        btnEnviarCmd.setOnClickListener { v: View? ->
            try {
                tectoy.imprimirQrCode(
                    txtConteudoQrCode.text.toString(),
                    txtNivelCorrecao.text.toString(),
                    Integer.valueOf(txtLarguraModulo.text.toString())
                )
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