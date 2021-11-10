package br.com.tectoy.tectoyautomacao.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import br.com.example.tectoy.tectoyautomacao.R
import br.com.tectoy.tectoyautomacao.Utils.TectoySunmiPrint

class LcdActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lcd)

        val btn_pic = findViewById<TextView>(R.id.btn_pic)
        val btn_2linha = findViewById<TextView>(R.id.btn_2linha)
        val btn_1linha = findViewById<TextView>(R.id.btn_1linha)

        btn_pic.setOnClickListener {
            val options = BitmapFactory.Options()
            options.inScaled = false
            options.inDensity = resources.displayMetrics.densityDpi
            TectoySunmiPrint.getInstance().sendPicToLcd(BitmapFactory.decodeResource(resources,
                R.drawable.mini, options))
        }
        btn_2linha.setOnClickListener {
            TectoySunmiPrint.getInstance().sendTextsToLcd()
        }
        btn_1linha.setOnClickListener {
            TectoySunmiPrint.getInstance().sendTextToLcd()
        }
        //init lcd 、light up lcd and clear screen
        TectoySunmiPrint.getInstance().controlLcd(1)
        TectoySunmiPrint.getInstance().controlLcd(2)
        TectoySunmiPrint.getInstance().controlLcd(4)
    }
}