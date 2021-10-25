package br.com.tectoy.tectoyautomacao.activity

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import br.com.example.tectoy.tectoyautomacao.R
import br.com.tectoy.tectoyautomacao.Utils.BluetoothUtil
import com.sunmi.statuslampmanager.IStateLamp
import sunmi.sunmiui.dialog.DialogCreater

class LedActivity : AppCompatActivity() {


    private var mService: IStateLamp? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_led)
        // Conectando Serviço de Led
        // Conectando Serviço de Led
        connectService()
        val btn_azul = findViewById<Button>(R.id.btn_azul)
        val btn_verde = findViewById<Button>(R.id.btn_verde)
        val btn_vermelho = findViewById<Button>(R.id.btn_vermelho)
        val btn_desliga = findViewById<Button>(R.id.btn_desligar)

        btn_azul.setOnClickListener {
            try {
                mService!!.closeAllLamp()
                mService!!.controlLamp(0, "Led-3")
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
        btn_verde.setOnClickListener {
            try {
                mService!!.closeAllLamp()
                mService!!.controlLamp(0, "Led-2")
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
        btn_vermelho.setOnClickListener {
            try {
                mService!!.closeAllLamp()
                mService!!.controlLamp(0, "Led-1")
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
        btn_desliga.setOnClickListener {
            try {
                mService!!.closeAllLamp()
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }
    private val con: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mService = IStateLamp.Stub.asInterface(service)
            Log.d("darren", "Service Connected.")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.d("darren", "Service Disconnected.")
            mService = null
        }
    }

    private fun connectService() {
        val intent = Intent()
        intent.setPackage("com.sunmi.statuslampmanager")
        intent.action = "com.sunmi.statuslamp.service"
        startService(intent)
        bindService(intent, con, BIND_AUTO_CREATE)
    }
}