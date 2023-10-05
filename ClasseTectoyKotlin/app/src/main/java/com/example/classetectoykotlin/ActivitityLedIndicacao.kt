package com.example.classetectoykotlin

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import br.com.itfast.tectoy.CorLed
import br.com.itfast.tectoy.Dispositivo
import br.com.itfast.tectoy.TecToy

class ActivitityLedIndicacao : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lerd_indicacao)
        val spinnerCorLiga = findViewById<Spinner>(R.id.spinnerCorLiga)
        val adapter: ArrayAdapter<*> =
            ArrayAdapter.createFromResource(this, R.array.coresLed, R.layout.spinner_item)
        spinnerCorLiga.adapter = adapter
        val spinnerCorLigaLoop = findViewById<Spinner>(R.id.spinnerCorLigaLoop)
        spinnerCorLigaLoop.adapter = adapter
        val btnSendLigarLedIndicacaoLoop = findViewById<Button>(R.id.btnSendLigarLedIndicacaoLoop)
        val btnSendLigarLedIndicacao = findViewById<Button>(R.id.btnSendLigarLedIndicacao)
        val txtValorTempoLoop = findViewById<EditText>(R.id.txtTempoLoop)
        btnSendLigarLedIndicacaoLoop.setOnClickListener { view: View? ->
            try {

                when (spinnerCorLigaLoop.selectedItem.toString()) {
                    "AZUL" -> tectoy.loopLigarStatus(
                        CorLed.AZUL,
                        Integer.valueOf(txtValorTempoLoop.text.toString())
                    )
                    "VERDE" -> tectoy.loopLigarStatus(
                        CorLed.VERDE,
                        Integer.valueOf(txtValorTempoLoop.text.toString())
                    )
                    "VERMELHO" -> tectoy.loopLigarStatus(
                        CorLed.VERMELHO,
                        Integer.valueOf(txtValorTempoLoop.text.toString())
                    )
                }
                Toast.makeText(
                    applicationContext,
                    "Comando enviado com sucesso",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        btnSendLigarLedIndicacao.setOnClickListener { view: View? ->
            try {
                when (spinnerCorLiga.selectedItem.toString()) {
                    "AZUL" -> tectoy.ligarLedStatus(CorLed.AZUL)
                    "VERDE" -> tectoy.ligarLedStatus(CorLed.VERDE)
                    "VERMELHO" ->tectoy.ligarLedStatus(CorLed.VERMELHO)
                }
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