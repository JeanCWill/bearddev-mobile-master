package com.beardev.findrestaurant;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by developer on 12/05/16.
 */
@SuppressLint("ParcelCreator")
public class GenericResultReceiver extends ResultReceiver {

    public static final int RUNNING = 0;
    public static final int ENVIOU_LOCALIZACAO_ONIBUS = 1;
    public static final int LIST_PHOTOS = 2;
    public static final int LIST_MENUS = 5;
    public static final int LIST_REVIEWS = 6;
    public static final int LIST_ALL_RESTAURANTS = 3;
    public static final int ERROR = 4;
    public static final int ADD_REVIEW = 7;

    private Receiver mReceiver;

    public GenericResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}
