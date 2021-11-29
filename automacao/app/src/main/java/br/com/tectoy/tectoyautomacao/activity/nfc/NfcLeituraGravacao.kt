package br.com.tectoy.tectoyautomacao.activity.nfc

import android.annotation.SuppressLint
import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import java.io.IOException
import java.lang.Exception
import java.nio.charset.Charset
import java.util.*
import kotlin.experimental.and

class NfcLeituraGravacao(tag: Tag) {
    var mifareClassic: MifareClassic? = null

    // Mensagem padrão para ser usada quando o o cartão for formatado
    private val MENSAGEM_PADRAO = "TecToy"

    // Tag para ser usado no LOG
    private val TAG = NfcLeituraGravacao::class.java.simpleName

    private var tempInicial: Long = 0
    private var tempFinal: Long = 0
    private var direfenca: Long = 0

    /**
     * Método construtor da classe.
     *
     * @param tag = contém as tag do cartão que foi lido.
     *
     */
    fun NfcLeituraGravacao(tag: Tag?) {
        mifareClassic = MifareClassic.get(tag)
        gravaTempoInicia()
    }

    /**
     *
     * Método que faz a leitura de todos os setores e blocos
     * existentes no cartão que esta sendo lido.
     *
     * Esse método nunca deve ser usado na Thread principal do Android,
     * sempre que foi necessario ler todas essas informações.
     *
     * Crie uma Thread auxíliar para fazer a leitura e obter o seu retorno.
     *
     * @throws IOException
     *
     * @return List<Object>
     *
    </Object> */
    @Throws(Exception::class)
    fun lerTodosOsSetoresDoCartao(): List<Any>? {
        var byteRead: ByteArray // Irá armazenar os byte lidos do cartão
        var auth = false // Valida se existe ou não permissão para ler o bloco
        var quantSetores = 0 // Quantidade de setores existentes no cartão
        var blocoCount = 0 // Quantidades de blocos existentes no cartão
        var blocoIndex = 0 // Irá armazenar o indice que esta sendo lido do cartão
        val ByteRetorno: MutableList<Any> =
            ArrayList() // Lista com a leitura de todos os blocos e setores
        try {

            // Faz a conexão com o cartão
            validConexaoCartao()

            // Irá armazenar a quantidade de setores existentes no cartão
            quantSetores = mifareClassic!!.sectorCount

            // Percorre todos os setores existentes no cartão
            for (i in 0 until quantSetores) {

                // Faz a validação de permissão para a leitura do bloco A
                auth = validPermissaoBlocoA(i)
                if (!auth) {
                    // Faz a validação de permissão para a leitura do bloco B
                    auth = validPermissaoBlocoB(i)
                }
                if (auth) {

                    // Busca a quanidade de blocos em um setor
                    blocoCount = mifareClassic!!.getBlockCountInSector(i)

                    // Percore todos os blocos do setor
                    for (j in 0 until blocoCount) {
                        // É necessário fazer novamente a validação de permissão Bloco a bloco

                        // Faz a validação de permissão para a leitura do bloco A
                        auth = validPermissaoBlocoA(j)
                        if (!auth) {
                            // Faz a validação de permissão para a leitura do bloco B
                            auth = validPermissaoBlocoB(j)
                        }
                        if (auth) {

                            // seta o indice do setor dentro do bloco
                            blocoIndex = mifareClassic!!.sectorToBlock(j)

                            // Faz a leitura de um BLOCO no setor
                            byteRead = mifareClassic!!.readBlock(blocoIndex)
                            ByteRetorno.add(byteRead)
                        }
                    }
                }
            }
        } catch (e: IOException) {
            throw IOException(e)
        } finally {
            // Fecha a conexão com o cartão
            desconectaCartao()
            gravaTempoFinal()
        }
        return ByteRetorno
    }

