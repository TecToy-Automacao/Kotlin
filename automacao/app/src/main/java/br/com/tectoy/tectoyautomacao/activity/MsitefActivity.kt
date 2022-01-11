package br.com.tectoy.tectoyautomacao.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import br.com.example.tectoy.tectoyautomacao.R
import br.com.tectoy.tectoyautomacao.Utils.TectoySunmiPrint
import com.google.gson.Gson
import org.jetbrains.annotations.Nullable
import org.json.JSONException
import org.json.JSONObject
import java.lang.StringBuilder
import java.text.DateFormat
import java.util.*

class MsitefActivity: AppCompatActivity() {

    private val r = Random()
    private val dt = Date()
    private val op = r.nextInt(99999).toString()
    private val currentDateTimeString = DateFormat.getDateInstance().format(Date())
    private val currentDateTimeStringT =
        dt.hours.toString() + dt.minutes.toString() + dt.seconds.toString()
    private val REQ_CODE = 4321
    var gson = Gson()
    var acao = "venda"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.msitef)

        val valor = findViewById<EditText>(R.id.txtValorOperacao)
        val ip = findViewById<EditText>(R.id.edt_ip)
        val parcelas = findViewById<EditText>(R.id.edt_parcelas)
        val pagamento = findViewById<TextView>(R.id.spTipoPagamento)
        val parcelamento = findViewById<TextView>(R.id.spTipoParcelamento)
        val usb = findViewById<CheckBox>(R.id.chc_USB)
        val blu = findViewById<CheckBox>(R.id.chc_blu)
        val pagar = findViewById<Button>(R.id.btnPagar)
        pagar.setOnClickListener {
            execulteSTefVenda()
        }
        val adm = findViewById<Button>(R.id.btnAdministrativo)
        adm.visibility = View.GONE;
        val cancelamento = findViewById<Button>(R.id.btnCancelamento)
        cancelamento.setOnClickListener {
            execulteSTefCancelamento()
        }
        val reimpressao = findViewById<Button>(R.id.btnRepressao)
        reimpressao.setOnClickListener {
            execulteSTefReimpressao()
        }
    }
    // transação
    fun execulteSTefVenda() {
        var teste =  "Não Definido";
        var parcelas = "0";
        val intentSitef = Intent("br.com.softwareexpress.sitef.msitef.ACTIVITY_CLISITEF")
        intentSitef.putExtra("empresaSitef", "00000000")
        intentSitef.putExtra("enderecoSitef", "192.168.45.95")
        intentSitef.putExtra("operador", "0001")
        intentSitef.putExtra("data", "20200324")
        intentSitef.putExtra("hora", "130358")
        intentSitef.putExtra("numeroCupom", op)
        intentSitef.putExtra("valor", "1000")
        intentSitef.putExtra("CNPJ_CPF", "03654119000176")
        intentSitef.putExtra("comExterna", "0")
        if (false) {
            intentSitef.putExtra("pinpadMac", "00:00:00:00:00:00")
        }
        if (teste == "Não Definido") {
            intentSitef.putExtra("modalidade", "0")
        } else if ("Crédito" == teste) {
            intentSitef.putExtra("modalidade", "3")
            if (parcelas.toString() == "0" || parcelas
                    .toString() == "1"
            ) {
                intentSitef.putExtra("transacoesHabilitadas", "26")
            } else if (true) {
                // Essa informações habilida o parcelamento Loja
                intentSitef.putExtra("transacoesHabilitadas", "27")
            }
            intentSitef.putExtra("numParcelas", parcelas.toString())
        } else if ("Débito" == teste) {
            intentSitef.putExtra("modalidade", "2")
            //intentSitef.putExtra("transacoesHabilitadas", "16");
        } else if ("Carteira Digital" == teste) {
        }
        intentSitef.putExtra("isDoubleValidation", "0")
        intentSitef.putExtra("caminhoCertificadoCA", "ca_cert_perm")
        startActivityForResult(intentSitef, REQ_CODE)
    }
    private fun execulteSTefCancelamento() {
        val intentSitef = Intent("br.com.softwareexpress.sitef.msitef.ACTIVITY_CLISITEF")
        intentSitef.putExtra("empresaSitef", "00000000")
        intentSitef.putExtra("enderecoSitef", "192.168.45.95")
        intentSitef.putExtra("operador", "0001")
        intentSitef.putExtra("data", currentDateTimeString)
        intentSitef.putExtra("hora", currentDateTimeStringT)
        intentSitef.putExtra("numeroCupom", op)
        intentSitef.putExtra("valor", "1000")
        intentSitef.putExtra("CNPJ_CPF", "03654119000176")
        intentSitef.putExtra("comExterna", "0")
        intentSitef.putExtra("modalidade", "200")
        intentSitef.putExtra("isDoubleValidation", "0")
        intentSitef.putExtra("caminhoCertificadoCA", "ca_cert_perm")
        startActivityForResult(intentSitef, REQ_CODE)
    }
    private fun execulteSTefFuncoes() {
        val intentSitef = Intent("br.com.softwareexpress.sitef.msitef.ACTIVITY_CLISITEF")
        intentSitef.putExtra("empresaSitef", "00000000")
        intentSitef.putExtra("enderecoSitef", "192.168.45.95")
        intentSitef.putExtra("operador", "0001")
        intentSitef.putExtra("data", currentDateTimeString)
        intentSitef.putExtra("hora", currentDateTimeStringT)
        intentSitef.putExtra("numeroCupom", op)
        intentSitef.putExtra("valor", "1000")
        intentSitef.putExtra("CNPJ_CPF", "03654119000176")
        intentSitef.putExtra("comExterna", "0")
        intentSitef.putExtra("isDoubleValidation", "0")
        intentSitef.putExtra("caminhoCertificadoCA", "ca_cert_perm")
        intentSitef.putExtra("modalidade", "110")
        intentSitef.putExtra("restricoes", "transacoesHabilitadas=16;26;27")
        startActivityForResult(intentSitef, REQ_CODE)
    }
    private fun execulteSTefReimpressao() {
        val intentSitef = Intent("br.com.softwareexpress.sitef.msitef.ACTIVITY_CLISITEF")
        intentSitef.putExtra("empresaSitef", "00000000")
        intentSitef.putExtra("enderecoSitef", "192.168.45.95")
        intentSitef.putExtra("operador", "0001")
        intentSitef.putExtra("data", "20200324")
        intentSitef.putExtra("hora", "130358")
        intentSitef.putExtra("numeroCupom", op)
        intentSitef.putExtra("valor", "1000")
        intentSitef.putExtra("CNPJ_CPF", "03654119000176")
        intentSitef.putExtra("comExterna", "0")
        intentSitef.putExtra("modalidade", "114")
        intentSitef.putExtra("isDoubleValidation", "0")
        intentSitef.putExtra("caminhoCertificadoCA", "ca_cert_perm")
        startActivityForResult(intentSitef, REQ_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var retornoSitef: RetornoMsiTef? = null
        if (data == null) return
        try {
            retornoSitef = gson.fromJson(respSitefToJson(data), RetornoMsiTef::class.java)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            if (retornoSitef != null) {
                if (retornoSitef.getCodResp().equals("0")) {
                    var impressao = ""
                    // Verifica se tem algo pra imprimir
                    if (retornoSitef != null) {
                        if (!retornoSitef.textoImpressoCliente().isEmpty()) {
                            impressao += retornoSitef.textoImpressoCliente()
                        }
                    }
                    if (!retornoSitef.textoImpressoEstabelecimento().isEmpty()) {
                        impressao += "\n\n-----------------------------     \n"
                        impressao += retornoSitef.textoImpressoEstabelecimento()
                    }
                    if (!impressao.isEmpty() && acao == "reimpressao") {
                        dialogImpressao(impressao, 17)
                    }
                }
            }
            // Verifica se ocorreu um erro durante venda ou cancelamento
            if (acao == "venda" || acao == "cancelamento") {
                if (retornoSitef != null) {
                    if (retornoSitef.getCodResp().isEmpty() || !retornoSitef.getCodResp()
                            .equals("0") || retornoSitef.getCodResp() == null
                    ) {
                        //dialodTransacaoNegadaMsitef(retornoSitef);
                    } else {
                        dialodTransacaoAprovadaMsitef(retornoSitef)
                    }
                }
            }
        } else {
            // ocorreu um erro
            if (acao == "venda" || acao == "cancelamento") {
                //dialodTransacaoNegadaMsitef(retornoSitef);
            }
        }
    }
    private fun dialodTransacaoAprovadaMsitef(retornoMsiTef: RetornoMsiTef) {
        val alertDialog: AlertDialog = AlertDialog.Builder(this).create()
        val cupom = StringBuilder()
        val teste = StringBuilder()
        cupom.append("""
    Via Cliente 
    ${retornoMsiTef.viA_CLIENTE}
    
    """.trimIndent())
        teste.append("""
    Via Estabelecimento 
    ${retornoMsiTef.viA_ESTABELECIMENTO}
    
    """.trimIndent())
        alertDialog.setTitle("Ação executada com sucesso")
        alertDialog.setMessage(cupom.toString())
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
            DialogInterface.OnClickListener { dialogInterface, i ->
                TectoySunmiPrint.getInstance().setSize(20)
                TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
                TectoySunmiPrint.getInstance().printStyleBold(true)
                TectoySunmiPrint.getInstance().printText(cupom.toString())
                TectoySunmiPrint.getInstance().print3Line()
                TectoySunmiPrint.getInstance().feedPaper()
                TectoySunmiPrint.getInstance().printText(teste.toString())
                TectoySunmiPrint.getInstance().print3Line()
                TectoySunmiPrint.getInstance().cutpaper()
            })
        alertDialog.show()
    }
    @Throws(JSONException::class)
    fun respSitefToJson(data: Intent): String? {
        val json = JSONObject()
        json.put("CODRESP", data.getStringExtra("CODRESP"))
        json.put("COMP_DADOS_CONF", data.getStringExtra("COMP_DADOS_CONF"))
        json.put("CODTRANS", data.getStringExtra("CODTRANS"))
        json.put("VLTROCO", data.getStringExtra("VLTROCO"))
        json.put("REDE_AUT", data.getStringExtra("REDE_AUT"))
        json.put("BANDEIRA", data.getStringExtra("BANDEIRA"))
        json.put("NSU_SITEF", data.getStringExtra("NSU_SITEF"))
        json.put("NSU_HOST", data.getStringExtra("NSU_HOST"))
        json.put("COD_AUTORIZACAO", data.getStringExtra("COD_AUTORIZACAO"))
        json.put("NUM_PARC", data.getStringExtra("NUM_PARC"))
        json.put("TIPO_PARC", data.getStringExtra("TIPO_PARC"))
        json.put("VIA_ESTABELECIMENTO", data.getStringExtra("VIA_ESTABELECIMENTO"))
        json.put("VIA_CLIENTE", data.getStringExtra("VIA_CLIENTE"))
        return json.toString()
    }
    private fun dialogImpressao(texto: String, size: Int) {
        TectoySunmiPrint.getInstance().printText(texto)
    }
}