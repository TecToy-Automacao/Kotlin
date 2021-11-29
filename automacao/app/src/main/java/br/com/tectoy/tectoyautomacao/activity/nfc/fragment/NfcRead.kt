package br.com.tectoy.tectoyautomacao.activity.nfc.fragment

import android.content.Context
import android.nfc.FormatException
import android.nfc.tech.Ndef
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import br.com.example.tectoy.tectoyautomacao.R
import br.com.tectoy.tectoyautomacao.activity.nfc.NfcExemplo
import br.com.tectoy.tectoyautomacao.activity.nfc.NfcLeituraGravacao
import org.jetbrains.annotations.Nullable
import java.io.IOException
import java.lang.Exception

class NfcRead : DialogFragment() {
    private var nfcLeituraGravacao: NfcLeituraGravacao? = null

    val TAG: String = NfcRead::class.java.getSimpleName()

    fun newInstance(): NfcRead? {
        return NfcRead()
    }

    private var mTvMessage: TextView? = null
    private var mListener: NfcExemplo? = null


    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_leitura, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        mTvMessage = view.findViewById(R.id.tv_message)
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

    fun onNfcDetected(ndef: Ndef) {
        nfcLeituraGravacao = NfcLeituraGravacao(ndef.tag)
        readFromNFC(ndef)
    }

    /**
     * Este método irá apresentar na tela as atuais mensagens cadastadas no cartão
     *
     * @param ndef = contém as informações do cartão que acabou de ser lido.
     *
     * @exception IOException
     * @exception FormatException
     * @exception Exception
     *
     */
    private fun readFromNFC(ndef: Ndef) {
        val mensagem: String
        val idCarao: String
        val tempoExecucao: Long
        try {

            // Recebe a leitura das atuais mensagens cadastradas no cartão
            mensagem = nfcLeituraGravacao?.retornaMensagemGravadaCartao(ndef).toString()
            idCarao = nfcLeituraGravacao?.idCartaoHexadecimal().toString()

            // Recebe o tempo total de execução da operação de leitura
            tempoExecucao = nfcLeituraGravacao?.retornaTempoDeExeculcaoSegundos()!!
            if (mensagem == "") {
                mTvMessage!!.text = "Não existe mensagem gravada no cartão"
            } else {
                mTvMessage!!.text = """
                ID Cartão: $idCarao
                $mensagem
                
                Tempo de execução: ${String.format("%02d segundos", tempoExecucao)}
                """.trimIndent()
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