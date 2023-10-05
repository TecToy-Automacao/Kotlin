package com.example.classetectoykotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.itfast.tectoy.Dispositivo
import br.com.itfast.tectoy.TecToy

class ActivityImprimirImagem : AppCompatActivity (){



    private val CHOOSE_IMAGE_FROM_DEVICE = 1001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imprimir_imagem)

        val btnSendImprimirImagem = findViewById<Button>(R.id.btnSendImprimirImagem)
        btnSendImprimirImagem.setOnClickListener { v: View? -> callChoseFile() }
    }

    private fun callChoseFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, CHOOSE_IMAGE_FROM_DEVICE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == CHOOSE_IMAGE_FROM_DEVICE && resultCode == RESULT_OK) {
            if (resultData != null) {
                try {
                    val fullFilePath: String? =
                        resultData.data?.let { UriUtils().getPathFromUri(this, it) }
                    tectoy.imprimirImagem(fullFilePath)
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

}