package br.com.tectoy.tectoyautomacao.activity.nfc

import android.app.PendingIntent
import android.app.ProgressDialog.show
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.support.constraint.ConstraintLayoutStates.TAG
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import br.com.example.tectoy.tectoyautomacao.R
import br.com.tectoy.tectoyautomacao.activity.nfc.fragment.NfcFormat
import br.com.tectoy.tectoyautomacao.activity.nfc.fragment.NfcRead
import br.com.tectoy.tectoyautomacao.activity.nfc.fragment.NfcWrite
import br.com.tectoy.tectoyautomacao.activity.nfc.fragment.NfcWriteRead
import javax.xml.datatype.DatatypeFactory.newInstance

class NfcExemplo : AppCompatActivity() {
    private var editMesagemPadrao: TextView? = null
    private var btn_ler: Button? = null
    private var btn_gravar: Button? = null
    private var btn_forceteste: Button? = null
    private var btn_formatarCartao: Button? = null

    private var isDialogDisplayed = false
    private var isWrite = false
    private var isRead = false
    private var isFormat = false
    private var isForceTeste = false

    private var mNfcWriteFragment: NfcWrite? = null
    private var mNfcReadFragment: NfcRead? = null
    private var nfcWriteReadFragment: NfcWriteRead? = null
    private var nfcFormatFragment: NfcFormat? = null

    private var mNfcAdapter: NfcAdapter? = null

    private val MENSAGEM_PADRAO = "TecToy"
    private var processo = 1000
 //   private val getID: ByteArray =

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nfcexemplo)
        editMesagemPadrao = findViewById(R.id.editMensagemPadrao) as TextView
        btn_ler = findViewById(R.id.btn_leitura) as Button
        btn_gravar = findViewById(R.id.btn_gravar) as Button
        btn_forceteste = findViewById(R.id.btn_teste) as Button
        btn_formatarCartao = findViewById(R.id.btn_formatarCartao) as Button
        initViews()
        initNFC()
    }

    private fun initViews() {
        btn_gravar!!.setOnClickListener { showWriteFragment() }
        btn_ler!!.setOnClickListener { showReadFragment() }
        btn_forceteste!!.setOnClickListener { showReadWriteFragment() }
        btn_formatarCartao!!.setOnClickListener { showFormatFragment() }
    }

    private fun initNFC() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
    }


    private fun showWriteFragment() {
        isWrite = true
        isRead = false
        isForceTeste = false
        isFormat = false
        mNfcWriteFragment =
            fragmentManager.findFragmentByTag(NfcWrite().TAG) as NfcWrite
        if (mNfcWriteFragment == null) {
            mNfcWriteFragment = NfcWrite().newInstance()
        }
        //mNfcWriteFragment.show(fragmentManager, NfcWrite().TAG)
    }

    private fun showReadFragment() {
        isRead = true
        isWrite = false
        isForceTeste = false
        isFormat = false
        mNfcReadFragment = fragmentManager.findFragmentByTag(NfcRead().TAG) as NfcRead
        if (mNfcReadFragment == null) {
            mNfcReadFragment = NfcRead().newInstance()
        }
        //mNfcReadFragment.show(fragmentManager, NfcRead().TAG)
    }



    private fun showFormatFragment() {
        isFormat = true
        isForceTeste = false
        isRead = false
        isWrite = false
        nfcFormatFragment =
            fragmentManager.findFragmentByTag(NfcFormat().TAG) as NfcFormat
        if (nfcFormatFragment == null) {
            nfcFormatFragment = NfcFormat().newInstance()
        }
        //nfcFormatFragment.show(fragmentManager, NfcFormat().TAG)
    }

    private fun showReadWriteFragment() {
        isForceTeste = true
        isRead = false
        isWrite = false
        isFormat = false
        processo = 1000
        nfcWriteReadFragment =
            fragmentManager.findFragmentByTag(NfcWriteRead().TAG) as NfcWriteRead
        if (nfcWriteReadFragment == null) {
            nfcWriteReadFragment = NfcWriteRead().newInstance()
        }
       // nfcWriteReadFragment.show(fragmentManager, NfcWriteRead().TAG)
    }

    fun onDialogDisplayed() {
        isDialogDisplayed = true
    }

    fun onDialogDismissed() {
        isDialogDisplayed = false
    }

    override fun onResume() {
        super.onResume()
        val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        val ndefDetected = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        val techDetected = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        val idDetected = IntentFilter(NfcAdapter.EXTRA_ID)
        val isMira = IntentFilter(NfcAdapter.EXTRA_TAG)
        val nfcIntentFilter = arrayOf(techDetected, tagDetected, ndefDetected, idDetected, isMira)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        if (mNfcAdapter != null) mNfcAdapter!!.enableForegroundDispatch(this,
            pendingIntent,
            nfcIntentFilter,
            null)
    }

    override fun onPause() {
        super.onPause()
        if (mNfcAdapter != null) mNfcAdapter!!.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        Log.d("TAG", "onNewIntent: " + intent.action)
        if (tag != null) {
            val ndef = Ndef.get(tag)
            if (isDialogDisplayed) {
                if (ndef == null) {
                    Toast.makeText(applicationContext,
                        "Tipo de cartão não suportado.",
                        Toast.LENGTH_SHORT).show()
                } else if (isWrite) {
                    val messageToWrite = editMesagemPadrao!!.text.toString()
                    if (messageToWrite == "") {
                        Toast.makeText(applicationContext,
                            "Preencha uma mensagem",
                            Toast.LENGTH_SHORT).show()
                    } else {
                        mNfcWriteFragment =
                            fragmentManager.findFragmentByTag(NfcWrite().TAG) as NfcWrite
                        mNfcWriteFragment!!.onNfcDetected(ndef, messageToWrite)
                    }
                } else if (isRead) {
                    mNfcReadFragment =
                        fragmentManager.findFragmentByTag(NfcRead().TAG) as NfcRead
                    mNfcReadFragment!!.onNfcDetected(ndef)
                } else if (isForceTeste) {
                    nfcWriteReadFragment =
                        fragmentManager.findFragmentByTag(NfcWriteRead().TAG) as NfcWriteRead
                    nfcWriteReadFragment!!.onNfcDetected(ndef, MENSAGEM_PADRAO + processo.toString())
                    processo--
                } else if (isFormat) {
                    nfcFormatFragment =
                        fragmentManager.findFragmentByTag(NfcFormat().TAG) as NfcFormat
                    nfcFormatFragment!!.onNfcDetected(ndef)
                }
            }
        }
    }
}



