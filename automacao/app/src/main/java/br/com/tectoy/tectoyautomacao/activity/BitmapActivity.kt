package br.com.tectoy.tectoyautomacao.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import br.com.example.tectoy.tectoyautomacao.R
import br.com.tectoy.tectoyautomacao.Utils.TectoySunmiPrint
import sunmi.sunmiui.dialog.DialogCreater
import sunmi.sunmiui.dialog.ListDialog
import sunmi.sunmiui.dialog.ListDialog.ItemClickListener

class BitmapActivity : AppCompatActivity() {

    private var aling = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bitmap)
        val ll = findViewById<LinearLayout>(R.id.pic_style)
        ll.visibility = View.GONE
        val mText1 = findViewById<TextView>(R.id.pic_align_info)
        mText1.setOnClickListener{
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
                mText1.setText(pos[position])
                aling = position
                listDialog.cancel()
            }
            listDialog.show()
        }
        val mText2 = findViewById<TextView>(R.id.cut_paper_info)
        mText2.setOnClickListener {
            val cut = arrayOf("Sim", "Não")
            val listDialog = DialogCreater.createListDialog(
                this,
                resources.getString(R.string.error_qrcode),
                resources.getString(R.string.cancel),
                cut
            )
            listDialog.setItemClickListener { position ->
                mText2.setText(cut[position])
                listDialog.cancel()
            }
            listDialog.show()
        }
        val imagem = findViewById<ImageView>(R.id.bitmap_imageview)
        val options = Options()
        options.inTargetDensity = 160
        options.inDensity = 160
        var bitmap1: Bitmap? = null
        var bitmap: Bitmap? = null
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.test, options)

        }

        if (bitmap1 == null) {
            bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.test1, options)
            bitmap1 = scaleImage(bitmap1)
        }
        imagem.setImageDrawable(BitmapDrawable(bitmap1))

        val btn = findViewById<TextView>(R.id.btn_image)
        btn.setOnClickListener {
            if (mText2.getText().toString() === "Não") {
                TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
                TectoySunmiPrint.getInstance().printText("Imagem\n")
                TectoySunmiPrint.getInstance().printText("--------------------------------\n")
                if (aling == 0){
                    TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_LEFT)
                }else if(aling == 1){
                    TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
                }else if (aling == 2){
                    TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_RIGTH)
                }
                TectoySunmiPrint.getInstance().printBitmap(bitmap)
                TectoySunmiPrint.getInstance().print3Line()
                TectoySunmiPrint.getInstance().feedPaper()
            } else {
                TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
                TectoySunmiPrint.getInstance().printText("Imagem\n")
                TectoySunmiPrint.getInstance().printText("--------------------------------\n")
                if (aling == 0){
                    TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_LEFT)
                }else if(aling == 1){
                    TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
                }else if (aling == 2){
                    TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_RIGTH)
                }
                TectoySunmiPrint.getInstance().printBitmap(bitmap)
                TectoySunmiPrint.getInstance().print3Line()
                TectoySunmiPrint.getInstance().feedPaper()
                TectoySunmiPrint.getInstance().cutpaper()
            }
        }
    }
    fun scaleImage(bitmap1: Bitmap): Bitmap? {
        val width = bitmap1.width
        val height = bitmap1.height

        val newWidth = (width / 8 + 1) * 8

        val scaleWidth = newWidth.toFloat() / width

        val matrix = Matrix()
        matrix.postScale(scaleWidth, 1f)

        return Bitmap.createBitmap(bitmap1, 0, 0, width, height, matrix, true)
    }
}