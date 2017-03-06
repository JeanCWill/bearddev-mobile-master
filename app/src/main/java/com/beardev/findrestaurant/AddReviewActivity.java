package com.beardev.findrestaurant;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddReviewActivity extends AppCompatActivity implements GenericResultReceiver.Receiver {

    private TextView txtName;
    private TextView txtDescription;
    private RatingBar rbPlace;
    private RatingBar rbPrice;
    private RatingBar rbAttendance;
    private RatingBar rbFood;
    private Button btSendReview;

    private Integer idRestaurant;

    private RestHelper restHelper;
    private GenericResultReceiver mReceiver;
    private ProgressDialog syncProgressDialog;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

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

        initDisplay();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutAddReview);

        restHelper = new RestHelper();

        mReceiver = new GenericResultReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    private void initDisplay() {
        txtName = (TextView) findViewById(R.id.txtName);
        txtDescription = (TextView) findViewById(R.id.txtDescription);

        rbPlace = (RatingBar) findViewById(R.id.rbPlace);
        rbPrice = (RatingBar) findViewById(R.id.rbPrice);
        rbAttendance = (RatingBar) findViewById(R.id.rbAttendance);
        rbFood = (RatingBar) findViewById(R.id.rbFood);

        btSendReview = (Button) findViewById(R.id.btSendReview);
        btSendReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Review review = new Review();
                review.setRestaurant_id(idRestaurant);
                review.setName(txtName.getText().toString());
                review.setDescriprion(txtDescription.getText().toString());
                review.setPlace(rbPlace.getNumStars());
                review.setPrice(rbPrice.getNumStars());
                review.setAttendance(rbAttendance.getNumStars());
                review.setFood(rbFood.getNumStars());

                restHelper.sendReview(review, mReceiver);

                syncProgressDialog = new ProgressDialog(AddReviewActivity.this);
                syncProgressDialog.setCanceledOnTouchOutside(false);
                syncProgressDialog.setCancelable(false);
                syncProgressDialog.setMessage(getString(R.string.processando));
            }
        });
    }

    @Override
    public void onReceiveResult(int resultCode, final Bundle resultData) {
        switch (resultCode) {
            case GenericResultReceiver.RUNNING:
                syncProgressDialog.show();
                break;

            case GenericResultReceiver.ADD_REVIEW:
                syncProgressDialog.dismiss();
                mostraSnack(getString(R.string.review_enviado));
                this.onBackPressed();
                break;

            case GenericResultReceiver.ERROR:
                syncProgressDialog.dismiss();
                Exception erro = (Exception) resultData.getSerializable("error");
                mostraSnack(getString(R.string.erro_ao_comunicar_servidor, erro.getMessage()));
                this.onBackPressed();
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
