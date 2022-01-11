package br.com.tectoy.tectoyautomacao.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import br.com.example.tectoy.tectoyautomacao.R
import br.com.setis.interfaceautomacao.*
import br.com.tectoy.tectoyautomacao.Utils.TectoySunmiPrint
import com.google.gson.Gson
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class PaygoActivity : AppCompatActivity() {

    private val r = Random()
    private val dt = Date()
    private val op = r.nextInt(99999).toString()
    private val currentDateTimeString = DateFormat.getDateInstance().format(Date())
    private val currentDateTimeStringT =
        dt.hours.toString() + dt.minutes.toString() + dt.seconds.toString()
    private val REQ_CODE = 4321
    var gson = Gson()
    var acao = "venda"

    private var mConfirmacao = Confirmacoes()
    private var mDadosAutomacao: DadosAutomacao? = null
    private var mPersonalizacao: Personalizacao? = null
    private var mTransacoes: Transacoes? = null
    private var mSaidaTransacao: SaidaTransacao? = null
    private var mEntradaTransacao: EntradaTransacao? = null
    private var versoes: String? = null
    private var mensagem: String? = null
    private val mHandler = Handler()
    private var nsu: String? = null
    private  var dataOperacao:kotlin.String? = null
    private  var codigoAutorizacao:kotlin.String? = null
    private  var valorOperacao:kotlin.String? = null

    private val REQUEST_CODE = 1000

    private val tectoySunmiPrint: TectoySunmiPrint? = null
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")
//    val valor = findViewById<EditText>(R.id.txtValorOperacaoo)
//    val parcelas = findViewById<EditText>(R.id.edt_parcelas)
//    val pagamento = findViewById<TextView>(R.id.spTipoPagamento)
//    val parcelamento = findViewById<TextView>(R.id.spTipoParcelamento)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.paygo)

        val parcelas = findViewById<EditText>(R.id.edt_parcelas)
        val pagamento = findViewById<TextView>(R.id.spTipoPagamento)
        val parcelamento = findViewById<TextView>(R.id.spTipoParcelamento)

        val pagar = findViewById<Button>(R.id.btnPagar)
        pagar.setOnClickListener {
            efetuaOperacao(Operacoes.VENDA)
        }
        val adm = findViewById<Button>(R.id.btnAdministrativo)
        adm.visibility = View.GONE;
        val cancelamento = findViewById<Button>(R.id.btnCancelamento)
        cancelamento.setOnClickListener {

        }
        val reimpressao = findViewById<Button>(R.id.btnRepressao)
        reimpressao.setOnClickListener {

        }
    }

    private val resultadoOperacacao = Runnable {
        val resultado = mSaidaTransacao?.obtemResultadoTransacao() ?: -999999
        traduzResultadoOperacao(resultado)
        mensagem = null
    }
    private fun traduzResultadoOperacao(resultado: Int) {
        var confirmaOperacaoManual = false
        var existeTransacaoPendente = false
        val caixaMensagem = AlertDialog.Builder(this)
        val requerConfirmacao = ""
        if (resultado == 0) {
            if (mSaidaTransacao!!.obtemInformacaoConfirmacao()) {
                if (false) { // cb_manual.isChecked()
                    confirmaOperacaoManual = true
                } else {
                    mConfirmacao.informaStatusTransacao(StatusTransacao.CONFIRMADO_AUTOMATICO)
                    mTransacoes!!.confirmaTransacao(mConfirmacao)
                    caixaMensagem.setPositiveButton("OK", null)
                }
            } else if (mSaidaTransacao!!.existeTransacaoPendente()) {
                mConfirmacao = Confirmacoes()
                caixaMensagem.setTitle("Transações Pendentes")
                caixaMensagem.setPositiveButton("Confirme"
                ) { dialog: DialogInterface?, which: Int ->
                    mConfirmacao.informaStatusTransacao(StatusTransacao.CONFIRMADO_AUTOMATICO)
                }
                caixaMensagem.setPositiveButton("Cancelar") { dialog: DialogInterface?, which: Int ->
                    mConfirmacao.informaStatusTransacao(StatusTransacao.DESFEITO_ERRO_IMPRESSAO_AUTOMATICO)
                    mTransacoes!!.resolvePendencia(mSaidaTransacao!!.obtemDadosTransacaoPendente()!!,
                        mConfirmacao)
                }
            } else {
             caixaMensagem.setPositiveButton("OK", null)
            }
        } else if (mSaidaTransacao!!.existeTransacaoPendente()) {
            mConfirmacao = Confirmacoes()
            existeTransacaoPendente = true
        } else {
            caixaMensagem.setTitle("Erro")
            caixaMensagem.setPositiveButton("OK", null)
        }
        val mensagemRetorno =
            if (mSaidaTransacao != null) mSaidaTransacao!!.obtemMensagemResultado() else ""
        if (mensagemRetorno.length > 1) {
            caixaMensagem.setTitle(mensagemRetorno + requerConfirmacao)
            val builder = StringBuilder()
            builder.append("""
    
    ID do Cartão: ${mSaidaTransacao!!.obtemAidCartao()}
    """.trimIndent())
            builder.append("""
    
    
    Nome Portador Cartão: ${mSaidaTransacao!!.obtemNomePortadorCartao()}
    """.trimIndent())
            builder.append("""
    
    Nome Cartão Padrão: ${mSaidaTransacao!!.obtemNomeCartaoPadrao()}
    """.trimIndent())
            builder.append("""
    
    Nome Estabelecimento: ${mSaidaTransacao!!.obtemNomeEstabelecimento()}
    """.trimIndent())
            builder.append("""
    
    
    Pan Mascarado Cartão: ${mSaidaTransacao!!.obtemPanMascaradoPadrao()}
    """.trimIndent())
            builder.append("""
    
    Pan Mascarado: ${mSaidaTransacao!!.obtemPanMascarado()}
    """.trimIndent())
            builder.append("""
    
    
    Identificador Transação: ${mSaidaTransacao!!.obtemIdentificadorConfirmacaoTransacao()}
    """.trimIndent())
            builder.append("""
    
    
    NSU Original: ${mSaidaTransacao!!.obtemNsuLocalOriginal()}
    """.trimIndent())
            builder.append("""
    
    NSU Local: ${mSaidaTransacao!!.obtemNsuLocal()}
    """.trimIndent())
            builder.append("""
    
    NSU Transação: ${mSaidaTransacao!!.obtemNsuHost()}
    """.trimIndent())
            builder.append("""
    
    
    Nome Cartão: ${mSaidaTransacao!!.obtemNomeCartao()}
    """.trimIndent())
            builder.append("""
    
    Nome Provedor: ${mSaidaTransacao!!.obtemNomeProvedor()}
    """.trimIndent())
            builder.append("""
    
    
    Modo Verificação Senha: ${mSaidaTransacao!!.obtemModoVerificacaoSenha()}
    """.trimIndent())
            builder.append("""
    
    
    Cod Autorização: ${mSaidaTransacao!!.obtemCodigoAutorizacao()}
    """.trimIndent())
            builder.append("""
    
    Cod Autorização Original: ${mSaidaTransacao!!.obtemCodigoAutorizacaoOriginal()}
    """.trimIndent())
            builder.append("""
    
    Ponto Captura: ${mSaidaTransacao!!.obtemIdentificadorPontoCaptura()}
    """.trimIndent())
            builder.append("""
    
    
    Valor da Operação: ${mSaidaTransacao!!.obtemValorTotal()}
    """.trimIndent())
            builder.append("""
    
    Salvo Voucher: ${mSaidaTransacao!!.obtemSaldoVoucher()}
    """.trimIndent())
          if (resultado == 0) {
                caixaMensagem.setMessage(builder.toString())
                val valor = findViewById<EditText>(R.id.txtvalor)
                valorOperacao = valor.toString()
                nsu = mSaidaTransacao!!.obtemNsuHost()
                codigoAutorizacao = mSaidaTransacao!!.obtemCodigoAutorizacao()
                dataOperacao = dateFormat.format(mSaidaTransacao!!.obtemDataHoraTransacao())
            }
        } else if (mensagem == null) {
            caixaMensagem.setMessage(if (resultado == 0) "Operação OK" else "Erro: $resultado")
        } else {
            caixaMensagem.setMessage(mensagem)
        }
        val alert = caixaMensagem.create()
        alert.setCancelable(true)
        alert.setCanceledOnTouchOutside(true)
        if (resultado == 0) {
            if (confirmaOperacaoManual) {
                confirmaOperacao(alert)
            } else {
                trataComprovante()
                alert.show()
            }
        } else {
            if (existeTransacaoPendente) {
                existeTransacaoPendente(alert)
            } else {
                alert.show()
            }
        }
    }
    private fun efetuaOperacao(operacoes: Operacoes) {
        val identificacaoAutomacao = Random().nextInt(99999)
        val valor = findViewById<EditText>(R.id.txtvalor)
        Log.d("Valor", valor.toString())
        iniPayGoInterface(false)
        mEntradaTransacao = EntradaTransacao(operacoes, identificacaoAutomacao.toString())
        if (operacoes == Operacoes.VENDA) {
            mEntradaTransacao!!.informaDocumentoFiscal(identificacaoAutomacao.toString())
            mEntradaTransacao!!.informaValorTotal("1000")
        }
        if (operacoes == Operacoes.CANCELAMENTO) {
            mEntradaTransacao!!.informaNsuTransacaoOriginal(nsu)
            mEntradaTransacao!!.informaCodigoAutorizacaoOriginal(codigoAutorizacao)
            try {
                mEntradaTransacao!!.informaDataHoraTransacaoOriginal(dateFormat.parse(dataOperacao))
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            //Informa novamente o valor para realizar a operação de cancelamento
            mEntradaTransacao!!.informaValorTotal(valorOperacao)
        }
        val pagamento = findViewById<TextView>(R.id.spTipoPagamento)
        val parcelas = findViewById<EditText>(R.id.edt_parcelas)
        if (pagamento.toString() === "Não Definido") {
            mEntradaTransacao!!.informaModalidadePagamento(ModalidadesPagamento.PAGAMENTO_CARTAO)
            mEntradaTransacao!!.informaTipoCartao(Cartoes.CARTAO_DESCONHECIDO)
        } else if (pagamento.toString() === "Crédito") {
            mEntradaTransacao!!.informaModalidadePagamento(ModalidadesPagamento.PAGAMENTO_CARTAO)
            mEntradaTransacao!!.informaTipoCartao(Cartoes.CARTAO_CREDITO)
        } else if (pagamento.toString() === "Débito") {
            mEntradaTransacao!!.informaModalidadePagamento(ModalidadesPagamento.PAGAMENTO_CARTAO)
            mEntradaTransacao!!.informaTipoCartao(Cartoes.CARTAO_DEBITO)
        } else if (pagamento.toString() === "Carteira Digital") {
            mEntradaTransacao!!.informaModalidadePagamento(ModalidadesPagamento.PAGAMENTO_CARTEIRA_VIRTUAL)
            mEntradaTransacao!!.informaTipoCartao(Cartoes.CARTAO_VOUCHER)
        }
        if (false) {
            mEntradaTransacao!!.informaTipoFinanciamento(Financiamentos.FINANCIAMENTO_NAO_DEFINIDO)
        } else if (true) {
            mEntradaTransacao!!.informaTipoFinanciamento(Financiamentos.A_VISTA)
        } else if (false) {
            mEntradaTransacao!!.informaTipoFinanciamento(Financiamentos.PARCELADO_EMISSOR)
            mEntradaTransacao!!.informaNumeroParcelas(parcelas.getText().toString().toInt())
        } else if (false) {
            mEntradaTransacao!!.informaTipoFinanciamento(Financiamentos.PARCELADO_ESTABELECIMENTO)
            mEntradaTransacao!!.informaNumeroParcelas(parcelas.getText().toString().toInt())
        }
        if (false) {
            // mEntradaTransacao!!.informaNomeProvedor(adquirente.toString())
        }
        mEntradaTransacao!!.informaCodigoMoeda("986") // Real
        mConfirmacao = Confirmacoes()
        Thread(label@ Runnable {
            try {
                mDadosAutomacao!!.obtemPersonalizacaoCliente()
                mSaidaTransacao = mTransacoes!!.realizaTransacao(mEntradaTransacao!!)
                if (mSaidaTransacao == null) return@Runnable
                mConfirmacao
                    .informaIdentificadorConfirmacaoTransacao(
                        mSaidaTransacao!!.obtemIdentificadorConfirmacaoTransacao()
                    )
            } catch (e: QuedaConexaoTerminalExcecao) {
                e.printStackTrace()
                mensagem = "Queda de Conexão"
            } catch (terminalNaoConfiguradoExcecao: TerminalNaoConfiguradoExcecao) {
                terminalNaoConfiguradoExcecao.printStackTrace()
                mensagem = "Cliente não configurado!"
            } catch (aplicacaoNaoInstaladaExcecao: AplicacaoNaoInstaladaExcecao) {
                aplicacaoNaoInstaladaExcecao.printStackTrace()
                mensagem = "Aplicação não instalada!"
            } finally {
                // Trata o Fim da Operação
                mEntradaTransacao = null
                mHandler.post(resultadoOperacacao)
            }
        }).start()
    }
    private fun iniPayGoInterface(mudaCor: Boolean) {
        val versaoAutomacao: String?
        versaoAutomacao = try {
            this.packageManager.getPackageInfo(
                this.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            "Indisponivel"
        }
        mPersonalizacao = setPersonalizacao(mudaCor)
        mDadosAutomacao = DadosAutomacao("TecToy Automação", "Automação", versaoAutomacao!!,
            true, true, false, true, mPersonalizacao)
        mTransacoes = Transacoes.obtemInstancia(mDadosAutomacao, this)
        versoes = mTransacoes!!.obtemVersoes().toString()
    }

    private fun trataComprovante() {
        var comprovante: List<String?>? = ArrayList()
        if (false) {
            val vias = mSaidaTransacao!!.obtemViasImprimir()
            if (vias == ViasImpressao.VIA_CLIENTE || vias == ViasImpressao.VIA_CLIENTE_E_ESTABELECIMENTO) {
                comprovante = mSaidaTransacao!!.obtemComprovanteDiferenciadoPortador()
                if (comprovante == null || comprovante.size <= 1) {
                    // Verifica se tem via completa
                    comprovante = mSaidaTransacao!!.obtemComprovanteCompleto()
                    if (comprovante == null) {
                        return
                    }
                }
                printComprovante("Via do Cliente", comprovante)
            }
            if (vias == ViasImpressao.VIA_ESTABELECIMENTO || vias == ViasImpressao.VIA_CLIENTE_E_ESTABELECIMENTO) {
                comprovante = mSaidaTransacao!!.obtemComprovanteDiferenciadoLoja()
                if (comprovante == null || comprovante.size <= 1) {
                    // Verifica se tem via completa
                    comprovante = mSaidaTransacao!!.obtemComprovanteCompleto()
                    if (comprovante == null) {
                        return
                    }
                }
                printComprovante("Via do Estabelecimento", comprovante)
            }
        } else {
            comprovante = mSaidaTransacao!!.obtemComprovanteCompleto()
            if (comprovante == null || comprovante.size <= 1) {
                return
            }
            printComprovante("Comprovante Full", comprovante)
        }
    }
    private fun printComprovante(mensagem: String, comprovante: List<String>) {
        val caixaMensagem = AlertDialog.Builder(this)
        var cupom = ""
        for (linha in comprovante) {
            cupom += linha
        }
        caixaMensagem.setTitle("Impressão de comprovante")
        caixaMensagem.setMessage("Deseja imprimir $mensagem")
        val finalCupom = cupom
        caixaMensagem.setPositiveButton("Sim"
        ) { dialog: DialogInterface?, which: Int ->
            try {
                //printer.getStatusImpressora();
                //if (printer.isImpressoraOK()) {
                if (true) {
                    TectoySunmiPrint.getInstance().setAlign(TectoySunmiPrint.Alignment_CENTER)
                    TectoySunmiPrint.getInstance().printStyleBold(true)
                    TectoySunmiPrint.getInstance().printText(finalCupom)
                    TectoySunmiPrint.getInstance().print3Line()
                    TectoySunmiPrint.getInstance().cutpaper()
                    //printer.imprimeTexto(finalCupom);
                    //printer.avancaLinha(150);
                    //printer.ImpressoraOutput();
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        caixaMensagem.setNegativeButton("Não", null)
        val alert = caixaMensagem.create()
        alert.setCancelable(false)
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }
    private fun setPersonalizacao(isInverse: Boolean): Personalizacao? {
        val pb = Personalizacao.Builder()
        try {
            if (isInverse) {
                pb.informaCorFonte("#000000")
                pb.informaCorFonteTeclado("#000000")
                pb.informaCorFundoCaixaEdicao("#FFFFFF")
                pb.informaCorFundoTela("#F4F4F4")
                pb.informaCorFundoTeclado("#F4F4F4")
                pb.informaCorFundoToolbar("#FF8C00")
                pb.informaCorTextoCaixaEdicao("#000000")
                pb.informaCorTeclaPressionadaTeclado("#e1e1e1")
                pb.informaCorTeclaLiberadaTeclado("#dedede")
                pb.informaCorSeparadorMenu("#FF8C00")
            }
        } catch (e: IllegalArgumentException) {
            Toast.makeText(this, "Verifique valores de\nconfiguração", Toast.LENGTH_SHORT).show()
        }
        return pb.build()
    }
    private fun existeTransacaoPendente(dialog2: AlertDialog) {
        val confirmaOperacao = AlertDialog.Builder(this)
        confirmaOperacao.setTitle("Transação Pendente")
        confirmaOperacao.setMessage("Deseja confirmar a transação que esta PENDENTE?")
     confirmaOperacao.setPositiveButton("Confirme") { dialog: DialogInterface?, which: Int ->
            mConfirmacao.informaStatusTransacao(StatusTransacao.CONFIRMADO_MANUAL)
            mTransacoes!!.resolvePendencia(mSaidaTransacao!!.obtemDadosTransacaoPendente()!!,
                mConfirmacao)
            trataComprovante()
            dialog2.show()
        }
        confirmaOperacao.setNegativeButton("Cancelar") { dialog: DialogInterface?, which: Int ->
            mConfirmacao.informaStatusTransacao(StatusTransacao.DESFEITO_ERRO_IMPRESSAO_AUTOMATICO)
            mTransacoes!!.confirmaTransacao(mConfirmacao)
            trataComprovante()
            dialog2.show()
        }
        confirmaOperacao.setNegativeButton("Não", null)
        val alert = confirmaOperacao.create()
        alert.setCancelable(false)
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }
    private fun confirmaOperacao(dialog2: AlertDialog) {
        val confirmaOperacao = AlertDialog.Builder(this)
        confirmaOperacao.setTitle("Confirmação manual")
        confirmaOperacao.setMessage("Deseja confirmar a operação de forma manual?")
        confirmaOperacao.setPositiveButton("Confirme") { dialog: DialogInterface?, which: Int ->
            mConfirmacao.informaStatusTransacao(StatusTransacao.CONFIRMADO_MANUAL)
            mTransacoes!!.confirmaTransacao(mConfirmacao)
            trataComprovante()
            dialog2.show()
        }
        confirmaOperacao.setNegativeButton("Cancelar") { dialog: DialogInterface?, which: Int ->
            mConfirmacao.informaStatusTransacao(StatusTransacao.DESFEITO_MANUAL)
            mTransacoes!!.confirmaTransacao(mConfirmacao)
            trataComprovante()
            dialog2.show()
        }
        confirmaOperacao.setNegativeButton("Não", null)
        val alert = confirmaOperacao.create()
        alert.setCancelable(false)
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }
}