package br.com.tectoy.tectoyautomacao;

import android.app.Application;
import android.util.Log;

import br.com.tectoy.tectoyautomacao.Utils.TectoySunmiPrint;

public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    /**
     * Connect print servive through interface library
     */
    private void init(){
        TectoySunmiPrint.getInstance().initSunmiPrinterService(this);
    }
}
