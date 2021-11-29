package br.com.tectoy.tectoyautomacao.activity.nfc.fragment

import android.content.Context
import android.nfc.FormatException
import android.nfc.tech.Ndef
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import br.com.example.tectoy.tectoyautomacao.R
import br.com.tectoy.tectoyautomacao.activity.nfc.NfcExemplo
import br.com.tectoy.tectoyautomacao.activity.nfc.NfcLeituraGravacao
import org.jetbrains.annotations.Nullable
import java.io.IOException

public class NfcWrite : DialogFragment() {
    private var nfcLeituraGravacao: NfcLeituraGravacao? = null

    public val TAG: String =
        NfcWrite::class.java.getSimpleName()


    fun newInstance(): NfcWrite? {
        return NfcWrite()
    }

    private var mTvMessage: TextView? = null
    private var mProgress: ProgressBar? = null
    private var mListener: NfcExemplo? = null

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_gravacao, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        mTvMessage = view.findViewById<View>(R.id.tv_message) as TextView
        mProgress = view.findViewById<View>(R.id.progress) as ProgressBar
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

    fun onNfcDetected(ndef: Ndef, messageToWrite: String) {
        nfcLeituraGravacao = NfcLeituraGravacao(ndef.tag)
        mProgress!!.visibility = View.VISIBLE
        writeToNfc(ndef, messageToWrite)
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
     */
    private fun writeToNfc(ndef: Ndef?, message: String) {
        val tempoExecucao: Long
        mTvMessage!!.text = getString(R.string.message_write_progress)
        if (ndef != null) {
            try {
                nfcLeituraGravacao?.gravaMensagemCartao(ndef, message)
                tempoExecucao = nfcLeituraGravacao?.retornaTempoDeExeculcaoSegundos()!!
                mTvMessage!!.text = """
                ${getString(R.string.message_write_success)}
                
                Tempo de execução: ${String.format("%02d segundos", tempoExecucao)}
                """.trimIndent()
            } catch (e: IOException) {
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                e.printStackTrace()
                mTvMessage!!.text = getString(R.string.message_write_error)
            } catch (e: FormatException) {
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                e.printStackTrace()
            } finally {
                mProgress!!.visibility = View.GONE
            }
        } else {
            Toast.makeText(activity, "Não foi possível ler este cartão", Toast.LENGTH_LONG).show()
        }
    }

}