    /**
     *
     * Método que faz a leitura de um bloco e setor específico
     * no cartão que esta sendo lido.
     *
     * A leitura sempre será retornada em Bytes.
     *
     * @param bloco = número que bloco que deve ser lido
     * @param setor = número do setor existente dentro do bloco que será lido
     *
     * @throws IOException
     *
     * @return byte[] = a
     *
     */
    @Throws(Exception::class)
    fun lerUmSetoresDoCartao(bloco: Int, setor: Int): ByteArray? {
        var byteRead: ByteArray? = null // Irá armazenar os byte lidos do cartão
        var auth = false // Valida se existe ou não permissão para ler o bloco
        var blocoIndex = 0 // Irá armazenar o indice que esta sendo lido do cartão
        try {

            // Faz a conexão com o cartão
            mifareClassic!!.connect()


            // Faz a validação de permissão para a leitura do bloco A
            auth = validPermissaoBlocoA(bloco)
            if (!auth) {
                // Faz a validação de permissão para a leitura do bloco B
                auth = validPermissaoBlocoB(bloco)
                if (!auth) {
                    throw Exception("Falha na validação de senha.")
                }
            }
            if (auth) {

                // É necessário fazer novamente a validação de permissão Bloco a bloco

                // Faz a validação de permissão para a leitura do bloco A
                auth = validPermissaoBlocoA(setor)
                if (!auth) {
                    // Faz a validação de permissão para a leitura do bloco B
                    auth = validPermissaoBlocoB(setor)
                }
                if (auth) {

                    // seta o indice do setor dentro do bloco
                    blocoIndex = mifareClassic!!.sectorToBlock(setor)

                    // Faz a leitura de um BLOCO no setor
                    byteRead = mifareClassic!!.readBlock(blocoIndex)
                }
            }
        } catch (e: IOException) {
            throw Exception(e)
        } finally {
            desconectaCartao()
            gravaTempoFinal()
        }
        return byteRead
    }

    /**
     *
     * Método faz a gravação de uma mensagem em um bloco específico
     * no cartão.
     *
     * A mensagem que será gravada não deve ser superior a 16 bits.
     *
     * A leitura sempre será retornada em Bytes.
     *
     * @param bloco = número que bloco que deve ser lido
     * @param mensagem = número do setor existente dentro do bloco que será lido
     *
     * @throws IOException
     *
     * @return true = caso haja um erro na gravação, será tratada no throw
     *
     */
    @Throws(Exception::class)
    fun gravaSetorCartao(bloco: Int, mensagem: ByteArray): Boolean {
        try {
            // Valida a conexão com o cartão
            validConexaoCartao()

            // Valida a quantidade de bytes que serão gravados
            require(mensagem.size == 16) { "A mensagem não contem 16 bits" }
            // Grava no cartão
            mifareClassic!!.writeBlock(bloco, mensagem)
        } catch (e: IOException) {
            throw Exception(e)
        } finally {
            desconectaCartao()
            gravaTempoFinal()
        }
        return true
    }

    /**
     *
     * Método que faz o incremento de um valor em um bloco específico
     * no cartão.
     *
     * O valor a ser incrementado será sempre o INTEIRO
     *
     *
     * @param bloco = Número que bloco que deve ser incrementado
     * @param valor = Valor que será incrementado ao já existente no bloco
     *
     * @throws IOException
     *
     * @return true = caso haja um erro na gravação será tratado no throw
     *
     */
    @Throws(Exception::class)
    fun incrementaValorCartao(bloco: Int, valor: Int): Boolean {
        try {
            // Valida a conexão com o cartão
            validConexaoCartao()
            // Valida o valor a ser gravado
            validaValor(valor)
            // Incrementa o valor no cartão
            mifareClassic!!.increment(bloco, valor)
        } catch (e: IOException) {
            throw Exception(e)
        } finally {
            desconectaCartao()
            gravaTempoFinal()
        }
        return true
    }

    /**
     *
     * Método que faz o decremento de um valor em um bloco específico
     * no cartão.
     *
     * O valor a ser decrementado será sempre o INTEIRO
     *
     *
     * @param bloco = Número que bloco que deve ser incrementado
     * @param valor = Valor que será incrementado ao já existente no bloco
     *
     * @throws IOException
     *
     * @return true = caso haja um erro na gravação será tratado no throw
     *
     */
    @Throws(Exception::class)
    fun decrementaValorCartao(bloco: Int, valor: Int): Boolean {
        try {
            // Valida conexão
            validConexaoCartao()
            // Valida o valor
            validaValor(valor)
            // Decrementa o valor do cartão
            mifareClassic!!.decrement(bloco, valor)
        } catch (e: IOException) {
            throw Exception(e)
        } finally {
            desconectaCartao()
            gravaTempoFinal()
        }
        return true
    }

