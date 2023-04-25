package com.example.lanchonetetoten

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.daruma.framework.mobile.DarumaMobile
import br.com.daruma.framework.mobile.comunicacao.exception.DarumaException
import br.com.itfast.tectoy.Dispositivo
import br.com.itfast.tectoy.TecToy
import kotlinx.android.synthetic.main.activity_pagment.*

class ActivityPagment : AppCompatActivity() {

    var centro = "" + 0x1B.toChar() + 0x61.toChar() + 0x31.toChar()
    var deslCentro = "" + 0x1B.toChar() + 0x61.toChar() + 0x30.toChar()
    var extra = "" + 0x1B.toChar() + 0x21.toChar() + 0x38.toChar()
    var deslExtra = "" + 0x1B.toChar() + 0x21.toChar() + 0x00.toChar()
    var negrito = "" + 0x1B.toChar() + 0x45.toChar() + 0x31.toChar()
    var deslNegrito = "" + 0x1B.toChar() + 0x45.toChar() + 0x30.toChar()

    var strNota = String

    var resp = CharArray(5)
    var flag = 1

    lateinit var k2mini: TecToy
    lateinit var  dmf: DarumaMobile
    var strXML : String = ""
    var nomedocliente : String = "Breno"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagment)

        dmf = DarumaMobile.inicializar(this, "@FRAMEWORK(TRATAEXCECAO=TRUE;LOGMEMORIA=25;TIMEOUTWS=10000;);@DISPOSITIVO(NAME=K2MINI)");

        lateinit var strNota : String

        strNota =
            """================================================$centro${negrito}TecToy Automacao - K2 MINI
            $deslNegrito${deslCentro}acesse e saiba mais: 
             www.tectoyautomacao.com.br
            ------------------------------------------------
            ${negrito}AppDemo usando o K2 Mini
            $deslNegrito------------------------------------------------
             ------- FOOD-SERVICE / Restaurantes -----------------------------------------------------------"""

        k2mini = TecToy(Dispositivo.K2_MINI, this)


        ///COnfigurações para NFCE
        val thrCgf: Thread
        try {
            thrCgf = Thread(config)
            thrCgf.start()
            //thrCgf.join();
        } catch (e: Exception) {
            mensagem("Erro na inicialização: " + e.message)
        }


        val btnMoney : Button = findViewById(R.id.btnMoney)
        btnMoney.setOnClickListener(View.OnClickListener {
            Fecharvenda()
            val st = Intent(this, MainActivity::class.java)
            startActivity(st)
            finish()
        })

        val btnCard : Button = findViewById(R.id.btnCard)
        btnCard.setOnClickListener(View.OnClickListener {
            Fecharvenda()
            val st = Intent(this, MainActivity::class.java)
            startActivity(st)
            finish()
        })

        val btnPix : Button = findViewById(R.id.btnPix)
        btnPix.setOnClickListener(View.OnClickListener {
            Fecharvenda()
            val st = Intent(this, MainActivity::class.java)
            startActivity(st)
            finish()
        })


        val btnFinal : Button = findViewById(R.id.btnfinsish)
        btnFinal.setOnClickListener(View.OnClickListener {
            finish()
            zerarValores()
        })

    }

    fun mensagem(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    var config = Runnable {
        try {
            Looper.prepare()
            configGneToNfce()
        } catch (de: DarumaException) {
            throw de
        }
    }

    fun configGneToNfce() {

        //dmf.regRetornarValor_NFCe("IDE\\Serie", resp)
        //val resposta: String = String(resp)
        //// como só estamos imrprimindo xml pronto não é necessário configurar tudo, mas precisa indicar a MarcaImpressora!
        //if (resposta.isEmpty() || resposta.trim { it <= ' ' }
               // .isEmpty() || resposta.trim { it <= ' ' } == "1") {
            /*dmf.RegAlterarValor_NFCe("CONFIGURACAO\\TipoAmbiente", "2", false);
                dmf.RegAlterarValor_NFCe("CONFIGURACAO\\EmpPK", "YPxRwGxIbpWZtwhuC0m+Wg==", false);
                dmf.RegAlterarValor_NFCe("CONFIGURACAO\\EmpCK", "eKdz2fcZg9ZMt3DrfF/KSIVoH59Ca6nN", false);
                dmf.RegAlterarValor_NFCe("CONFIGURACAO\\Token", "ABCDEF1234567890-ABCDEF1234567890-AB", false);
                dmf.RegAlterarValor_NFCe("CONFIGURACAO\\EmpCO", "001", false);
                dmf.RegAlterarValor_NFCe("CONFIGURACAO\\IdToken", "000001", false);
                dmf.RegAlterarValor_NFCe("CONFIGURACAO\\ArredondarTruncar", "A", false);
                dmf.RegAlterarValor_NFCe("EMIT\\CRT", "3", false);

                dmf.RegAlterarValor_NFCe("CONFIGURACAO\\AvisoContingencia", "1", false);
                dmf.RegAlterarValor_NFCe("CONFIGURACAO\\ImpressaoCompleta", "1", false);
                dmf.RegAlterarValor_NFCe("CONFIGURACAO\\NumeracaoAutomatica", "1", false);
                dmf.RegAlterarValor_NFCe("IDE\\cUF", "43", false);
                dmf.RegAlterarValor_NFCe("IDE\\cMunFG", "4321808", false);
                dmf.RegAlterarValor_NFCe("EMIT\\CNPJ", "06354976000149", false);
                dmf.RegAlterarValor_NFCe("EMIT\\IE", "0018001360", false);
                dmf.RegAlterarValor_NFCe("EMIT\\xNome", "IT FAST - TESTES", false);
                dmf.RegAlterarValor_NFCe("EMIT\\ENDEREMIT\\UF", "RS", false);
                dmf.RegAlterarValor_NFCe("IMPOSTO\\ICMS\\ICMS00\\orig", "0", false);
                dmf.RegAlterarValor_NFCe("IMPOSTO\\ICMS\\ICMS00\\CST", "00", false);
                dmf.RegAlterarValor_NFCe("IMPOSTO\\ICMS\\ICMS00\\modBC", "3", false);
                dmf.RegAlterarValor_NFCe("IMPOSTO\\PIS\\PISNT\\CST", "07", false);
                dmf.RegAlterarValor_NFCe("IMPOSTO\\COFINS\\COFINSNT\\CST", "07", false);*/
            dmf.RegAlterarValor_NFCe("CONFIGURACAO\\Impressora", "Q4", false)
            dmf.RegPersistirXML_NFCe()
            //dmf.confNumSeriesNF_NFCe("68", "890");
       // }
        dmf.RegAlterarValor_NFCe("CONFIGURACAO\\EstadoCFe", "0")
    }

   fun Fecharvenda() {
        val thrImp: Thread
        try {
            flag++
            thrImp = Thread(nfceImp)
            thrImp.start()
            thrImp.join()
        } catch (e: java.lang.Exception) {
            mensagem("Ocorreu erro na impressão:" + e.message)
        }
        zerarValores()
   }


   private val nfceImp = Runnable {
        if (flag == 1) {
            strXML =
                "<nfeProc versao=\"4.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\"><NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\"><infNFe Id=\"NFe43221106354976000149650670000016331026846523\" versao=\"4.00\"><ide><cUF>43</cUF><cNF>02684652</cNF><natOp>Venda</natOp><mod>65</mod><serie>67</serie><nNF>1633</nNF><dhEmi>2022-11-09T19:28:41-02:00</dhEmi><tpNF>1</tpNF><idDest>1</idDest><cMunFG>4321808</cMunFG><tpImp>4</tpImp><tpEmis>1</tpEmis><cDV>3</cDV><tpAmb>2</tpAmb><finNFe>1</finNFe><indFinal>1</indFinal><indPres>1</indPres><procEmi>0</procEmi><verProc>1.0</verProc></ide><emit><CNPJ>06354976000149</CNPJ><xNome>IT FAST - TESTES</xNome><enderEmit><xLgr>Logradouro</xLgr><nro>001</nro><xBairro>Bairro</xBairro><cMun>4321808</cMun><xMun>Municipio</xMun><UF>RS</UF><CEP>11111111</CEP></enderEmit><IE>1470049241</IE><CRT>3</CRT></emit><det nItem=\"1\"><prod><cProd>1113</cProd><cEAN>7896022204969</cEAN><xProd>NOTA FISCAL EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL</xProd><NCM>18063210</NCM><CEST>1705700</CEST><CFOP>5102</CFOP><uCom>UN</uCom><qCom>2</qCom><vUnCom>10.00</vUnCom><vProd>20.00</vProd><cEANTrib>7896022204969</cEANTrib><uTrib>UN</uTrib><qTrib>2</qTrib><vUnTrib>10.00</vUnTrib><indTot>1</indTot></prod><imposto><ICMS><ICMS00><orig>0</orig><CST>00</CST><modBC>3</modBC><vBC>20.00</vBC><pICMS>0.00</pICMS><vICMS>0.00</vICMS></ICMS00></ICMS><PIS><PISNT><CST>07</CST></PISNT></PIS><COFINS><COFINSNT><CST>07</CST></COFINSNT></COFINS></imposto></det><det nItem=\"2\"><prod><cProd>1114</cProd><cEAN>7896022204969</cEAN><xProd>Refrigerante</xProd><NCM>18063210</NCM><CEST>1705700</CEST><CFOP>5102</CFOP><uCom>UN</uCom><qCom>1</qCom><vUnCom>5.00</vUnCom><vProd>5.00</vProd><cEANTrib>7896022204969</cEANTrib><uTrib>UN</uTrib><qTrib>1</qTrib><vUnTrib>5.00</vUnTrib><indTot>1</indTot></prod><imposto><ICMS><ICMS00><orig>0</orig><CST>00</CST><modBC>3</modBC><vBC>5.00</vBC><pICMS>0.00</pICMS><vICMS>0.00</vICMS></ICMS00></ICMS><PIS><PISNT><CST>07</CST></PISNT></PIS><COFINS><COFINSNT><CST>07</CST></COFINSNT></COFINS></imposto></det><total><ICMSTot><vBC>25.00</vBC><vICMS>0.00</vICMS><vICMSDeson>0.00</vICMSDeson><vFCP>0.00</vFCP><vBCST>0.00</vBCST><vST>0.00</vST><vFCPST>0.00</vFCPST><vFCPSTRet>0.00</vFCPSTRet><vProd>25.00</vProd><vFrete>0.00</vFrete><vSeg>0.00</vSeg><vDesc>0.00</vDesc><vII>0.00</vII><vIPI>0.00</vIPI><vIPIDevol>0.00</vIPIDevol><vPIS>0.00</vPIS><vCOFINS>0.00</vCOFINS><vOutro>0.00</vOutro><vNF>25.00</vNF></ICMSTot></total><transp><modFrete>9</modFrete></transp><pag><detPag><tPag>01</tPag><vPag>25.00</vPag></detPag></pag><infAdic><infCpl>Teste IT FAST demonstracao.</infCpl></infAdic><infRespTec><CNPJ>06354976000149</CNPJ><xContato>Adilson Weddigen</xContato><email>adilsonweddigen@migrate.info</email><fone>5535354800</fone></infRespTec></infNFe><infNFeSupl><qrCode><![CDATA[https://www.sefaz.rs.gov.br/NFCE/NFCE-COM.aspx?p=43221106354976000149650670000016331026846523|2|2|1|2815F2B11827699A7DF518B601097FBA89F18F5D]]></qrCode><urlChave>www.sefaz.rs.gov.br/nfce/consulta</urlChave></infNFeSupl><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\" /><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\" /><Reference URI=\"#NFe43221106354976000149650670000016331026846523\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\" /><Transform Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\" /></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\" /><DigestValue>rLSKkFib6NDR2BdbZzr9f2Fi6rQ=</DigestValue></Reference></SignedInfo><SignatureValue>0G4ep/bUFCRN0Dq6iPIYtcFAt7yMXYlEDFdfkG7CwbcW1BkV8dxTFOvFR7kIFTVAfImVPNdN52GaZINhSUnGfMIzfuLbcZIL1oVgroAePeiU6TquZsITFKCnHTG3UYl9UqkrMM/iqbLJh/p7/qdoS70ndElWcWF18MWDqGKAQhGkGQWmfvi0bOcJtifC/PSjmRAGVyXsohcaO8zfQWdJs8zu5+UJ9PRNQbNN5vqwXDSOAktIJkKK9QbWaq6bHTIVbws+vQrLV+JjmZisvD5YLgeTesAs33ZZ80YH1X4ob9X0P3a/GA+6q/1knze3VGr1Bm85Av/cNxU8w9EiyIOTPA==</SignatureValue><KeyInfo><X509Data><X509Certificate>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</X509Certificate></X509Data></KeyInfo></Signature></NFe><protNFe versao=\"4.00\"><infProt><tpAmb>2</tpAmb><verAplic>RSnfce202210200825</verAplic><chNFe>43221106354976000149650670000016331026846523</chNFe><dhRecbto>2022-11-09T18:28:44-03:00</dhRecbto><nProt>143220000797520</nProt><digVal>rLSKkFib6NDR2BdbZzr9f2Fi6rQ=</digVal><cStat>100</cStat><xMotivo>Autorizado o uso da NF-e</xMotivo></infProt></protNFe></nfeProc>"
        } else if (flag == 2) {
            strXML =
                "<nfeProc versao=\"4.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\"><NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\"><infNFe Id=\"NFe43221106354976000149650670000016351026912105\" versao=\"4.00\"><ide><cUF>43</cUF><cNF>02691210</cNF><natOp>Venda</natOp><mod>65</mod><serie>67</serie><nNF>1635</nNF><dhEmi>2022-11-09T19:29:52-02:00</dhEmi><tpNF>1</tpNF><idDest>1</idDest><cMunFG>4321808</cMunFG><tpImp>4</tpImp><tpEmis>1</tpEmis><cDV>5</cDV><tpAmb>2</tpAmb><finNFe>1</finNFe><indFinal>1</indFinal><indPres>1</indPres><procEmi>0</procEmi><verProc>1.0</verProc></ide><emit><CNPJ>06354976000149</CNPJ><xNome>IT FAST - TESTES</xNome><enderEmit><xLgr>Logradouro</xLgr><nro>001</nro><xBairro>Bairro</xBairro><cMun>4321808</cMun><xMun>Municipio</xMun><UF>RS</UF><CEP>11111111</CEP></enderEmit><IE>1470049241</IE><CRT>3</CRT></emit><det nItem=\"1\"><prod><cProd>1112</cProd><cEAN>7896022204969</cEAN><xProd>NOTA FISCAL EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL</xProd><NCM>18063210</NCM><CEST>1705700</CEST><CFOP>5102</CFOP><uCom>UN</uCom><qCom>4</qCom><vUnCom>7.00</vUnCom><vProd>28.00</vProd><cEANTrib>7896022204969</cEANTrib><uTrib>UN</uTrib><qTrib>4</qTrib><vUnTrib>7.00</vUnTrib><indTot>1</indTot></prod><imposto><ICMS><ICMS00><orig>0</orig><CST>00</CST><modBC>3</modBC><vBC>28.00</vBC><pICMS>0.00</pICMS><vICMS>0.00</vICMS></ICMS00></ICMS><PIS><PISNT><CST>07</CST></PISNT></PIS><COFINS><COFINSNT><CST>07</CST></COFINSNT></COFINS></imposto></det><det nItem=\"2\"><prod><cProd>1114</cProd><cEAN>7896022204969</cEAN><xProd>Refrigerante</xProd><NCM>18063210</NCM><CEST>1705700</CEST><CFOP>5102</CFOP><uCom>UN</uCom><qCom>7</qCom><vUnCom>5.00</vUnCom><vProd>35.00</vProd><cEANTrib>7896022204969</cEANTrib><uTrib>UN</uTrib><qTrib>7</qTrib><vUnTrib>5.00</vUnTrib><indTot>1</indTot></prod><imposto><ICMS><ICMS00><orig>0</orig><CST>00</CST><modBC>3</modBC><vBC>35.00</vBC><pICMS>0.00</pICMS><vICMS>0.00</vICMS></ICMS00></ICMS><PIS><PISNT><CST>07</CST></PISNT></PIS><COFINS><COFINSNT><CST>07</CST></COFINSNT></COFINS></imposto></det><det nItem=\"3\"><prod><cProd>1116</cProd><cEAN>7896022204969</cEAN><xProd>Tortinha</xProd><NCM>18063210</NCM><CEST>1705700</CEST><CFOP>5102</CFOP><uCom>UN</uCom><qCom>2</qCom><vUnCom>5.00</vUnCom><vProd>10.00</vProd><cEANTrib>7896022204969</cEANTrib><uTrib>UN</uTrib><qTrib>2</qTrib><vUnTrib>5.00</vUnTrib><indTot>1</indTot></prod><imposto><ICMS><ICMS00><orig>0</orig><CST>00</CST><modBC>3</modBC><vBC>10.00</vBC><pICMS>0.00</pICMS><vICMS>0.00</vICMS></ICMS00></ICMS><PIS><PISNT><CST>07</CST></PISNT></PIS><COFINS><COFINSNT><CST>07</CST></COFINSNT></COFINS></imposto></det><total><ICMSTot><vBC>73.00</vBC><vICMS>0.00</vICMS><vICMSDeson>0.00</vICMSDeson><vFCP>0.00</vFCP><vBCST>0.00</vBCST><vST>0.00</vST><vFCPST>0.00</vFCPST><vFCPSTRet>0.00</vFCPSTRet><vProd>73.00</vProd><vFrete>0.00</vFrete><vSeg>0.00</vSeg><vDesc>0.00</vDesc><vII>0.00</vII><vIPI>0.00</vIPI><vIPIDevol>0.00</vIPIDevol><vPIS>0.00</vPIS><vCOFINS>0.00</vCOFINS><vOutro>0.00</vOutro><vNF>73.00</vNF></ICMSTot></total><transp><modFrete>9</modFrete></transp><pag><detPag><tPag>01</tPag><vPag>73.00</vPag></detPag></pag><infAdic><infCpl>Teste IT FAST demonstracao.</infCpl></infAdic><infRespTec><CNPJ>06354976000149</CNPJ><xContato>Adilson Weddigen</xContato><email>adilsonweddigen@migrate.info</email><fone>5535354800</fone></infRespTec></infNFe><infNFeSupl><qrCode><![CDATA[https://www.sefaz.rs.gov.br/NFCE/NFCE-COM.aspx?p=43221106354976000149650670000016351026912105|2|2|1|2B7B79C12C2F8F428778EE16B036323D10983EBA]]></qrCode><urlChave>www.sefaz.rs.gov.br/nfce/consulta</urlChave></infNFeSupl><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\" /><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\" /><Reference URI=\"#NFe43221106354976000149650670000016351026912105\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\" /><Transform Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\" /></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\" /><DigestValue>2nAg5o3tqaYj1JKg5tqY/VAdqgc=</DigestValue></Reference></SignedInfo><SignatureValue>gkSIdFhAXMs6RXTa342HgmgQ4bzrnerRzlCrClP+AGId0OskArGRv+F7Ex8u7QLRraCKTyFMsHaIFZK2h1DUbBBfj421dQnflxhHoHG8jLj208EAoIh1RtVIykSgOCDqUWGV/TLkrmw0SZcHpSzPsa/29sxQvFnrOtdpc+CIVJOUfBkm/nxULtighSLztPhC6Q69EID4Q7E8ErBUUJ4uCEPnxHQzAHBMa+A8DEhV+pxG3XKkAcr74w4p/kaiIciqZG71uxUTugWkVUfuOGonHmgiWlfeRT2yj5CTMS5gt75YIk6aOINSGobLpZtsuPSvmnTF2gsMhm7HPm0jEyEoxg==</SignatureValue><KeyInfo><X509Data><X509Certificate>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</X509Certificate></X509Data></KeyInfo></Signature></NFe><protNFe versao=\"4.00\"><infProt><tpAmb>2</tpAmb><verAplic>RSnfce202210200825</verAplic><chNFe>43221106354976000149650670000016351026912105</chNFe><dhRecbto>2022-11-09T18:29:55-03:00</dhRecbto><nProt>143220000797523</nProt><digVal>2nAg5o3tqaYj1JKg5tqY/VAdqgc=</digVal><cStat>100</cStat><xMotivo>Autorizado o uso da NF-e</xMotivo></infProt></protNFe></nfeProc>"
        } else if (flag == 3) {
            strXML =
                "<nfeProcnot versao=\"4.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\"><NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\"><infNFe Id=\"NFe43221106354976000149650670000016361026944926\" versao=\"4.00\"><ide><cUF>43</cUF><cNF>02694492</cNF><natOp>Venda</natOp><mod>65</mod><serie>67</serie><nNF>1636</nNF><dhEmi>2022-11-09T19:33:15-02:00</dhEmi><tpNF>1</tpNF><idDest>1</idDest><cMunFG>4321808</cMunFG><tpImp>4</tpImp><tpEmis>1</tpEmis><cDV>6</cDV><tpAmb>2</tpAmb><finNFe>1</finNFe><indFinal>1</indFinal><indPres>1</indPres><procEmi>0</procEmi><verProc>1.0</verProc></ide><emit><CNPJ>06354976000149</CNPJ><xNome>IT FAST - TESTES</xNome><enderEmit><xLgr>Logradouro</xLgr><nro>001</nro><xBairro>Bairro</xBairro><cMun>4321808</cMun><xMun>Municipio</xMun><UF>RS</UF><CEP>11111111</CEP></enderEmit><IE>1470049241</IE><CRT>3</CRT></emit><det nItem=\"1\"><prod><cProd>1111</cProd><cEAN>7896022204969</cEAN><xProd>NOTA FISCAL EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL</xProd><NCM>18063210</NCM><CEST>1705700</CEST><CFOP>5102</CFOP><uCom>UN</uCom><qCom>1</qCom><vUnCom>10.00</vUnCom><vProd>10.00</vProd><cEANTrib>7896022204969</cEANTrib><uTrib>UN</uTrib><qTrib>1</qTrib><vUnTrib>10.00</vUnTrib><indTot>1</indTot></prod><imposto><ICMS><ICMS00><orig>0</orig><CST>00</CST><modBC>3</modBC><vBC>10.00</vBC><pICMS>0.00</pICMS><vICMS>0.00</vICMS></ICMS00></ICMS><PIS><PISNT><CST>07</CST></PISNT></PIS><COFINS><COFINSNT><CST>07</CST></COFINSNT></COFINS></imposto></det><det nItem=\"2\"><prod><cProd>1112</cProd><cEAN>7896022204969</cEAN><xProd>Batata</xProd><NCM>18063210</NCM><CEST>1705700</CEST><CFOP>5102</CFOP><uCom>UN</uCom><qCom>1</qCom><vUnCom>7.00</vUnCom><vProd>7.00</vProd><cEANTrib>7896022204969</cEANTrib><uTrib>UN</uTrib><qTrib>1</qTrib><vUnTrib>7.00</vUnTrib><indTot>1</indTot></prod><imposto><ICMS><ICMS00><orig>0</orig><CST>00</CST><modBC>3</modBC><vBC>7.00</vBC><pICMS>0.00</pICMS><vICMS>0.00</vICMS></ICMS00></ICMS><PIS><PISNT><CST>07</CST></PISNT></PIS><COFINS><COFINSNT><CST>07</CST></COFINSNT></COFINS></imposto></det><det nItem=\"3\"><prod><cProd>1113</cProd><cEAN>7896022204969</cEAN><xProd>Nuggets</xProd><NCM>18063210</NCM><CEST>1705700</CEST><CFOP>5102</CFOP><uCom>UN</uCom><qCom>1</qCom><vUnCom>10.00</vUnCom><vProd>10.00</vProd><cEANTrib>7896022204969</cEANTrib><uTrib>UN</uTrib><qTrib>1</qTrib><vUnTrib>10.00</vUnTrib><indTot>1</indTot></prod><imposto><ICMS><ICMS00><orig>0</orig><CST>00</CST><modBC>3</modBC><vBC>10.00</vBC><pICMS>0.00</pICMS><vICMS>0.00</vICMS></ICMS00></ICMS><PIS><PISNT><CST>07</CST></PISNT></PIS><COFINS><COFINSNT><CST>07</CST></COFINSNT></COFINS></imposto></det><total><ICMSTot><vBC>27.00</vBC><vICMS>0.00</vICMS><vICMSDeson>0.00</vICMSDeson><vFCP>0.00</vFCP><vBCST>0.00</vBCST><vST>0.00</vST><vFCPST>0.00</vFCPST><vFCPSTRet>0.00</vFCPSTRet><vProd>27.00</vProd><vFrete>0.00</vFrete><vSeg>0.00</vSeg><vDesc>0.00</vDesc><vII>0.00</vII><vIPI>0.00</vIPI><vIPIDevol>0.00</vIPIDevol><vPIS>0.00</vPIS><vCOFINS>0.00</vCOFINS><vOutro>0.00</vOutro><vNF>27.00</vNF></ICMSTot></total><transp><modFrete>9</modFrete></transp><pag><detPag><tPag>01</tPag><vPag>27.00</vPag></detPag></pag><infAdic><infCpl>Teste IT FAST demonstracao.</infCpl></infAdic><infRespTec><CNPJ>06354976000149</CNPJ><xContato>Adilson Weddigen</xContato><email>adilsonweddigen@migrate.info</email><fone>5535354800</fone></infRespTec></infNFe><infNFeSupl><qrCode><![CDATA[https://www.sefaz.rs.gov.br/NFCE/NFCE-COM.aspx?p=43221106354976000149650670000016361026944926|2|2|1|6A18B5AE19427D0C0F5996DC83B3F468DA9D0360]]></qrCode><urlChave>www.sefaz.rs.gov.br/nfce/consulta</urlChave></infNFeSupl><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\" /><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\" /><Reference URI=\"#NFe43221106354976000149650670000016361026944926\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\" /><Transform Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\" /></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\" /><DigestValue>ST/28VkyvbzBqmo8xPh7cByvJFQ=</DigestValue></Reference></SignedInfo><SignatureValue>zJMKj1/MCkFtNWh1Koq6h7IGTWJv6Jn7AIK+2PSQHh4uCzqE3MyqYhFxWS9E7nakCFy+44T0xW4hAfSosPRCSHYTE4zpuU3Kz0+uPpU0/P9mASbtsk+oQb7CIVh67eG30FpDu8KkZ/4G7BCbOMML21DlqLjR70XpXfCMyeuNf9gczRRSBohgrIxEmVJqrYABtalHdXuwqZ1SwHwhjA1MaUIu+cQtqOMLdmwMRxJS6K1p20Pbk0cYHr5zXzFQKnfxIuJUWIG3o26ZxzH37mBLhK6GBWEqGVkxL/h0ybd3VGMX6+/ILHuteQU/QYW+my9JcRjmuQsysKlE7s14ESW4gQ==</SignatureValue><KeyInfo><X509Data><X509Certificate>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</X509Certificate></X509Data></KeyInfo></Signature></NFe><protNFe versao=\"4.00\"><infProt><tpAmb>2</tpAmb><verAplic>RSnfce202210200825</verAplic><chNFe>43221106354976000149650670000016361026944926</chNFe><dhRecbto>2022-11-09T18:33:18-03:00</dhRecbto><nProt>143220000797525</nProt><digVal>ST/28VkyvbzBqmo8xPh7cByvJFQ=</digVal><cStat>100</cStat><xMotivo>Autorizado o uso da NF-e</xMotivo></infProt></protNFe></nfeProc>"
            flag = 0
        }

        try {
            Looper.prepare()
            try {
               // k2mini.imprimir(byteArrayOf(0x1B, 0x40, 0x1C, 0x2E, 0x1B, 0x74, 0x10))
               // k2mini.imprimir("passou aqui \n\n\n\n" )
                //dmf.enviarComando(reinicImp+cmdCanModoChines+cfgCodePage);
                dmf.iCFImprimirParametrizado_NFCe(strXML, strXML, "", 48, 1, "")
                // dmf.enviarComando("teste impressão Açaí, Pó de Café\n\n\n\n");
            } catch (e: java.lang.Exception) {
                mensagem("Erro: " + e.message)
            }
        } catch (de: DarumaException ) {
            throw de
        }
    }

    fun zerarValores(){
        val soma = 0
    }
}