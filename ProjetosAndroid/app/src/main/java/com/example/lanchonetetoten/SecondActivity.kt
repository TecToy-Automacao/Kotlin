package com.example.lanchonetetoten

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.itfast.tectoy.Dispositivo
import br.com.itfast.tectoy.TecToy
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.item_product.*
import kotlinx.android.synthetic.main.item_product.view.*

import java.util.*

class SecondActivity : AppCompatActivity() {

    lateinit var tectoy: TecToy
    var iNum = 0
    var soma = 0
    var remove = 0
    var iItens = 0
    lateinit var strNum: String
    private lateinit var productAdapter: ProductAdapter


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)


        val btnReturn: Button = findViewById(R.id.btnReturn)

        btnReturn.setOnClickListener(View.OnClickListener {
            val pause = Intent(this, MainActivity::class.java)
            startActivity(pause)
        })

        initRecyclerView()
        addDataSource()

        btnPagment.setOnClickListener(View.OnClickListener {
            if(!textViewValue.text.toString().startsWith("0")) {
            val proxima = Intent(this, ActivityPagment::class.java)
            startActivity(proxima)}
            else {
               mensagem("Selecione um produto para ir para o pagamento.")
            }
        })
    }

    private fun addDataSource() {
        val dataSource = DataSource.createDataSet()
        this.productAdapter.setDataSet(dataSource)
    }

    private fun initRecyclerView() {
        this.productAdapter = ProductAdapter()
        recycler_view_main.layoutManager = LinearLayoutManager(this@SecondActivity)
        recycler_view_main.adapter = this.productAdapter
    }



    override fun onResume() {
        super.onResume()
        moreItensProduct1.setOnClickListener(View.OnClickListener {
            soma(24)
        })
        lessItensProduct1.setOnClickListener(View.OnClickListener {
            remove(24)
        })
        moreItensProduct2.setOnClickListener(View.OnClickListener {
            soma(28)
        })
        lessItensProduct2.setOnClickListener(View.OnClickListener {
            remove(28)
        })
        moreItensProduct3.setOnClickListener(View.OnClickListener {
            soma(15)
        })
        lessItensProduct3.setOnClickListener(View.OnClickListener {
            remove(15)
        })
        moreItensProduct4.setOnClickListener(View.OnClickListener {
            soma(6)
        })
        lessItensProduct4.setOnClickListener(View.OnClickListener {
            remove(6)
        })
        moreItensProduct5.setOnClickListener(View.OnClickListener {
            soma(9)
        })
        lessItensProduct5.setOnClickListener(View.OnClickListener {
            remove(9)
        })
        moreItensProduct6.setOnClickListener(View.OnClickListener {
            soma(9)
        })
        lessItensProduct6.setOnClickListener(View.OnClickListener {
            remove(9)
        })
        moreItensProduct7.setOnClickListener(View.OnClickListener {
            soma(7)
        })
        lessItensProduct7.setOnClickListener(View.OnClickListener {
            remove(7)
        })
        moreItensProduct8.setOnClickListener(View.OnClickListener {
            soma(7)
        })
        lessItensProduct8.setOnClickListener(View.OnClickListener {
            remove(7)
        })
        moreItensProduct9.setOnClickListener(View.OnClickListener {
            soma(15)
        })
        lessItensProduct9.setOnClickListener(View.OnClickListener {
            remove(15)
        })
    }
    fun mensagem(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun soma( iValor: Int) {
        iItens = iItens + 1
        soma = soma + iValor
        textViewValue.setText("$soma,00")
    }
    fun remove( iValor: Int) {
        if (iValor > 0 ) {
            soma = soma - iValor
        }
        textViewValue.setText("$soma,00")
         if (soma <= 0) {
            zerarValores()
             textViewValue.setText("00,00")
         }
    }
    fun zerarValores() {
        soma = 0
        iNum = 0
        textViewValue.text = "0"
    }
}


