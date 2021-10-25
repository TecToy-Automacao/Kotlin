package br.com.tectoy.tectoyautomacao.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import br.com.example.tectoy.tectoyautomacao.R
import br.com.tectoy.tectoyautomacao.Utils.TectoySunmiPrint
import sunmi.sunmiui.dialog.DialogCreater

class TextActivity : AppCompatActivity(){
    private var record = 0
    private val mStrings = arrayOf(
        "CP437",
        "CP850",
        "CP860",
        "CP863",
        "CP865",
        "CP857",
        "CP737",
        "CP928",
        "Windows-1252",
        "CP866",
        "CP852",
        "CP858",
        "CP874",
        "Windows-775",
        "CP855",
        "CP862",
        "CP864",
        "GB18030",
        "BIG5",
        "KSC5601",
        "utf-8"
    )
    private var isBold = false
    private  var isUnderLine:Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)
        isBold = false
        isUnderLine = false
        val mText1 = findViewById<TextView>(R.id.text_text_character)
        record = 17

        mText1.setOnClickListener {
            val listDialog = DialogCreater.createListDialog(
                this@TextActivity,
                resources.getString(R.string.characterset),
                resources.getString(R.string.cancel),
                mStrings
            )
            listDialog.setItemClickListener { position ->
                mText1.setText(mStrings.get(position))
                record = position
                listDialog.cancel()
            }
            listDialog.show()
        }
        val mText2 = findViewById<TextView>(R.id.text_text_size)
        mText2.setOnClickListener {
            showSeekBarDialog(
                this@TextActivity,
                resources.getString(R.string.size_text),
                12,
                36,
                mText2
            )

        }


        val check_bold = findViewById<CheckBox>(R.id.text_bold)
        check_bold.setOnClickListener {
            isBold = true
        }
        val check_underline = findViewById<CheckBox>(R.id.text_underline)
        check_underline.setOnClickListener {
            isUnderLine = true
        }






        val texto = findViewById<EditText>(R.id.text_text)
        texto.setOnClickListener {  }
        val btn = findViewById<TextView>(R.id.btn_imprimir)
        btn.setOnClickListener {
            val content: String = texto.getText().toString()

            val size: Float = mText2.getText().toString().toInt().toFloat()

            TectoySunmiPrint.getInstance().printStyleBold(isBold)
            TectoySunmiPrint.getInstance().printStyleUnderLine(isUnderLine)

            TectoySunmiPrint.getInstance().printTextWithSize(content, size)
            TectoySunmiPrint.getInstance().feedPaper()
        }
    }
    private fun showSeekBarDialog(
        context: Context,
        title: String,
        min: Int,
        max: Int,
        set: TextView
    ) {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        val view: View = LayoutInflater.from(context).inflate(R.layout.widget_seekbar, null)
        builder.setView(view)
        builder.setCancelable(false)
        val dialog = builder.create()
        val tv_title = view.findViewById<View>(R.id.sb_title) as TextView
        val tv_start = view.findViewById<View>(R.id.sb_start) as TextView
        val tv_end = view.findViewById<View>(R.id.sb_end) as TextView
        val tv_result = view.findViewById<View>(R.id.sb_result) as TextView
        val tv_ok = view.findViewById<View>(R.id.sb_ok) as TextView
        val tv_cancel = view.findViewById<View>(R.id.sb_cancel) as TextView
        val sb = view.findViewById<View>(R.id.sb_seekbar) as SeekBar
        tv_title.text = title
        tv_start.text = min.toString() + ""
        tv_end.text = max.toString() + ""
        tv_result.text = set.text
        tv_cancel.setOnClickListener { dialog.cancel() }
        tv_ok.setOnClickListener {
            set.text = tv_result.text
            dialog.cancel()
        }
        sb.max = max - min
        sb.progress = set.text.toString().toInt() - min
        sb.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val rs = min + progress
                tv_result.text = rs.toString() + ""
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        dialog.show()
    }
}