    /**
     *
     * Método faz a gravação de uma nova mensagem no cartão.
     *
     * Essa nova mensagem será códificada usando o padrão UTF-8.
     *
     * @param ndef = Contém as informações do cartão que esta sendo lido.
     * @param mensagem = Mensagem que será gravada no cartão
     *
     * @throws IOException
     * @throws FormatException
     *
     * @return boolean =>  True = Mensagem Gravada / False = Erro ao gravar mensagem
     *
     */
    @SuppressLint("NewApi")
    @Throws(FormatException::class, IOException::class)
    fun gravaMensagemCartao(ndef: Ndef?, mensagem: String): Boolean {
        var retorno = false
        try {
            if (ndef != null) {
                ndef.connect()
                var mimeRecord: NdefRecord? = null
                mimeRecord =
                    NdefRecord.createMime("UTF-8", mensagem.toByteArray(Charset.forName("UTF-8")))
                ndef.writeNdefMessage(NdefMessage(mimeRecord))
                ndef.close()
                retorno = true
            } else {
                retorno = formataCartao(ndef)
            }
        } catch (e: FormatException) {
            throw FormatException(e.message)
        } catch (e: IOException) {
            throw IOException(e)
        } finally {
            gravaTempoFinal()
        }
        return retorno
    }

    /**
     *
     * Método faz a gravação de uma nova mensagem no cartão.
     *
     * Essa nova mensagem será códificada usando o padrão UTF-8.
     *
     * @param ndef = Contém as informações do cartão que esta sendo lido.
     *
     * @throws IOException
     * @throws FormatException
     *
     * @return String = contém a mensagem que esta gravada no cartão
     *
     */
    @Throws(Exception::class)
    fun retornaMensagemGravadaCartao(ndef: Ndef?): String? {
        val message: String
        message = try {
            if (ndef == null) {
                throw Exception("Não foi possível ler o cartão.")
            }
            if (!ndef.isConnected) {
                ndef.connect()
            }
            val ndefMessage = ndef.ndefMessage
            if (ndefMessage == null) {
                throw Exception("Não foi possível ler o cartão.")
            } else {
                String(ndefMessage.records[0].payload)
            }
        } catch (e: IOException) {
            throw Exception(e)
        } catch (e: FormatException) {
            throw Exception(e)
        } catch (e: Exception) {
            throw Exception(e)
        } finally {
            gravaTempoFinal()
        }
        return message
    }

    /**
     *
     * Método faz a formatação do cartão.
     *
     * A formatação do cartão só é necessario na sua primeira gravação.
     *
     * Após já existir algum valor gravado no cartão, não será possível formata-lo
     * novamente.
     *
     * @param ndef = Contém as informações do cartão que esta sendo lido.
     *
     * @throws IOException
     * @throws FormatException
     *
     * @return boolean =>  True = Cartão Formatado / False = Cartão não formatado
     *
     */
    @SuppressLint("NewApi")
    @Throws(IOException::class, FormatException::class)
    fun formataCartao(ndef: Ndef?): Boolean {
        var retorno = false
        val ndefFormatable = NdefFormatable.get(ndef!!.tag)
        retorno = try {
            if (ndefFormatable == null) {
                return retorno
            }
            if (!ndefFormatable.isConnected) {
                ndefFormatable.connect()
            }
            ndefFormatable.format(NdefMessage(NdefRecord.createMime("UTF-8",
                MENSAGEM_PADRAO.toByteArray(
                    Charset.forName("UTF-8")))))
            ndefFormatable.close()
            true
        } catch (e: IOException) {
            throw IOException(e)
        } catch (e: FormatException) {
            throw FormatException(e.message)
        } finally {
            gravaTempoFinal()
        }
        return retorno
    }

    /**
     *
     * Método que válida o valor a ser incrementado ou decrementado do cartão.
     *
     * @param valor = Valor a ser incrementado ou decrementado do cartão
     *
     * @throws IllegalArgumentException
     *
     */
    private fun validaValor(valor: Int) {
        require(valor >= 0) { "O valor não poder ser negativo." }
    }

    /**
     *
     * Método que válida se existe ou não conexão com o atual cartão.
     *
     * Caso não haja conexão aberta com o cartão, esse método irá abrir
     * uma nova conexão.
     *
     */
    private fun validConexaoCartao() {
        if (!mifareClassic!!.isConnected) {
            conectarCartao()
        }
    }

