package com.beardev.findrestaurant.realm;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class MigracaoBancoDados implements RealmMigration {

    public static final long VERSAO_BANCO = 1;


    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();


        //Descomentar para atualizações de versão de banco (VERSAO_BANCO)
        //if (oldVersion == 1) {
        //
        //    oldVersion++;
        //}

    }

}
