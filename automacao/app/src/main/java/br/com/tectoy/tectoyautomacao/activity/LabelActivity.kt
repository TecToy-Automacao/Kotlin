package br.com.tectoy.tectoyautomacao.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import br.com.example.tectoy.tectoyautomacao.R
import br.com.tectoy.tectoyautomacao.Utils.TectoySunmiPrint
import sunmi.sunmiui.dialog.DialogCreater
import sunmi.sunmiui.dialog.EditTextDialog

class LabelActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_label)


        val mButtonone = findViewById<Button>(R.id.btn_one)
        mButtonone.setOnClickListener{
        TectoySunmiPrint.getInstance().printOneLabel();
        }
        val mButtonmulti = findViewById<Button>(R.id.btn_multi)
        mButtonmulti.setOnClickListener {
            TectoySunmiPrint.getInstance().printMultiLabel(5);
        }
    }
}