package br.com.tectoy.tectoyautomacao.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.widget.ScrollView
import android.widget.TextView
import br.com.example.tectoy.tectoyautomacao.R

class ScannerActivity : AppCompatActivity(){


    private var sunmiScanner: SunmiScanner? = null
    private var tvNote: TextView? = null
    private var scrollView: ScrollView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        scrollView = findViewById(R.id.scrollView)
        tvNote = findViewById(R.id.tv_note)
        initScanner()
    }
    override fun onDestroy() {
        super.onDestroy()
        sunmiScanner?.destory()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val action = event.action
        when (action) {
            KeyEvent.ACTION_DOWN -> {
                if (event.keyCode == KeyEvent.KEYCODE_VOLUME_UP || event.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || event.keyCode == KeyEvent.KEYCODE_BACK || event.keyCode == KeyEvent.KEYCODE_MENU || event.keyCode == KeyEvent.KEYCODE_HOME || event.keyCode == KeyEvent.KEYCODE_POWER) return super.dispatchKeyEvent(
                    event)
                sunmiScanner?.analysisKeyEvent(event)
                return true
            }
            else -> {
            }
        }
        return super.dispatchKeyEvent(event)
    }

    private fun initScanner() {
        sunmiScanner = SunmiScanner(applicationContext)
        sunmiScanner!!.analysisBroadcast()
        sunmiScanner!!.setScannerListener(object : SunmiScanner.OnScannerListener {
            override fun onScanData(data: String, type: SunmiScanner.DATA_DISCRIBUTE_TYPE) {
                append("Tipo de Dado:$type\nCodigo:$data\n")
            }

            override fun onResponseData(data: String?, type: SunmiScanner.DATA_DISCRIBUTE_TYPE?) {}
            override fun onResponseTimeout() {}
        })
    }
    private fun append(message: String) {
        runOnUiThread {
            tvNote?.append(message)
            scrollView?.post(Runnable { tvNote?.let { scrollView!!.smoothScrollTo(0, it.getBottom()) } })
        }
    }
}