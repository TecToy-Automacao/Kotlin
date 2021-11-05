package br.com.tectoy.tectoyautomacao.activity


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.graphics.Matrix
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import br.com.example.tectoy.tectoyautomacao.R
import br.com.tectoy.tectoyautomacao.Utils.TectoySunmiPrint


open class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val workText = findViewById<Button>(R.id.worktext)
        workText.setOnClickListener{
            testeCompleto()
        }
        val wordthreeline = findViewById<Button>(R.id.wordthreeline)
        wordthreeline.setOnClickListener {
            avanco()
        }
        val wordStatus = findViewById<Button>(R.id.wordstatus)
        wordStatus.setOnClickListener {
            status()
        }
        val wordCash = findViewById<Button>(R.id.wordcash)
        wordCash.setOnClickListener {
            gaveta()
        }
        val wordcortar = findViewById<Button>(R.id.wordcorta)
        workText.setOnClickListener{
            cut()
        }
        val btn_qr = findViewById<Button>(R.id.wordqr)
        btn_qr.setOnClickListener{
            val intent = Intent(this, QrActivity::class.java)
            startActivity(intent)
        }

        val btn_image = findViewById<Button>(R.id.wordimage)
        btn_image.setOnClickListener {
            val intent = Intent(this, BitmapActivity::class.java)
            startActivity(intent)
        }
        val btn_barcode = findViewById<Button>(R.id.wordbarcode)
        btn_barcode.setOnClickListener {
            val intent = Intent(this, BarcodeActivity::class.java)
            startActivity(intent)
        }
        val btn_table = findViewById<Button>(R.id.wordtable)
        btn_table.setOnClickListener {
            val intent = Intent(this, TableActivity::class.java)
            startActivity(intent)
        }
        val btn_text = findViewById<Button>(R.id.wordtext)
        btn_text.setOnClickListener {
            val intent = Intent(this, TextActivity::class.java)
            startActivity(intent)
        }
        val btn_led = findViewById<Button>(R.id.wordled)
        btn_led.setOnClickListener {
            val intent = Intent(this, LedActivity::class.java)
            startActivity(intent)
        }
        val btn_scanner = findViewById<Button>(R.id.wordscanner)
        btn_scanner.setOnClickListener {
            val intent = Intent(this, ScannerActivity::class.java)
            startActivity(intent)
        }
        val btn_label = findViewById<Button>(R.id.wordlabel)
        btn_label.setOnClickListener {
            val intent = Intent(this, LabelActivity::class.java)
            startActivity(intent)
        }

    }
    private fun avanco(){
        TectoySunmiPrint.getInstance().initPrinter()
        TectoySunmiPrint.getInstance().print3Line()
    }
    private fun status(){
        TectoySunmiPrint.getInstance().showPrinterStatus(this@MainActivity)
    }
    private fun gaveta(){
        TectoySunmiPrint.getInstance().openCashBox()
    }
    private fun cut(){
        TectoySunmiPrint.getInstance().cutpaper()
    }
    private fun testeCompleto() {


        TectoySunmiPrint.getInstance().initPrinter()
        TectoySunmiPrint.getInstance().setSize(24)

        // Alinhamento do texto


        // Alinhamento do texto
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
        TectoySunmiPrint.getInstance().printText("Alinhamento\n")
        TectoySunmiPrint.getInstance().printText("--------------------------------\n")
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_LEFT)
        TectoySunmiPrint.getInstance().printText("TecToy Automação\n")
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
        TectoySunmiPrint.getInstance().printText("TecToy Automação\n")
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_RIGTH)
        TectoySunmiPrint.getInstance().printText("TecToy Automação\n")

        // Formas de impressão

        // Formas de impressão
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
        TectoySunmiPrint.getInstance().printText("Formas de Impressão\n")
        TectoySunmiPrint.getInstance().printText("--------------------------------\n")
        TectoySunmiPrint.getInstance().setSize(28)
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_LEFT)
        TectoySunmiPrint.getInstance().printStyleBold(true)
        TectoySunmiPrint.getInstance().printText("TecToy Automação\n")
        TectoySunmiPrint.getInstance().printStyleReset()
        TectoySunmiPrint.getInstance().printStyleAntiWhite(true)
        TectoySunmiPrint.getInstance().printText("TecToy Automação\n")
        TectoySunmiPrint.getInstance().printStyleReset()
        TectoySunmiPrint.getInstance().printStyleDoubleHeight(true)
        TectoySunmiPrint.getInstance().printText("TecToy Automação\n")
        TectoySunmiPrint.getInstance().printStyleReset()
        TectoySunmiPrint.getInstance().printStyleDoubleWidth(true)
        TectoySunmiPrint.getInstance().printText("TecToy Automação\n")
        TectoySunmiPrint.getInstance().printStyleReset()
        TectoySunmiPrint.getInstance().printStyleInvert(true)
        TectoySunmiPrint.getInstance().printText("TecToy Automação\n")
        TectoySunmiPrint.getInstance().printStyleReset()
        TectoySunmiPrint.getInstance().printStyleItalic(true)
        TectoySunmiPrint.getInstance().printText("TecToy Automação\n")
        TectoySunmiPrint.getInstance().printStyleReset()
        TectoySunmiPrint.getInstance().printStyleStrikethRough(true)
        TectoySunmiPrint.getInstance().printText("TecToy Automação\n")
        TectoySunmiPrint.getInstance().printStyleReset()
        TectoySunmiPrint.getInstance().printStyleUnderLine(true)
        TectoySunmiPrint.getInstance().printText("TecToy Automação\n")
        TectoySunmiPrint.getInstance().printStyleReset()
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_LEFT)
        TectoySunmiPrint.getInstance().printTextWithSize("TecToy Automação\n", 35F)
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
        TectoySunmiPrint.getInstance().printTextWithSize("TecToy Automação\n", 28F)
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_RIGTH)
        TectoySunmiPrint.getInstance().printTextWithSize("TecToy Automação\n", 50F)
        TectoySunmiPrint.getInstance().feedPaper()
        TectoySunmiPrint.getInstance().setSize(24)


        // Impressão de BarCode


        // Impressão de BarCode
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
        TectoySunmiPrint.getInstance().printText("Imprime BarCode\n")
        TectoySunmiPrint.getInstance().printText("--------------------------------\n")
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_LEFT)
        TectoySunmiPrint.getInstance().printBarCode(
            "7894900700046", TectoySunmiPrint.BarCodeModels_EAN13, 162, 2,
            TectoySunmiPrint.BarCodeTextPosition_INFORME_UM_TEXTO
        )
        TectoySunmiPrint.getInstance().printAdvanceLines(2)
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
        TectoySunmiPrint.getInstance().printBarCode(
            "7894900700046", TectoySunmiPrint.BarCodeModels_EAN13, 162, 2,
            TectoySunmiPrint.BarCodeTextPosition_ABAIXO_DO_CODIGO_DE_BARRAS
        )
        TectoySunmiPrint.getInstance().printAdvanceLines(2)
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_RIGTH)
        TectoySunmiPrint.getInstance().printBarCode(
            "7894900700046", TectoySunmiPrint.BarCodeModels_EAN13, 162, 2,
            TectoySunmiPrint.BarCodeTextPosition_ACIMA_DO_CODIGO_DE_BARRAS_BARCODE
        )
        TectoySunmiPrint.getInstance().printAdvanceLines(2)
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
        TectoySunmiPrint.getInstance().printBarCode(
            "7894900700046", TectoySunmiPrint.BarCodeModels_EAN13, 162, 2,
            TectoySunmiPrint.BarCodeTextPosition_ACIMA_E_ABAIXO_DO_CODIGO_DE_BARRAS
        )
        TectoySunmiPrint.getInstance().feedPaper()

        // Impressão de BarCode

        // Impressão de BarCode
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
        TectoySunmiPrint.getInstance().printText("Imprime QrCode\n")
        TectoySunmiPrint.getInstance().printText("--------------------------------\n")
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_LEFT)
        TectoySunmiPrint.getInstance().printQr("www.tectoysunmi.com.br", 8, 1)
        TectoySunmiPrint.getInstance().feedPaper()
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
        TectoySunmiPrint.getInstance().printQr("www.tectoysunmi.com.br", 8, 1)
        TectoySunmiPrint.getInstance().feedPaper()
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_RIGTH)
        TectoySunmiPrint.getInstance().printQr("www.tectoysunmi.com.br", 8, 1)
        TectoySunmiPrint.getInstance().feedPaper()
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_LEFT)
        TectoySunmiPrint.getInstance()
            .printDoubleQRCode("www.tectoysunmi.com.br", "tectoysunmi", 7, 1)
        TectoySunmiPrint.getInstance().feedPaper()


        // Impresão Imagem


        // Impresão Imagem
        TectoySunmiPrint.getInstance().printText("Imprime Imagem\n")
        TectoySunmiPrint.getInstance().printText("-------------------------------\n")
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
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
        TectoySunmiPrint.getInstance().printBitmap(bitmap1)
        TectoySunmiPrint.getInstance().feedPaper()
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_LEFT)
        TectoySunmiPrint.getInstance().printBitmap(bitmap1)
        TectoySunmiPrint.getInstance().feedPaper()
        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_RIGTH)
        TectoySunmiPrint.getInstance().printBitmap(bitmap1)
        TectoySunmiPrint.getInstance().feedPaper()


        TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
        TectoySunmiPrint.getInstance().printText("Imprime Tabela\n")
        TectoySunmiPrint.getInstance().printText("--------------------------------\n")

        val prod = arrayOfNulls<String>(3)
        val width = IntArray(3)
        val align = IntArray(3)

        width[0] = 100
        width[1] = 50
        width[2] = 50

        align[0] = TectoySunmiPrint.Alignment_LEFT
        align[1] = TectoySunmiPrint.Alignment_CENTER
        align[2] = TectoySunmiPrint.Alignment_RIGTH

        prod[0] = "Produto 001"
        prod[1] = "10 und"
        prod[2] = "3,98"
        TectoySunmiPrint.getInstance().printTable(prod, width, align)

        prod[0] = "Produto 002"
        prod[1] = "10 und"
        prod[2] = "3,98"
        TectoySunmiPrint.getInstance().printTable(prod, width, align)

        prod[0] = "Produto 003"
        prod[1] = "10 und"
        prod[2] = "3,98"
        TectoySunmiPrint.getInstance().printTable(prod, width, align)

        prod[0] = "Produto 004"
        prod[1] = "10 und"
        prod[2] = "3,98"
        TectoySunmiPrint.getInstance().printTable(prod, width, align)

        prod[0] = "Produto 005"
        prod[1] = "10 und"
        prod[2] = "3,98"
        TectoySunmiPrint.getInstance().printTable(prod, width, align)

        prod[0] = "Produto 006"
        prod[1] = "10 und"
        prod[2] = "3,98"
        TectoySunmiPrint.getInstance().printTable(prod, width, align)

        TectoySunmiPrint.getInstance().print3Line()
        TectoySunmiPrint.getInstance().openCashBox()
        TectoySunmiPrint.getInstance().cutpaper()
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




