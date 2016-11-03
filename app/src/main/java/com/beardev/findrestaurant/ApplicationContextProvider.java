package com.beardev.findrestaurant;

import android.app.Application;
import android.content.Context;

import com.beardev.findrestaurant.realm.MigracaoBancoDados;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Jean on 11/06/2016.
 */
public class ApplicationContextProvider extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        this.configuraRealm();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    private void configuraRealm() {
        RealmConfiguration configuracaoRealm = new RealmConfiguration.Builder(this)
                .schemaVersion(MigracaoBancoDados.VERSAO_BANCO)
                .migration(new MigracaoBancoDados())
                .build();
        Realm.setDefaultConfiguration(configuracaoRealm);
    }
}
