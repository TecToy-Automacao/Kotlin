package br.com.tectoy.tectoyautomacao.activity

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import br.com.example.tectoy.tectoyautomacao.R
import br.com.tectoy.tectoyautomacao.Utils.BitmapUtil
import br.com.tectoy.tectoyautomacao.Utils.BluetoothUtil
import br.com.tectoy.tectoyautomacao.Utils.TectoySunmiPrint
import sunmi.sunmiui.dialog.DialogCreater
import sunmi.sunmiui.dialog.EditTextDialog
import sunmi.sunmiui.dialog.ListDialog
import sunmi.sunmiui.dialog.ListDialog.ItemClickListener


class QrActivity : AppCompatActivity() {

    private var print_size = 8
    private var error_level = 3
    private var print_num = 0
    private var aling = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)

        val mText1 = findViewById<TextView>(R.id.qr_text_content)
        mText1.setOnClickListener{
            var mDialog: EditTextDialog? = null
            mDialog = DialogCreater.createEditTextDialog(this,
                resources.getString(R.string.cancel),
                resources.getString(R.string.confirm),
                resources.getString(R.string.input_qrcode),
                { mDialog?.cancel() },
                {
                    mText1.setText(mDialog!!.getEditText().getText())
                    mDialog?.cancel()
                },
                null
            )
            mDialog.setHintText("www.tectoysunmi.com.br")
            mDialog.show()
        }
        val mText2 = findViewById<TextView>(R.id.qr_text_num)
        mText2.setOnClickListener {
            val mStrings =
                arrayOf(resources.getString(R.string.single), resources.getString(R.string.twice))
            val listDialog = DialogCreater.createListDialog(
                this@QrActivity,
                resources.getString(R.string.array_qrcode),
                resources.getString(R.string.cancel),
                mStrings
            )
            listDialog.setItemClickListener { position ->
                var position = position
                if (!BluetoothUtil.isBlueToothPrinter) {
                    Toast.makeText(this, R.string.toast_7, Toast.LENGTH_LONG).show()
                    position = 0
                } else {
                    val mText3: TextView? = null
                    mText3!!.setText("7")
                    print_size = 7
                }
                mText2.setText(mStrings[position])
                print_num = position
                listDialog.cancel()
            }
            listDialog.show()
        }
        val mText3 = findViewById<TextView>(R.id.qr_text_size)
        mText3.setOnClickListener {
            val listDialog = DialogCreater.createListDialog(
                this,
                resources.getString(R.string.size_qrcode),
                resources.getString(R.string.cancel),
                arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
            )
            listDialog.setItemClickListener { position ->
                var position = position
                position += 1
                if (print_num == 1 && mText3.getText().toString().toInt() > 7) {
                    Toast.makeText(this, R.string.toast_8, Toast.LENGTH_LONG).show()
                    position = 7
                }
                mText3.setText("" + position)
                print_size = position
                listDialog.cancel()
            }
            listDialog.show()
        }
        val mText4 = findViewById<TextView>(R.id.qr_text_el)
        mText4.setOnClickListener {
            val el = arrayOf(
                "Correção L (7%)",
                "Correção M (15%)",
                "Correção Q (25%)",
                "Correção H (30%)"
            )
            val listDialog = DialogCreater.createListDialog(
                this,
                resources.getString(R.string.error_qrcode),
                resources.getString(R.string.cancel),
                el
            )
            listDialog.setItemClickListener { position ->
                mText4.setText(el[position])
                error_level = position
                listDialog.cancel()
            }
            listDialog.show()
        }
        val mText5 = findViewById<TextView>(R.id.qr_align_info)
        mText5.setOnClickListener {
            val pos = arrayOf(
                resources.getString(R.string.align_left),
                resources.getString(R.string.align_mid),
                resources.getString(R.string.align_right)
            )
            val listDialog = DialogCreater.createListDialog(
                this,
                resources.getString(R.string.align_form),
                resources.getString(R.string.cancel),
                pos
            )
            listDialog.setItemClickListener { position ->
               mText5.setText(pos[position])
             aling = position

                listDialog.cancel()
            }
            listDialog.show()
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
                error_level = position
                listDialog.cancel()
            }
            listDialog.show()
        }
        val imagem = findViewById<ImageView>(R.id.qr_image)
        val btn = findViewById<TextView>(R.id.btn_qr)
        btn.setOnClickListener {
            val bitmap: Bitmap =
                BitmapUtil.generateBitmap(mText1.getText().toString(), 9, 700, 700)
            if (bitmap != null) {
                imagem.setImageDrawable(BitmapDrawable(bitmap))
            }
            if (mText6.getText().toString() === "Não") {
                TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
                TectoySunmiPrint.getInstance().printText("QrCode\n")
                TectoySunmiPrint.getInstance().printText("--------------------------------\n")
                if (aling == 0){
                    TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_LEFT)
                }else if(aling == 1){
                    TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
                }else if (aling == 2){
                    TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_RIGTH)
                }
                TectoySunmiPrint.getInstance()
                    .printQr(mText1.getText().toString(), print_size, error_level)
                TectoySunmiPrint.getInstance().print3Line()
                TectoySunmiPrint.getInstance().feedPaper()
            } else {
                TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
                TectoySunmiPrint.getInstance().printText("QrCode\n")
                TectoySunmiPrint.getInstance().printText("--------------------------------\n")
                if (aling == 0){
                    TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_LEFT)
                }else if(aling == 1){
                    TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
                }else if (aling == 2){
                    TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_RIGTH)
                }
                TectoySunmiPrint.getInstance()
                    .printQr(mText1.getText().toString(), print_size, error_level)
                TectoySunmiPrint.getInstance().print3Line()
                TectoySunmiPrint.getInstance().feedPaper()
                TectoySunmiPrint.getInstance().cutpaper()
            }
        }
    }
}


