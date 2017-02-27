package com.beardev.findrestaurant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardev.findrestaurant.realm.GenericDao;
import com.beardev.findrestaurant.realm.MenuRealm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements GenericResultReceiver.Receiver {

    GridView gridMenuView;
    private PhotoViewAdapter menuAdapter;
    private Integer idRestaurant;

    private RestHelper restHelper;
    private GenericResultReceiver mReceiver;
    private GenericDao generiDao;
    private ArrayList<PhotoItem> imageItems = new ArrayList<>();
    private List<Menu> listMenus = new ArrayList<Menu>();
    private ProgressDialog syncProgressDialog;
    private ArrayAdapter<Menu> arrayAdapterMenus;

    private CoordinatorLayout coordinatorLayout;
    private Integer qtdeMenus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if(savedInstanceState != null){
            idRestaurant = savedInstanceState.getParcelable("restaurant");
        }
        else {
            if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getSerializable("restaurant") != null) {
                idRestaurant = (Integer) getIntent().getExtras().getSerializable("restaurant");
            } else {
                Toast.makeText(this, "Fail!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        restHelper = new RestHelper();
        generiDao = new GenericDao<MenuRealm, Menu>(MenuRealm.class, Menu.class);
        listMenus = generiDao.getAllVos();

        mReceiver = new GenericResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        restHelper.listMenus(idRestaurant, mReceiver);

        arrayAdapterMenus = new ArrayAdapter( MenuActivity.this, android.R.layout.select_dialog_singlechoice, listMenus);

        syncProgressDialog = new ProgressDialog(this);
        syncProgressDialog.setCanceledOnTouchOutside(false);
        syncProgressDialog.setCancelable(false);
        syncProgressDialog.setMessage(getString(R.string.processando));

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutMenu);

        gridMenuView = (GridView) findViewById(R.id.gridMenuView);

        gridMenuView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                PhotoItem item = (PhotoItem) parent.getItemAtPosition(position);
                //Create intent
                Intent intent = new Intent(MenuActivity.this, ImageDetailActivity.class);
                intent.putExtra("image", item.getPhoto());

                //Start details activity
                startActivity(intent);
            }
        });
    }

    private void getData() {
        String url = "";
        for (Menu menu: listMenus) {
            try {
                if (menu.getMenu() != null) {
                    if (!url.isEmpty()) {
                        url += "´";
                    }
                    url += "https://find-restaurant-jeanwill.c9users.io" + new JSONObject(menu.getMenu()).getJSONObject("menu").getJSONObject("thumb").get("url").toString();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        new MenuActivity.DownloadImage().execute(url);
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            Bitmap bitmap = null;

            for (String imageURL : URL[0].split("´")) {
                try {
                    InputStream input = new java.net.URL(imageURL).openStream();
                    bitmap = BitmapFactory.decodeStream(input);

                    imageItems.add(new PhotoItem(bitmap, qtdeMenus++));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            menuAdapter = new PhotoViewAdapter(MenuActivity.this, R.layout.photo_item_layout, imageItems);
            gridMenuView.setAdapter(menuAdapter);

            syncProgressDialog.dismiss();
        }
    }

    @Override
    public void onReceiveResult(int resultCode, final Bundle resultData) {
        Localizacao response;
        switch (resultCode) {
            case GenericResultReceiver.RUNNING:
                syncProgressDialog.show();
                break;

            case GenericResultReceiver.LIST_MENUS:
                String json = (String) resultData.get("response");

                generiDao.createOrUpdateFromJsonArray(json);
                listMenus = generiDao.getAllVos();
                arrayAdapterMenus = new ArrayAdapter(MenuActivity.this, android.R.layout.select_dialog_singlechoice, listMenus);

                getData();

                break;

            case GenericResultReceiver.ERROR:
                syncProgressDialog.dismiss();
                Exception erro = (Exception) resultData.getSerializable("error");
                mostraSnack(getString(R.string.erro_ao_comunicar_servidor, erro.getMessage()));
                break;
        }
    }

    private void mostraSnack(String msg) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, msg, Snackbar.LENGTH_INDEFINITE);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        snackbar.setDuration(Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
