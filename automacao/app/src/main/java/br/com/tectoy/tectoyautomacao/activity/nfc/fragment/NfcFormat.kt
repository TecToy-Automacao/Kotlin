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
import br.com.example.tectoy.tectoyautomacao.R
import br.com.tectoy.tectoyautomacao.activity.nfc.NfcExemplo
import br.com.tectoy.tectoyautomacao.activity.nfc.NfcLeituraGravacao
import org.jetbrains.annotations.Nullable
import java.io.IOException

class NfcFormat : DialogFragment(){
    private var nfcLeituraGravacao: NfcLeituraGravacao? = null

    val TAG: String = NfcFormat::class.toString()

    private var view = null
    private var texMensagem: TextView? = null
    private var mListener: NfcExemplo? = null

    fun newInstance(): NfcFormat? {
        return NfcFormat()
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        view = inflater.inflate(R.layout.fragment_formata, container, false) as Nothing?
        initViews(view)
        return view
    }

    private fun initViews(view: View?) {
        texMensagem = view!!.findViewById(R.id.tv_message)
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
        formatFromNFC(ndef)
    }

    /**
     * Este método irá tentar fazer a formatação do atual cartão que esta
     * sendo lido pela leitora.
     *
     * @param ndef = contém as informações do cartão que acabou de ser lido.
     *
     * @exception IOException
     * @exception FormatException
     *
     */
    private fun formatFromNFC(ndef: Ndef) {
        var retorno: Boolean
        retorno = true;
        try {
            retorno = (nfcLeituraGravacao?.formataCartao(ndef) ?: if (retorno) {
                texMensagem!!.text = "Cartão formatado"
            } else {
                texMensagem!!.text = "Não é necessário formatar este cartão."
            }) as Boolean
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: FormatException) {
            e.printStackTrace()
        }
    }
}