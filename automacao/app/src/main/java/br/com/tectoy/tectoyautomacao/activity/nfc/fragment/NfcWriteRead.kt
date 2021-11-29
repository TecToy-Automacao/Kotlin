package br.com.tectoy.tectoyautomacao.activity.nfc.fragment

import android.annotation.TargetApi
import android.content.Context
import android.nfc.FormatException
import android.nfc.tech.Ndef
import android.os.Build
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import br.com.example.tectoy.tectoyautomacao.R
import br.com.tectoy.tectoyautomacao.activity.nfc.NfcExemplo
import br.com.tectoy.tectoyautomacao.activity.nfc.NfcLeituraGravacao
import org.jetbrains.annotations.Nullable
import java.io.IOException
import java.lang.Exception

class NfcWriteRead : DialogFragment(){

    private var nfcLeituraGravacao: NfcLeituraGravacao? = null

    val TAG: String =
        NfcWriteRead::class.java.getSimpleName()

    fun newInstance(): NfcWriteRead? {
        return NfcWriteRead()
    }

    private var tvStatus: TextView? = null

    private var editProcesso: EditText? = null
    private var mProgress: ProgressBar? = null
    private var mListener: NfcExemplo? = null

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_forceteste, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        editProcesso = view.findViewById<View>(R.id.editProcesso) as EditText
        mProgress = view.findViewById<View>(R.id.progress) as ProgressBar
        tvStatus = view.findViewById<View>(R.id.tv_status) as TextView
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener = context as NfcExemplo?
        mListener?.onDialogDisplayed()
    }

    override fun onDetach() {
        super.onDetach()
        mListener?.onDialogDismissed()
    }

    fun onNfcDetected(ndef: Ndef, mensagem: String) {
        nfcLeituraGravacao = NfcLeituraGravacao(ndef.tag)
        if (writeToNfc(ndef, mensagem)) {
            readFromNFC(ndef)
        }
    }

    /**
     * Este método irá grava uma nova mensagem no cartão.
     *
     * @param ndef = contém as informações do cartão que acabou de ser lido.
     * @param message  = mensagem que será gravada no cartão
     *
     * @exception IOException
     * @exception FormatException
     *
     * @return boolean = Sinalize que a mensagem foi gravada
     *
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun writeToNfc(ndef: Ndef, message: String): Boolean {
        var retorno = false
        try {
            retorno = (nfcLeituraGravacao?.gravaMensagemCartao(ndef, message) ?: if (retorno) {
                editProcesso!!.setText("""
            Código ID:${
                    nfcLeituraGravacao?.idCartaoHexadecimal().toString()
                }
            Código gravado: $message
            
            """.trimIndent())
            } else {
                editProcesso!!.setText("Falha ao gravar mensagem")
            }) as Boolean
        } catch (e: FormatException) {
            Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
        } finally {
            mProgress!!.visibility = View.GONE
        }
        return retorno
    }

    /**
     * Este método irá grava uma nova mensagem no cartão.
     *
     * @param ndef = contém as informações do cartão que acabou de ser lido.
     *
     * @exception IOException
     * @exception FormatException
     *
     *
     */
    private fun readFromNFC(ndef: Ndef) {
        val editTex: String
        val mensagem: String
        val tempoExecucao: Long
        try {
            mensagem = nfcLeituraGravacao?.retornaMensagemGravadaCartao(ndef).toString()
            tempoExecucao = nfcLeituraGravacao?.retornaTempoDeExeculcaoSegundos()!!
            if (mensagem == "") {
                tvStatus!!.text = "Nenhuma mensagem cadastrada."
            } else {
                tvStatus!!.text = "Aproxime o cartão."
                editTex = editProcesso!!.text.toString()
                editProcesso!!.setText("""
    $editTex
    Código ID:${nfcLeituraGravacao?.idCartaoHexadecimal()}
    Leitura código: $mensagem
    ${String.format("\nTempo de execução: %02d segundos", tempoExecucao)}
    """.trimIndent())
            }
        } catch (e: IOException) {
            Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
        } catch (e: FormatException) {
            Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
        }
    }


}