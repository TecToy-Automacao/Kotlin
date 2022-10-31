package br.com.itfast.example.kotlin

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.appcompat.app.AppCompatActivity
import br.com.itfast.tectoy.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    lateinit var tectoy: TecToy
    lateinit var txtNfcLido: TextView
    lateinit var pendingIntent: PendingIntent

    var callback = TecToyNfcCallback { strValor -> txtNfcLido?.text = strValor }
    var callbackCodBarras = TecToyScannerCallback { s -> txtNfcLido?.text = s }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tectoy = TecToy(Dispositivo.V2_PRO, this)

        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            0
        )
        tectoy.iniciarNFC(intent, callback)

        var btnImprimir: Button = findViewById(R.id.btnImprimir)
        var btnLerCodBarras: Button = findViewById(R.id.btnIniciaScan)
        var btnGravarNFC: Button = findViewById(R.id.btnGravarNFC)
        txtNfcLido = findViewById(R.id.txtNFCLido)
        var txtEscreverNfc: EditText = findViewById(R.id.txtEscreverNFC)

        btnImprimir.setOnClickListener {
            try{
                tectoy.imprimir("testando 123456\n\n\n")
            }catch (ex: TecToyException){
                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show();
            }
        }

        btnGravarNFC.setOnClickListener {
            try{
                tectoy.escreverNFC(txtEscreverNfc.getText().toString());
                Toast.makeText(this, "Escrita NFC com sucesso", Toast.LENGTH_SHORT).show();
            }catch (ex: TecToyException){
                Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show();
            }
        }

        btnLerCodBarras.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    iniciarScanner(callbackCodBarras)
                }
            }.invokeOnCompletion {
            }
        }
    }

    suspend fun iniciarScanner(callback: TecToyScannerCallback) {
        try{
            tectoy.iniciarScanner(callback)
        }catch (ex: TecToyException){
            Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show();
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        tectoy.onNewIntentNFC(intent);
    }

    override fun onPause() {
        super.onPause()
        tectoy.onPauseNFC(this)
    }

    override fun onResume() {
        super.onResume()
        tectoy.onResumeNFC(this, pendingIntent);
    }
}