    /**
     *
     * Método que faz a conexão com o cartão.
     *
     * @throws IOException
     *
     */
    private fun conectarCartao() {
        try {
            mifareClassic!!.connect()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     *
     * Método que desconecta o cartão.
     *
     * @throws IOException
     *
     */
    private fun desconectaCartao() {
        try {
            mifareClassic!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    /**
     *
     * Método que faz a validação de senha
     * para leitura e escrita de um bloco no SETOR A
     *
     * @param bloco  = Número do bloco que será validado a permissão
     *
     * @throws IOException
     *
     * @return boolean = Caso falso é porque não existe a permissão para leitura do bloco
     *
     */
    @Throws(Exception::class)
    private fun validPermissaoBlocoA(bloco: Int): Boolean {
        var retorno = false
        retorno = try {
            if (mifareClassic!!.authenticateSectorWithKeyA(bloco,
                    MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY)
            ) {
                true
            } else if (mifareClassic!!.authenticateSectorWithKeyA(bloco,
                    MifareClassic.KEY_DEFAULT)
            ) {
                true
            } else if (mifareClassic!!.authenticateSectorWithKeyA(bloco,
                    MifareClassic.KEY_NFC_FORUM)
            ) {
                true
            } else {
                false
            }
        } catch (e: IOException) {
            throw Exception(e)
        }
        return retorno
    }

    /**
     *
     * Método que faz a validação de senha
     * para leitura e escrita de um bloco no SETOR B
     *
     * @param bloco  = Número do bloco que será validado a permissão
     *
     * @throws IOException
     *
     * @return boolean = Caso falso é porque não existe a permissão para leitura do bloco
     *
     */
    @Throws(Exception::class)
    private fun validPermissaoBlocoB(bloco: Int): Boolean {
        var retorno = false
        retorno = try {
            if (mifareClassic!!.authenticateSectorWithKeyB(bloco,
                    MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY)
            ) {
                true
            } else if (mifareClassic!!.authenticateSectorWithKeyB(bloco,
                    MifareClassic.KEY_DEFAULT)
            ) {
                true
            } else if (mifareClassic!!.authenticateSectorWithKeyB(bloco,
                    MifareClassic.KEY_NFC_FORUM)
            ) {
                true
            } else {
                false
            }
        } catch (e: IOException) {
            throw Exception(e)
        }
        return retorno
    }

    /**
     *
     * Método que grava os milesegundos na inicialização desta class
     *
     */
    private fun gravaTempoInicia() {
        tempInicial = System.currentTimeMillis()
    }

    /**
     *
     * Método que grava os milesegundos da finalização de um método.
     *
     * Esse método sempre será chamado na finally dos try/catch
     *
     *
     */
    private fun gravaTempoFinal() {
        tempFinal = System.currentTimeMillis()
    }

    /**
     *
     * Método que retorna a quantidade de segundos que foram
     * necessário para faz uma execução.
     *
     * @return Long = Segundos que foram usado para concluir um processo.
     *
     *
     */
    fun retornaTempoDeExeculcaoSegundos(): Long? {
        direfenca = tempFinal - tempInicial
        return direfenca / 60
    }

    /**
     *
     * Método que retorna o ID do cartão já convetido em Hexadecimal
     *
     * @return String = Id do cartão
     *
     */
    fun idCartaoHexadecimal(): String? {
        val idCartao = mifareClassic!!.tag.id
        var result: Long = 0
        if (idCartao == null) return ""
        for (i in idCartao.indices.reversed()) {
            result = result shl 8
            result = result or ((idCartao[i] and 0x0FF.toByte()).toLong())
        }
        return java.lang.Long.toString(result)
    }

    /**
     *
     * Método que retorna o ID do cartão em Bytes
     *
     * @return byte[] = Id do cartão
     *
     */
    fun cartaoId(): ByteArray? {
        return mifareClassic!!.tag.id
    }

    /**
     *
     * Método que gera um String randomicamente para ser usada em testes.
     *
     * @return String = Texto gerado randomicamente
     *
     */
    fun geraString(): String? {
        val uuid = UUID.randomUUID()
        val myRandom = uuid.toString()
        return myRandom.substring(0, 30)
    }
}