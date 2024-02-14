package com.example.classetectoykotlin

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.itfast.tectoy.*


lateinit var tectoy : TecToy

@Suppress("KotlinConstantConditions")
class MainActivity(): AppCompatActivity()  {

    private  lateinit var pendingIntent: PendingIntent
    var NFCIniciado = false

    lateinit var btnStatusImpressora : Button
    lateinit var btnImprimir : Button
    lateinit var btnStatusGaveta : Button
    lateinit var btnImprimirImagem : Button
    lateinit var btnAbrirGaveta : Button
    lateinit var btnAcionarGuilhotina : Button
    lateinit var btnPosicionarEtiqueta : Button
    lateinit var btnImprimirQrCode : Button
    lateinit var btnPosicionarFinalEtiqueta : Button
    lateinit var btnLimparDisplay : Button
    lateinit var btnEscreverDisplay : Button
    lateinit var btnQrCodeDisplay : Button
    lateinit var btnBmpDisplay : Button
    lateinit var btnEncerrarScanner : Button
    lateinit var btnIniciarScanner : Button
    lateinit var btnIniciarNFC : Button
    lateinit var btnLerPesoBalanca : Button
    lateinit var btnEscreverNFC : Button
    lateinit var btnIniciarCameraProfundidade : Button
    lateinit var txtAuxiliar : TextView
    lateinit var btnDesligarLedIndicacao : Button
    lateinit var btnLigarLedIndicacao : Button
    lateinit var btnIniciar :Button
    var retornoNFC :String? =""
    var retornoNFC2 :String? =""

    var nfcCallbackK2: TecToyNfcCallback = object : TecToyNfcCallback {
        override fun retornarValor(strValor: String) {
            retornoNFC = "Conteudo do NFC:$strValor"
            runOnUiThread { txtAuxiliar.text = retornoNFC }
        }

        override fun retornarId(s: String) {
            //Retorno do ID não disponível para o K2
        }
    }

    var nfcCallback: TecToyNfcCallback = object : TecToyNfcCallback {
        override fun retornarValor(strValor: String) {
            retornoNFC = "NFC Valor: $strValor"
            txtAuxiliar.text= retornoNFC+ retornoNFC2;
         }

        override fun retornarId(strID: String) {
            retornoNFC2 = " | NFC ID: $strID"
            txtAuxiliar.text= retornoNFC+ retornoNFC2
        }

    }

    var balancaCallback =
        TectoyBalancaCallback { map: Map<*, *> ->
            Toast.makeText(
                applicationContext,
                map["peso"].toString(),
                Toast.LENGTH_LONG
            ).show()
        }
    var scannerCallback = TecToyScannerCallback { s: String? ->
        Toast.makeText(
            applicationContext,
            s,
            Toast.LENGTH_LONG
        ).show()
    }

