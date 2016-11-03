package com.beardev.findrestaurant;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardev.findrestaurant.realm.GenericDao;
import com.beardev.findrestaurant.realm.RestaurantRealm;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {

    TextView tvName;

    TextView tvDescription;
    TextView tvFuncSem;

    TextView tvFuncFimSem;
    Button btPictures;

    Button btMenu;
    Button btAvaliar;

    private GenericDao generiDao;
    private Integer idRestaurant;
    private RestHelper restHelper;
    private RestaurantRealm restaurant;
    private GenericResultReceiver mReceiver;
    private List<Restaurant> listRestaurants = new ArrayList<Restaurant>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        restHelper = new RestHelper();

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

        generiDao = new GenericDao<RestaurantRealm, Restaurant>(RestaurantRealm.class, Restaurant.class);
        restaurant = (RestaurantRealm) generiDao.getEntityById(idRestaurant);

        initDisplay();

        btAvaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(RestaurantActivity.this, NovaAvaliacaoActivity.class));
            }
        });

        btPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                generiDao = new GenericDao<RestaurantRealm, Restaurant>(RestaurantRealm.class, Restaurant.class);
//                listRestaurants = generiDao.getAllVos();
//
//                mReceiver = new GenericResultReceiver(new Handler());
//                mReceiver.setReceiver(this);
//
//                restHelper.listRestaurants(mReceiver);
//                startActivity(new Intent(RestaurantActivity.this, NovaAvaliacaoActivity.class));
            }
        });

        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(RestaurantActivity.this, NovaAvaliacaoActivity.class));
            }
        });

        setDisplayValues();
    }

    private void setDisplayValues() {
        tvName.setText(restaurant.getFantasyName());
        tvDescription.setText(restaurant.getDescription());
    }

    private void initDisplay() {
        tvName = (TextView) findViewById(R.id.tv_nomeRestaurante);
        tvDescription = (TextView) findViewById(R.id.tv_description);

        tvFuncSem = (TextView) findViewById(R.id.tv_func_sem);
        tvFuncFimSem = (TextView) findViewById(R.id.tv_func_fim_sem);

        btPictures = (Button) findViewById(R.id.bt_pictures);
        btMenu = (Button) findViewById(R.id.bt_menu);
        btAvaliar = (Button) findViewById(R.id.bt_avaliar);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Integer widthButton = (metrics.widthPixels / 2) - 40;

        btPictures.setWidth(widthButton);
        btMenu.setWidth(widthButton);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("restaurant", idRestaurant);
    }
}
