package com.beardev.findrestaurant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
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
import com.beardev.findrestaurant.realm.PhotoRealm;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends AppCompatActivity implements GenericResultReceiver.Receiver{

    GridView gridPhotoView;
    private PhotoViewAdapter photoAdapter;
    private Integer idRestaurant;

    private RestHelper restHelper;
    private GenericResultReceiver mReceiver;
    private GenericDao generiDao;
    private ArrayList<PhotoItem> imageItems = new ArrayList<>();
    private List<Photo> listPhotos = new ArrayList<Photo>();
    private ProgressDialog syncProgressDialog;
    private ArrayAdapter<Photo> arrayAdapterPhotos;

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

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
        generiDao = new GenericDao<PhotoRealm, Photo>(PhotoRealm.class, Photo.class);
        listPhotos = generiDao.getAllVos();

        mReceiver = new GenericResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        restHelper.listPhotos(idRestaurant, mReceiver);

        arrayAdapterPhotos = new ArrayAdapter( PhotoActivity.this, android.R.layout.select_dialog_singlechoice, listPhotos);

        syncProgressDialog = new ProgressDialog(this);
        syncProgressDialog.setCanceledOnTouchOutside(false);
        syncProgressDialog.setCancelable(false);
        syncProgressDialog.setMessage(getString(R.string.processando));

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        gridPhotoView = (GridView) findViewById(R.id.gridPhotoView);

        gridPhotoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                PhotoItem item = (PhotoItem) parent.getItemAtPosition(position);
                //Create intent
                Intent intent = new Intent(PhotoActivity.this, ImageDetailActivity.class);
                intent.putExtra("image", item.getPhoto());

                //Start details activity
                startActivity(intent);
            }
        });
    }

    // Prepare some dummy data for gridview
    private ArrayList<PhotoItem> getData() {
//        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
//        for (int i = 0; i < imgs.length(); i++) {
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
//            imageItems.add(new PhotoItem(bitmap, "Image#" + i));
//        }
        for (Photo photo: listPhotos) {
            photo.getPhoto();
        }
        return imageItems;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Localizacao response;
        switch (resultCode) {
            case GenericResultReceiver.RUNNING:
                syncProgressDialog.show();
                break;

            case GenericResultReceiver.BUSCOU_TODAS_LINHAS:
                String json = (String) resultData.get("response");
                generiDao.createOrUpdateFromJsonArray(json);
                listPhotos = generiDao.getAllVos();
                arrayAdapterPhotos = new ArrayAdapter( PhotoActivity.this, android.R.layout.select_dialog_singlechoice, listPhotos);


                photoAdapter = new PhotoViewAdapter(this, R.layout.photo_item_layout, getData());
                gridPhotoView.setAdapter(photoAdapter);


                syncProgressDialog.dismiss();
                mostraSnack(getString(R.string.linhas_atualizadas));
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
