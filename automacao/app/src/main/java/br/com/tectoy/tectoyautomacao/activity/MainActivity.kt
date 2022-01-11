package br.com.tectoy.tectoyautomacao.activity


import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import br.com.example.tectoy.tectoyautomacao.R
import br.com.tectoy.tectoyautomacao.Utils.KTectoySunmiPrinter
import br.com.tectoy.tectoyautomacao.Utils.TectoySunmiPrint
import br.com.tectoy.tectoyautomacao.activity.nfc.NfcExemplo
import com.sunmi.extprinterservice.ExtPrinterService
import java.lang.Exception
import java.lang.StringBuilder


open class MainActivity : AppCompatActivity() {

    private var extPrinterService: ExtPrinterService? = null
    var kPrinterPresenter: KTectoySunmiPrinter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Coneção Impressão K2

        val wordnfc = findViewById<Button>(R.id.wordnfc)
        wordnfc.setOnClickListener {
            val intent = Intent(this, NfcExemplo::class.java)
            startActivity(intent)
        }
        val wordlcd = findViewById<Button>(R.id.wordlcd)
        wordlcd.setOnClickListener {
            val intent = Intent(this, LcdActivity::class.java)
            if (getDeviceName().equals("SUNMI T2mini")) {
                    startActivity(intent)
            }else{
                val context = applicationContext
                val text: CharSequence = "Função Não Disponivel No Device"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, text, duration)
                toast.show()
            }
        }
        val workText = findViewById<Button>(R.id.worktext)
        workText.setOnClickListener{
            if (getDeviceName() == "SUNMI L2" || getDeviceName() == "SUNMI L2K" || getDeviceName() == "SUNMI P2mini" || getDeviceName() == "SUNMI D2mini") {
                val context = applicationContext
                val text: CharSequence = "Função Não Disponivel No Device"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, text, duration)
                toast.show()
                println("Passo Aqui")
            }else {
                if (getDeviceName() == "SUNMI K2") {
                    KTesteCompleto()
                } else {
                    testeCompleto()
                }
            }
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
        wordcortar.setOnClickListener{
            if (getDeviceName() == "SUNMI L2" || getDeviceName() == "SUNMI L2K" || getDeviceName() == "SUNMI P2mini" || getDeviceName() == "SUNMI D2mini") {
                val context = applicationContext
                val text: CharSequence = "Função Não Disponivel No Device"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, text, duration)
                toast.show()
                println("Passo Aqui")
            }else {
                cut()
            }
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
            if (getDeviceName() == "SUNMI L2" || getDeviceName() == "SUNMI L2K" || getDeviceName() == "SUNMI P2mini" || getDeviceName() == "SUNMI V2_PRO") {
                val context = applicationContext
                val text: CharSequence = "Função Não Disponivel No Device"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, text, duration)
                toast.show()
            }else {
                val intent = Intent(this, ScannerActivity::class.java)
                startActivity(intent)
            }
        }
        val btn_label = findViewById<Button>(R.id.wordlabel)
        btn_label.setOnClickListener {
            val intent = Intent(this, LabelActivity::class.java)
            startActivity(intent)
        }
        val btn_scan = findViewById<Button>(R.id.wordteste)
        btn_scan.setOnClickListener {
            if (getDeviceName() == "SUNMI L2" || getDeviceName() == "SUNMI L2K" || getDeviceName() == "SUNMI P2mini" || getDeviceName() == "SUNMI V2_PRO") {
                val intent = Intent(this, ScanActivity::class.java)
                startActivity(intent)
            }else {
                val context = applicationContext
                val text: CharSequence = "Função Não Disponivel No Device"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, text, duration)
                toast.show()
            }
        }
        val btn_msitef = findViewById<Button>(R.id.wordmsitef)
        btn_msitef.setOnClickListener {
            val intent = Intent(this, MsitefActivity::class.java)
            startActivity(intent)
        }
        val btn_paygo = findViewById<Button>(R.id.wordpaygo)
        btn_paygo.setOnClickListener {
            val intent = Intent(this, PaygoActivity::class.java)
            startActivity(intent)
        }
        if (getDeviceName() == "SUNMI K2") {
        connectKPrintService()
        }

    }
    open fun connectKPrintService() {
        val intent = Intent()
        intent.setPackage("com.sunmi.extprinterservice")
        intent.action = "com.sunmi.extprinterservice.PrinterService"
        bindService(intent, connService, BIND_AUTO_CREATE)
    }

    val connService: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            extPrinterService = null
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            extPrinterService = ExtPrinterService.Stub.asInterface(service)
          kPrinterPresenter = KTectoySunmiPrinter(this@MainActivity, extPrinterService)
        }
    }
    private fun avanco(){
        TectoySunmiPrint.getInstance().initPrinter()
        TectoySunmiPrint.getInstance().print3Line()
    }
    private fun status(){
        if (getDeviceName() == "SUNMI K2") {
            try {
                Toast.makeText(applicationContext,
                    kPrinterPresenter?.traduzStatusImpressora(kPrinterPresenter!!.getStatus()),
                    Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            TectoySunmiPrint.getInstance().showPrinterStatus(this@MainActivity)
        }
    }
    private fun gaveta(){
        TectoySunmiPrint.getInstance().openCashBox()
    }
    private fun cut(){
        if (getDeviceName() == "SUNMI K2") {
            try {

                kPrinterPresenter?.print3Line()
                kPrinterPresenter?.cutpaper(KTectoySunmiPrinter.CUTTING_PAPER_FEED, 10)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }else {
            TectoySunmiPrint.getInstance().cutpaper()
        }
    }
    @Throws(RemoteException::class)
    open fun KTesteCompleto() {

        // Alinhamento


        // Alinhamento
        kPrinterPresenter?.printStyleBold(false)
        kPrinterPresenter?.setAlign(1)
        kPrinterPresenter?.text("Alinhamento\n")
        kPrinterPresenter?.text("--------------------------------\n")
        kPrinterPresenter?.setAlign(0)
        kPrinterPresenter?.text("TecToy Automação\n")
        kPrinterPresenter?.setAlign(1)
        kPrinterPresenter?.text("TecToy Automação\n")
        kPrinterPresenter?.setAlign(2)
        kPrinterPresenter?.text("TecToy Automação\n")
        kPrinterPresenter?.print3Line()

        // Formas de impressão


        // Formas de impressão
        kPrinterPresenter?.setAlign(1)
        kPrinterPresenter?.text("Formas de Impressão\n")
        kPrinterPresenter?.text("--------------------------------\n")
        kPrinterPresenter?.setAlign(0)
        kPrinterPresenter?.printStyleBold(true)
        kPrinterPresenter?.text("TecToy Automação\n")


        // Barcode


        // Barcode
        kPrinterPresenter?.setAlign(1)
        kPrinterPresenter?.text("BarCode\n")
        kPrinterPresenter?.text("--------------------------------\n")
        kPrinterPresenter?.setAlign(0)
        kPrinterPresenter?.printBarcode("7891098010575", 2, 162, 2, 0)
        kPrinterPresenter?.setAlign(1)
        kPrinterPresenter?.printBarcode("7891098010575", 2, 162, 2, 2)
        kPrinterPresenter?.setAlign(2)
        kPrinterPresenter?.printBarcode("7891098010575", 2, 162, 2, 1)
        kPrinterPresenter?.setAlign(1)
        kPrinterPresenter?.printBarcode("7891098010575", 2, 162, 2, 3)
        kPrinterPresenter?.print3Line()
        // QrCode

        // QrCode
        kPrinterPresenter?.setAlign(1)
        kPrinterPresenter?.text("QrCode\n")
        kPrinterPresenter?.text("--------------------------------\n")
        kPrinterPresenter?.setAlign(1)
        kPrinterPresenter?.printQr("www.tectoyautomacao.com.br", 8, 0)
        kPrinterPresenter?.setAlign(0)
        kPrinterPresenter?.printQr("www.tectoyautomacao.com.br", 8, 0)
        kPrinterPresenter?.setAlign(2)
        kPrinterPresenter?.printQr("www.tectoyautomacao.com.br", 8, 0)
        kPrinterPresenter?.setAlign(0)
        kPrinterPresenter?.printDoubleQRCode("www.tectoyautomacao.com.br",
            "tectoy",
            7,
            1)
        // Imagem


        // Imagem
        kPrinterPresenter?.setAlign(1)
        kPrinterPresenter?.text("Imagem\n")
        kPrinterPresenter?.text("--------------------------------\n")
        val options = Options()
        options.inTargetDensity = 160
        options.inDensity = 160
        var bitmap1: Bitmap? = null
        if (bitmap1 == null) {
            bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.test1, options)
            bitmap1 = scaleImage(bitmap1)
        }
        kPrinterPresenter?.printBitmap(bitmap1, 0)
        kPrinterPresenter?.setAlign(0)
        kPrinterPresenter?.printBitmap(bitmap1, 0)
        kPrinterPresenter?.setAlign(2)
        kPrinterPresenter?.printBitmap(bitmap1, 0)
        // Tabelas


        // Tabelas
        kPrinterPresenter?.setAlign(1)
        kPrinterPresenter?.text("Tabelas\n")
        kPrinterPresenter?.text("--------------------------------\n")
        val prod = arrayOfNulls<String>(3)
        val width = IntArray(3)
        val align = IntArray(3)

        width[0] = 100
        width[1] = 50
        width[2] = 50

        align[0] = 0
        align[1] = 1
        align[2] = 2

        prod[0] = "Produto 001"
        prod[1] = "10 und"
        prod[2] = "3,98"
        kPrinterPresenter?.printTable(prod, width, align)

        prod[0] = "Produto 002"
        prod[1] = "10 und"
        prod[2] = "3,98"
        kPrinterPresenter?.printTable(prod, width, align)


        prod[0] = "Produto 003"
        prod[1] = "10 und"
        prod[2] = "3,98"
        kPrinterPresenter?.printTable(prod, width, align)


        prod[0] = "Produto 004"
        prod[1] = "10 und"
        prod[2] = "3,98"
        kPrinterPresenter?.printTable(prod, width, align)


        prod[0] = "Produto 005"
        prod[1] = "10 und"
        prod[2] = "3,98"
        kPrinterPresenter?.printTable(prod, width, align)

        prod[0] = "Produto 006"
        prod[1] = "10 und"
        prod[2] = "3,98"
        kPrinterPresenter?.printTable(prod, width, align)


        kPrinterPresenter?.print3Line()
        kPrinterPresenter?.cutpaper(KTectoySunmiPrinter.HALF_CUTTING, 10)
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
    open fun getDeviceName(): String? {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else capitalize(manufacturer) + " " + model
    }

    open fun capitalize(str: String): String {
        if (TextUtils.isEmpty(str)) {
            return str
        }
        val arr = str.toCharArray()
        var capitalizeNext = true
        val phrase = StringBuilder()
        for (c in arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c))
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true
            }
            phrase.append(c)
        }
        return phrase.toString()
    }

}




