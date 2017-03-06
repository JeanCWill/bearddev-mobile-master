package com.beardev.findrestaurant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardev.findrestaurant.realm.GenericDao;
import com.beardev.findrestaurant.realm.MenuRealm;
import com.beardev.findrestaurant.realm.ReviewRealm;

import java.util.ArrayList;

public class ReviewsActivity extends AppCompatActivity implements GenericResultReceiver.Receiver {

    GridView gridReviewView;
    private ReviewViewAdapter reviewAdapter;
    private Integer idRestaurant;

    private RestHelper restHelper;
    private GenericResultReceiver mReceiver;
    private GenericDao generiDao;
    private ArrayList<PhotoItem> reviewItems = new ArrayList<>();
    private ArrayList<Review> listReviews = new ArrayList<>();
    private ProgressDialog syncProgressDialog;
    private ArrayAdapter<Review> arrayAdapterReviews;

    private CoordinatorLayout coordinatorLayout;
    private Button btAddReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

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
        generiDao = new GenericDao<ReviewRealm, Review>(ReviewRealm.class, Review.class);
        listReviews = (ArrayList<Review>) generiDao.getAllVos();

        mReceiver = new GenericResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        restHelper.listReviews(idRestaurant, mReceiver);

        arrayAdapterReviews = new ArrayAdapter( ReviewsActivity.this, android.R.layout.select_dialog_singlechoice, listReviews);

        syncProgressDialog = new ProgressDialog(this);
        syncProgressDialog.setCanceledOnTouchOutside(false);
        syncProgressDialog.setCancelable(false);
        syncProgressDialog.setMessage(getString(R.string.processando));

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutReviews);

        gridReviewView = (GridView) findViewById(R.id.gridReviewView);

        btAddReview = (Button) findViewById(R.id.btAddReview);

        btAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewsActivity.this, AddReviewActivity.class);
                intent.putExtra("restaurant", idRestaurant);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        reviewAdapter = new ReviewViewAdapter(ReviewsActivity.this, R.layout.review_item_layout, listReviews);
        gridReviewView.setAdapter(reviewAdapter);

        syncProgressDialog.dismiss();
    }

    @Override
    public void onReceiveResult(int resultCode, final Bundle resultData) {
        Localizacao response;
        switch (resultCode) {
            case GenericResultReceiver.RUNNING:
                syncProgressDialog.show();
                break;

            case GenericResultReceiver.LIST_REVIEWS:
                String json = (String) resultData.get("response");

                generiDao.createOrUpdateFromJsonArray(json);
                listReviews = (ArrayList<Review>) generiDao.getAllVos();
                arrayAdapterReviews = new ArrayAdapter(ReviewsActivity.this, android.R.layout.select_dialog_singlechoice, listReviews);

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

    @Override
    protected void onRestart() {
        super.onRestart();

        restHelper.listReviews(idRestaurant, mReceiver);

        syncProgressDialog = new ProgressDialog(this);
        syncProgressDialog.setCanceledOnTouchOutside(false);
        syncProgressDialog.setCancelable(false);
        syncProgressDialog.setMessage(getString(R.string.processando));
    }
}
