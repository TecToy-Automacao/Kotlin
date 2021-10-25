package br.com.tectoy.tectoyautomacao.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import br.com.example.tectoy.tectoyautomacao.R
import sunmi.sunmiui.button.ButtonRectangular
import java.util.*
import android.widget.BaseAdapter
import br.com.tectoy.tectoyautomacao.Utils.TectoySunmiPrint
import android.support.v7.recyclerview.extensions.ListAdapter as ListAdapter


class TableActivity : AppCompatActivity() {

    var  datalist = LinkedList<TableItem>()
    var ta: android.widget.ListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table)


        var convertView = LayoutInflater.from(this@TableActivity).inflate(R.layout.item_table, null)

        val mListView = findViewById<ListView>(R.id.table_list)
        mListView.addHeaderView(convertView)
        val btn = findViewById<TextView>(R.id.btn_imprimi)

        btn.setOnClickListener {
            for (tableItem in datalist) {
                TectoySunmiPrint.getInstance().printTable(tableItem.text,
                    tableItem.width, tableItem.align)
            }
            TectoySunmiPrint.getInstance().feedPaper()
        }

        initListView()


    }


    private fun initListView() {
        var convertView = LayoutInflater.from(this@TableActivity).inflate(R.layout.item_table, null)
        var rows = findViewById<TextView>(R.id.it_title)
        var view = convertView;
        val mListView = findViewById<ListView>(R.id.table_list)
        val mTextView = findViewById<TextView>(R.id.table_tips)
        var footView = ButtonRectangular(this)
        footView.setTitleText(resources.getString(R.string.add_line))
        footView.setTextColorEnabled(R.color.black)
        footView.setOnClickListener(View.OnClickListener {
            addOneData(datalist)
            //mListView.addHeaderView(view)
            mListView.adapter = ta
        })
        mListView.addFooterView(footView)
        addOneData(datalist)
        mListView.adapter = ta
    }
    fun addOneData(data: LinkedList<TableItem>) {
        val ti = TableItem()
        data.add(ti)
    }
    class TableItem {


        var text: Array<String>
        var width: IntArray
        var align: IntArray

        init {
            text = arrayOf("test", "test", "test")
            width = intArrayOf(1, 1, 1)
            align = intArrayOf(0, 0, 0)
        }
    }

}


