package br.com.tectoy.tectoyautomacao.activity

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import br.com.example.tectoy.tectoyautomacao.R
import com.sunmi.scanner.IScanInterface
import org.jetbrains.annotations.Nullable
import sunmi.sunmiui.dialog.DialogCreater
import sunmi.sunmiui.dialog.ListDialog
import sunmi.sunmiui.dialog.ListDialog.ItemClickListener

class ScanActivity : AppCompatActivity() {




    private var scanInterface: IScanInterface? = null
    private var error_level = 3
    private var sunmiScanner: SunmiScanner? = null


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        bindScannerService()
        val mBtn = findViewById<TextView>(R.id.btn_scan)

        val charcter_set_content = findViewById<TextView>(R.id.charcter_set_content)
        val prompt_mode_scan = findViewById<TextView>(R.id.prompt_mode_scan)
        val scan_mode_content = findViewById<TextView>(R.id.scan_mode_content)
        val scan_trigger_content = findViewById<TextView>(R.id.scan_trigger_content)
        findViewById<LinearLayout>(R.id.charcter_set).setOnClickListener(View.OnClickListener {
            val el = arrayOf("UTF-8", "GBK", "ISO-8859-1", "SHIFT-JLS", "Compatibility")
            val listDialog = DialogCreater.createListDialog(this@ScanActivity,
                resources.getString(R.string.error_qrcode),
                resources.getString(R.string.cancel),
                el)
            listDialog.setItemClickListener { position ->
                charcter_set_content.setText(el[position])
                error_level = position
                listDialog.cancel()
            }
            listDialog.show()
        })
        findViewById<LinearLayout>(R.id.prompt_mode).setOnClickListener(View.OnClickListener {
            val el = arrayOf("Beep+Vibrate", "Beep", "Vibrate")
            val listDialog = DialogCreater.createListDialog(this@ScanActivity,
                resources.getString(R.string.error_qrcode),
                resources.getString(R.string.cancel),
                el)
            listDialog.setItemClickListener { position ->
                prompt_mode_scan.setText(el[position])
                error_level = position
                listDialog.cancel()
            }
            listDialog.show()
        })
        findViewById<LinearLayout>(R.id.scan_mode).setOnClickListener(View.OnClickListener {
            val el = arrayOf("Trigger mode", "Continuos mode", "Pulse mode")
            val listDialog = DialogCreater.createListDialog(this@ScanActivity,
                resources.getString(R.string.error_qrcode),
                resources.getString(R.string.cancel),
                el)
            listDialog.setItemClickListener { position ->
                scan_mode_content.setText(el[position])
                error_level = position
                listDialog.cancel()
            }
            listDialog.show()
        })
        findViewById<LinearLayout>(R.id.scan_trigger).setOnClickListener(View.OnClickListener {
            val el = arrayOf("none", "On-screen Button")
            val listDialog = DialogCreater.createListDialog(this@ScanActivity,
                resources.getString(R.string.error_qrcode),
                resources.getString(R.string.cancel),
                el)
            listDialog.setItemClickListener { position ->
                scan_trigger_content.setText(el[position])
                error_level = position
                listDialog.cancel()
            }
            listDialog.show()
        })
        mBtn.setOnClickListener {
            try {
                scanInterface!!.scan()
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
        initScanner()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val action = event.action
        when (action) {
            KeyEvent.ACTION_DOWN -> {
                if (event.keyCode == KeyEvent.KEYCODE_VOLUME_UP || event.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || event.keyCode == KeyEvent.KEYCODE_BACK || event.keyCode == KeyEvent.KEYCODE_MENU || event.keyCode == KeyEvent.KEYCODE_HOME || event.keyCode == KeyEvent.KEYCODE_POWER) return super.dispatchKeyEvent(
                    event)
                if (sunmiScanner != null) sunmiScanner!!.analysisKeyEvent(event)
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
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        val tvNote = findViewById<TextView>(R.id.tv_note)
        runOnUiThread {
            tvNote!!.append(message)
            scrollView!!.post { scrollView!!.smoothScrollTo(0, tvNote!!.bottom) }
        }
    }

    private val conn: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            scanInterface = IScanInterface.Stub.asInterface(service)
            Log.i("setting", "Scanner Service Connected!")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.e("setting", "Scanner Service Disconnected!")
            scanInterface = null
        }
    }

    fun bindScannerService() {
        val intent = Intent()
        intent.setPackage("com.sunmi.scanner")
        intent.action = "com.sunmi.scanner.IScanInterface"
        bindService(intent, conn, BIND_AUTO_CREATE)
    }

}