    var profundidadeCallback =
        TecToyCameraProfundidadeCallback { i: Int ->
            runOnUiThread {
                "$i cm".also { txtAuxiliar.text = it }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //verificando as permissões necessarias para utilizar o app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
            }
            if (checkSelfPermission(Manifest.permission.NFC) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.NFC), 0)
            }
            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.INTERNET), 0)
            }
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 0)
            }
        }

        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0 )

        btnStatusImpressora = findViewById(R.id.btnStatusImpressora)
        btnImprimir = findViewById(R.id.btnImprimir)
        btnStatusGaveta = findViewById(R.id.btnStatusGaveta)
        btnImprimirImagem = findViewById(R.id.btnImprimirImagem)
        btnAbrirGaveta = findViewById(R.id.btnAbrirGaveta)
        btnAcionarGuilhotina = findViewById(R.id.btnAcionarGuilhotina)
        btnPosicionarEtiqueta = findViewById(R.id.btnPosicionarEtiqueta)
        btnImprimirQrCode = findViewById(R.id.btnImprimirQrCode)
        btnPosicionarFinalEtiqueta = findViewById(R.id.btnPosicionarFinalEtiqueta)
        btnLimparDisplay = findViewById(R.id.btnLimparDisplay)
        btnEscreverDisplay = findViewById(R.id.btnEscreverDisplay)
        btnQrCodeDisplay = findViewById(R.id.btnQrCodeDisplay)
        btnBmpDisplay = findViewById(R.id.btnBmpDisplay)
        btnEncerrarScanner = findViewById(R.id.btnEncerrarScanner)
        btnIniciarScanner = findViewById(R.id.btnIniciarScanner)
        btnIniciarNFC = findViewById(R.id.btnIniciarNFC)
        btnLerPesoBalanca = findViewById(R.id.btnLerPesoBalanca)
        btnEscreverNFC = findViewById(R.id.btnEscreverNFC)
        btnIniciarCameraProfundidade = findViewById(R.id.btnIniciarCameraProfundidade)
        txtAuxiliar = findViewById(R.id.txtAuxiliar)
        btnDesligarLedIndicacao = findViewById(R.id.btnDesligarLedIndicacao)
        btnLigarLedIndicacao = findViewById(R.id.btnLigarLedIndicacao)
        btnIniciar = findViewById<Button>(R.id.btnIniciar)


        val spinnerDispositivos = findViewById<Spinner>(R.id.spinnerDispositivos)
        val adapter =
            ArrayAdapter.createFromResource(this, R.array.dispositivos, R.layout.spinner_item)
        spinnerDispositivos.adapter = adapter

        btnIniciar.setOnClickListener { _: View? ->
            when (spinnerDispositivos.selectedItem.toString()) {
                "T2_MINI" -> {
                    tectoy = TecToy(Dispositivo.T2_MINI, this@MainActivity)
                    goneButtons()
                    btnStatusImpressora.visibility = View.VISIBLE
                    btnStatusGaveta.visibility = View.VISIBLE
                    btnImprimir.visibility = View.VISIBLE
                    btnAbrirGaveta.visibility = View.VISIBLE
                    btnImprimirImagem.visibility = View.VISIBLE
                    btnImprimirQrCode.visibility = View.VISIBLE
                    btnAcionarGuilhotina.visibility = View.VISIBLE
                    btnIniciarNFC.visibility = View.VISIBLE
                    btnEscreverNFC.visibility = View.VISIBLE
                    btnLimparDisplay.visibility = View.VISIBLE
                    btnBmpDisplay.visibility = View.VISIBLE
                    btnEscreverDisplay.visibility = View.VISIBLE
                    btnQrCodeDisplay.visibility = View.VISIBLE
                    btnLerPesoBalanca.visibility = View.VISIBLE
                }
                "D2_MINI" -> {
                    goneButtons()
                    tectoy = TecToy(Dispositivo.D2_MINI, this@MainActivity)
                    btnStatusGaveta.visibility = View.VISIBLE
                    btnStatusImpressora.visibility = View.VISIBLE
                    btnImprimir.visibility = View.VISIBLE
                    btnImprimirImagem.visibility = View.VISIBLE
                    btnAbrirGaveta.visibility = View.VISIBLE
                    btnEscreverNFC.visibility = View.VISIBLE
                    btnIniciarNFC.visibility = View.VISIBLE
                    btnImprimirQrCode.visibility = View.VISIBLE
                    btnLerPesoBalanca.visibility = View.VISIBLE
                }
                "D2S" -> {
                    goneButtons()
                    tectoy = TecToy(Dispositivo.D2S, this@MainActivity)
                    btnStatusGaveta.visibility = View.VISIBLE
                    btnStatusImpressora.visibility = View.VISIBLE
                    btnImprimir.visibility = View.VISIBLE
                    btnImprimirImagem.visibility = View.VISIBLE
                    btnAbrirGaveta.visibility = View.VISIBLE
                    btnAcionarGuilhotina.visibility = View.VISIBLE
                    btnImprimirQrCode.visibility = View.VISIBLE
                    btnLerPesoBalanca.visibility = View.VISIBLE
                }
                "V2" -> {
                    tectoy = TecToy(Dispositivo.V2, this@MainActivity)
                    goneButtons()
                    btnStatusImpressora.visibility = View.VISIBLE
                    btnImprimir.visibility = View.VISIBLE
                    btnImprimirImagem.visibility = View.VISIBLE
                    btnImprimirQrCode.visibility = View.VISIBLE
                    btnIniciarScanner.visibility = View.VISIBLE
                    btnEncerrarScanner.visibility = View.VISIBLE
                }
                "V2_PRO" -> {
                    goneButtons()
                    tectoy = TecToy(Dispositivo.V2_PRO, this@MainActivity)
                    btnStatusImpressora.visibility = View.VISIBLE
                    btnImprimir.visibility = View.VISIBLE
                    btnImprimirImagem.visibility = View.VISIBLE
                    btnEscreverNFC.visibility = View.VISIBLE
                    btnIniciarNFC.visibility = View.VISIBLE
                    btnImprimirQrCode.visibility = View.VISIBLE
                    btnPosicionarEtiqueta.visibility = View.VISIBLE
                    btnPosicionarFinalEtiqueta.visibility = View.VISIBLE
                    btnIniciarScanner.visibility = View.VISIBLE
                    btnEncerrarScanner.visibility = View.VISIBLE
                }
                "K2" -> {
                    tectoy = TecToy(Dispositivo.K2, this@MainActivity)
                    goneButtons()
                    btnStatusImpressora.visibility = View.VISIBLE
                    btnImprimir.visibility = View.VISIBLE
                    btnImprimirImagem.visibility = View.VISIBLE
                    btnAcionarGuilhotina.visibility = View.VISIBLE
                    btnIniciarNFC.visibility = View.VISIBLE
                    btnLerPesoBalanca.visibility = View.VISIBLE
                    btnIniciarCameraProfundidade.visibility = View.VISIBLE
                    btnIniciarScanner.visibility = View.VISIBLE
                    btnEncerrarScanner.visibility = View.VISIBLE
                    btnDesligarLedIndicacao.visibility = View.VISIBLE
                    btnLigarLedIndicacao.visibility = View.VISIBLE
                }
                "K2_MINI" -> {
                    goneButtons()
                    tectoy = TecToy(Dispositivo.K2_MINI, this@MainActivity)
                    btnStatusGaveta.visibility = View.VISIBLE
                    btnStatusImpressora.visibility = View.VISIBLE
                    btnImprimir.visibility = View.VISIBLE
                    btnImprimirImagem.visibility = View.VISIBLE
                    btnAbrirGaveta.visibility = View.VISIBLE
                    btnAcionarGuilhotina.visibility = View.VISIBLE
                    btnEscreverNFC.visibility = View.VISIBLE
                    btnIniciarNFC.visibility = View.VISIBLE
                    btnImprimirQrCode.visibility = View.VISIBLE
                    btnLerPesoBalanca.visibility = View.VISIBLE
                    btnDesligarLedIndicacao.visibility = View.VISIBLE
                    btnLigarLedIndicacao.visibility = View.VISIBLE
                    btnIniciarScanner.visibility = View.VISIBLE
                    btnEncerrarScanner.visibility = View.VISIBLE
                }
                "T2S" -> {
                    goneButtons()
                    tectoy =  TecToy(Dispositivo.T2S, this@MainActivity)
                    btnStatusGaveta.visibility = View.VISIBLE
                    btnStatusImpressora.visibility = View.VISIBLE
                    btnImprimir.visibility = View.VISIBLE
                    btnImprimirImagem.visibility = View.VISIBLE
                    btnAbrirGaveta.visibility = View.VISIBLE
                    btnAcionarGuilhotina.visibility = View.VISIBLE
                    btnImprimirQrCode.visibility = View.VISIBLE
                    btnLerPesoBalanca.visibility = View.VISIBLE
                }
                "L2Ks" -> {
                    goneButtons()
                    tectoy = TecToy(Dispositivo.L2Ks, this@MainActivity)
                    btnIniciarNFC.visibility = View.VISIBLE
                    btnIniciarScanner.visibility = View.VISIBLE
                    btnEncerrarScanner.visibility = View.VISIBLE
                    btnEscreverNFC.visibility = View.VISIBLE
                }
                "L2s" -> {
                    goneButtons()
                    tectoy = TecToy(Dispositivo.L2s, this@MainActivity)
                    btnIniciarNFC.visibility = View.VISIBLE
                    btnIniciarScanner.visibility = View.VISIBLE
                    btnEncerrarScanner.visibility = View.VISIBLE
                    btnEscreverNFC.visibility = View.VISIBLE
                }
            }
        }


        btnStatusImpressora.setOnClickListener { _: View? ->

            try {
                Toast.makeText(
                    applicationContext,
                    StatusImpressora.TextoStatus(tectoy.statusImpressora().obtemStatus()),
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }

        btnImprimir.setOnClickListener { _: View? ->
            try {
                this.startActivity(Intent(this, ActivityImprimir::class.java))
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        btnStatusGaveta.setOnClickListener { _: View? ->
            try {
                val bStatusGaveta: Boolean = tectoy.gavetaAberta()
                if (bStatusGaveta) {
                    Toast.makeText(applicationContext, "GAVETA ABERTA", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(applicationContext, "GAVETA FECHADA", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        btnImprimirImagem.setOnClickListener { _: View? ->
            try {
                this.startActivity(Intent(this, ActivityImprimirImagem::class.java))
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        btnAbrirGaveta.setOnClickListener { _: View? ->
            try {
                tectoy.abrirGaveta()
                Toast.makeText(
                    applicationContext,
                    "Comando enviado com sucesso",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        btnAcionarGuilhotina.setOnClickListener { _: View? ->
            try {
               tectoy.acionarGuilhotina()
                Toast.makeText(
                    applicationContext,
                    "Comando enviado com sucesso",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        btnPosicionarEtiqueta.setOnClickListener { _: View? ->
            try {
                tectoy.posicionarEtiqueta()
                Toast.makeText(
                    applicationContext,
                    "Comando enviado com sucesso",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        btnImprimirQrCode.setOnClickListener { _: View? ->
            try {
                this.startActivity(Intent(this, ActivityImprimirQrCode::class.java))
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        btnPosicionarFinalEtiqueta.setOnClickListener { _: View? ->
            try {
                tectoy.finalEtiqueta()
                Toast.makeText(
                    applicationContext,
                    "Comando enviado com sucesso",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        btnLimparDisplay.setOnClickListener { _: View? ->
            try {
                tectoy.limparDisplay()
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        btnEscreverDisplay.setOnClickListener { _: View? ->
            try {
                this.startActivity(Intent(this, ActivityEscreverDisplay::class.java))
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        btnQrCodeDisplay.setOnClickListener { _: View? ->
            try {
                this.startActivity(Intent(this, ActivityQrCodeDisplay::class.java))
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        btnBmpDisplay.setOnClickListener { _: View? ->
            try {
                this.startActivity(Intent(this, ActivityBMPDisplay::class.java))
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        btnEncerrarScanner.setOnClickListener { _: View? ->
            try {
                tectoy.pararScanner()
                Toast.makeText(
                    applicationContext,
                    "Comando enviado com sucesso",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        btnIniciarScanner.setOnClickListener { _: View? ->
            try {
                tectoy.iniciarScanner(scannerCallback)
                Toast.makeText(
                    applicationContext,
                    "Comando enviado com sucesso",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        btnIniciarNFC.setOnClickListener { _: View? ->

            val dispositivo = spinnerDispositivos.selectedItem.toString()
            txtAuxiliar.setText("");
            retornoNFC = "";
            if (dispositivo == "K2") {
                try {
                    tectoy.iniciarNFC(intent, nfcCallbackK2)
                    NFCIniciado = true
                    Toast.makeText(
                        applicationContext,
                        "Comando enviado com sucesso",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (ex: java.lang.Exception) {
                    Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                try {
                    tectoy.iniciarNFC(intent, nfcCallback)
                    NFCIniciado = true
                    tectoy.onResumeNFC(this, pendingIntent);
                    Toast.makeText(
                        applicationContext,
                        "Comando enviado com sucesso",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (ex: java.lang.Exception) {
                    Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
                }
            }



        }

        btnLerPesoBalanca.setOnClickListener { _: View? ->
            try {
                tectoy.lerPeso(balancaCallback)
                Toast.makeText(
                    applicationContext,
                    "Comando enviado com sucesso",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }

        btnEscreverNFC.setOnClickListener { _: View? ->
            try {
                this.startActivity(Intent(this, ActivityEscreverNFC::class.java))
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
        btnIniciarCameraProfundidade.setOnClickListener { v: View? ->
            try {
                val thrCamProf = Thread(this.iniciarCamProfundidade)
                thrCamProf.start()
                thrCamProf.join()
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "Câmera iniciada com sucesso",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }


        btnDesligarLedIndicacao.setOnClickListener { _: View? ->
            try {
                tectoy.desligarLedStatus()
                Toast.makeText(
                    applicationContext,
                    "Comando enviado com sucesso",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }

        btnLigarLedIndicacao.setOnClickListener { _: View? ->
            try {
                this.startActivity(Intent(this, ActivitityLedIndicacao::class.java))
            } catch (ex: java.lang.Exception) {
                Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        tectoy.onNewIntentNFC(intent)
    }

    override fun onPause() {
        super.onPause()
        if (NFCIniciado == true){
        tectoy.onPauseNFC(this)}
    }

    override fun onResume() {
        super.onResume()
        if (this.NFCIniciado == true){
            tectoy.onResumeNFC(this, pendingIntent)
        }
    }


    private val iniciarCamProfundidade = Runnable {
        try {
            Looper.prepare()
            tectoy.iniciarCameraProfundidade(profundidadeCallback)
        } catch (e: Exception) {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setMessage(e.message).setNeutralButton("OK", null)
            builder.show()
        }
    }

    private fun goneButtons() {
        btnStatusImpressora.visibility = View.GONE
        btnImprimir.visibility = View.GONE
        btnStatusGaveta.visibility = View.GONE
        btnImprimirImagem.visibility = View.GONE
        btnAbrirGaveta.visibility = View.GONE
        btnAcionarGuilhotina.visibility = View.GONE
        btnPosicionarEtiqueta.visibility = View.GONE
        btnImprimirQrCode.visibility = View.GONE
        btnPosicionarFinalEtiqueta.visibility = View.GONE
        btnLimparDisplay.visibility = View.GONE
        btnEscreverDisplay.visibility = View.GONE
        btnQrCodeDisplay.visibility = View.GONE
        btnBmpDisplay.visibility = View.GONE
        btnEncerrarScanner.visibility = View.GONE
        btnIniciarScanner.visibility = View.GONE
        btnIniciarNFC.visibility = View.GONE
        btnLerPesoBalanca.visibility = View.GONE
        btnEscreverNFC.visibility = View.GONE
        btnIniciarCameraProfundidade.visibility = View.GONE
        btnDesligarLedIndicacao.visibility = View.GONE
        btnLigarLedIndicacao.visibility = View.GONE
    }
}
