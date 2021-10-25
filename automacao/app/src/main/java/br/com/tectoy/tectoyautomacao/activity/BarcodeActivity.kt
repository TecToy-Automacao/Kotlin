package br.com.tectoy.tectoyautomacao.activity

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import br.com.example.tectoy.tectoyautomacao.R
import br.com.tectoy.tectoyautomacao.Utils.BitmapUtil
import br.com.tectoy.tectoyautomacao.Utils.TectoySunmiPrint
import sunmi.sunmiui.dialog.DialogCreater
import sunmi.sunmiui.dialog.EditTextDialog
import sunmi.sunmiui.dialog.ListDialog
import sunmi.sunmiui.dialog.ListDialog.ItemClickListener

class BarcodeActivity : AppCompatActivity() {

    private var encode = 0
    private var position = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode)
        setTitle("BarCode")

        encode = 8
        position = 1
        val mImageView = findViewById<ImageView>(R.id.bc_image)
        var texto: String;
        val mText1 = findViewById<TextView>(R.id.bc_text_content)
        mText1.setOnClickListener {
            var mDialog: EditTextDialog? = null
            mDialog = DialogCreater.createEditTextDialog(this,
                resources.getString(R.string.cancel),
                resources.getString(R.string.confirm),
                resources.getString(R.string.input_barcode),
                { mDialog?.cancel() },
                {
                    mText1.setText(mDialog!!.getEditText().getText())
                    mDialog?.cancel()
                },
                null
            )
            mDialog.show()
        }
        val mText2 = findViewById<TextView>(R.id.bc_text_encode)
        mText2.setOnClickListener{
            val list = arrayOf(
                "UPC-A",
                "UPC-E",
                "EAN13",
                "EAN8",
                "CODE39",
                "ITF",
                "CODABAR",
                "CODE93",
                "CODE128A",
                "CODE128B",
                "CODE128C"
            )
            val listDialog = DialogCreater.createListDialog(
                this,
                resources.getString(R.string.encode_barcode),
                resources.getString(R.string.cancel),
                list
            )
            listDialog.setItemClickListener { position ->
                mText2.setText(list[position])
                encode = position
                listDialog.cancel()
            }
            listDialog.show()
        }
        val mText3 = findViewById<TextView>(R.id.bc_text_position)
        mText3.setOnClickListener {
            val list = arrayOf(
                resources.getString(R.string.no_print),
                resources.getString(R.string.barcode_up),
                resources.getString(R.string.barcode_down),
                resources.getString(R.string.barcode_updown)
            )
            val listDialog = DialogCreater.createListDialog(
                this,
                resources.getString(R.string.text_position),
                resources.getString(R.string.cancel),
                list
            )
            listDialog.setItemClickListener { position ->
                mText3.setText(list[position])
                this.position = position
                listDialog.cancel()
            }
            listDialog.show()
        }
        val mText4 = findViewById<TextView>(R.id.bc_text_height)
        mText4.setOnClickListener{
            showSeekBarDialog(
                this,
                resources.getString(R.string.barcode_height),
                1,
                255,
                mText4
            )
        }
        val mText5 = findViewById<TextView>(R.id.bc_text_width)
        mText5.setOnClickListener{
            showSeekBarDialog(
                this,
                resources.getString(R.string.barcode_width),
                2,
                6,
                mText5
            )
        }
        val mText6 = findViewById<TextView>(R.id.cut_paper_info)
        mText6.setOnClickListener {
            val cut = arrayOf("Sim", "Não")
            val listDialog = DialogCreater.createListDialog(
                this,
                resources.getString(R.string.error_qrcode),
                resources.getString(R.string.cancel),
                cut
            )
            listDialog.setItemClickListener { position ->
                mText6.setText(cut[position])
                listDialog.cancel()
            }
            listDialog.show()
        }
        val button = findViewById<TextView>(R.id.textView4)
        button.setOnClickListener {
            val text: String = mText1.getText().toString()
            val symbology: Int
            symbology = if (encode > 7) {
                8
            } else {
                encode
            }
            val bitmap: Bitmap = BitmapUtil.generateBitmap(text, symbology, 700, 400)
            if (bitmap != null) {
                mImageView.setImageDrawable(BitmapDrawable(bitmap))
            } else {
                Toast.makeText(this, R.string.toast_9, Toast.LENGTH_LONG).show()
            }

            val height: Int = mText4.getText().toString().toInt()
            val width: Int = mText5.getText().toString().toInt()

            if (mText6.getText().toString() === "Não") {
                TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
                TectoySunmiPrint.getInstance().printText("BarCode\n")
                TectoySunmiPrint.getInstance().printText("--------------------------------\n")
                TectoySunmiPrint.getInstance().printBarCode(text, encode, height, width, position)
                TectoySunmiPrint.getInstance().feedPaper()
            } else {
                TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
                TectoySunmiPrint.getInstance().printText("BarCode\n")
                TectoySunmiPrint.getInstance().printText("--------------------------------\n")
                TectoySunmiPrint.getInstance().printBarCode(text, encode, height, width, position)
                TectoySunmiPrint.getInstance().print3Line()
                TectoySunmiPrint.getInstance().feedPaper()
                TectoySunmiPrint.getInstance().cutpaper()
            }
        }
        }
    private fun showSeekBarDialog(
        context: Context,
        title: String,
        min: Int,
        max: Int,
        set: TextView
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
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





