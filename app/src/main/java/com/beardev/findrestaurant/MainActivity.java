package com.beardev.findrestaurant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.beardev.findrestaurant.realm.GenericDao;
import com.beardev.findrestaurant.realm.RestaurantRealm;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLoadedCallback, GenericResultReceiver.Receiver {

    private static final int REQUEST_ACCESS_LOCATION = 1;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private CoordinatorLayout coordinatorLayout;
    private RestHelper restHelper;
    private ArrayAdapter<Restaurant> arrayAdapterRestaurants;
    private List<Restaurant> listRestaurants = new ArrayList<Restaurant>();
    private ProgressDialog syncProgressDialog;
    private GenericResultReceiver mReceiver;
    private GenericDao generiDao;
    private HashMap<Marker, Integer> hashMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        hashMarkers = new HashMap<>();
        restHelper = new RestHelper();
        inicializaComponentes();

        generiDao = new GenericDao<RestaurantRealm, Restaurant>(RestaurantRealm.class, Restaurant.class);
        listRestaurants = generiDao.getAllVos();

        mReceiver = new GenericResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        restHelper.listRestaurants(mReceiver);

        arrayAdapterRestaurants = new ArrayAdapter( MainActivity.this, android.R.layout.select_dialog_singlechoice, listRestaurants);

        syncProgressDialog = new ProgressDialog(this);
        syncProgressDialog.setCanceledOnTouchOutside(false);
        syncProgressDialog.setCancelable(false);
        syncProgressDialog.setMessage(getString(R.string.processando));
    }

    private void insertMarker(Restaurant restaurant, Location actualLocation) {
        Location restaurantLocation = new Location("");
        restaurantLocation.setLatitude(restaurant.getLatitude());
        restaurantLocation.setLongitude(restaurant.getLongitude());

        this.hashMarkers.put(this.mMap.addMarker(new MarkerOptions()
                .position(new LatLng(restaurant.getLatitude(), restaurant.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .draggable(false)
                .title(restaurant.getFantasyName())
                .snippet(restaurant.getCategory_id().toString() + " \nDsiatância: " + String.format("%.2f", (actualLocation.distanceTo(restaurantLocation) / 1000)) + " Km")),
                restaurant.getId());
    }

    private void inicializaComponentes() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setConfiguracoesMapa();

        constroiGoogleApiClient(this);

        this.mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Integer idRestaurant = hashMarkers.get(marker);
                Intent intent = new Intent(MainActivity.this, RestaurantActivity.class);
                intent.putExtra("restaurant", idRestaurant);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    private void setConfiguracoesMapa() {

        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.setMapType(1);
        }
    }

    private synchronized void constroiGoogleApiClient(Context context) {
        this.mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        this.mGoogleApiClient.connect();
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

    private LatLng pegaPosicao() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(200);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return null;

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, MainActivity.this);

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation == null) {

            mostraSnack(getString(R.string.gps_inativo));
            return null;
        }

       return new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
    }

    public void posicionaCamera(LatLng point) {
        this.mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(point).zoom(14).build();
        this.mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_ACCESS_LOCATION);
        }

        if(ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            posicionaCamera(pegaPosicao());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @SuppressWarnings("WrongConstant")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setConfiguracoesMapa();
                    pegaPosicao();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.permissao_negada), Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    MainActivity.this.onBackPressed();
                                }
                            });

                    // Changing message text color
                    snackbar.setActionTextColor(Color.RED);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);

                    snackbar.setDuration(60000);
                    snackbar.show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
                listRestaurants = generiDao.getAllVos();
                arrayAdapterRestaurants = new ArrayAdapter( MainActivity.this, android.R.layout.select_dialog_singlechoice, listRestaurants);

                LatLng actualPosition = pegaPosicao();
                Location actualLocation = new Location("");
                actualLocation.setLatitude(actualPosition.latitude);
                actualLocation.setLongitude(actualPosition.longitude);

                for (Restaurant restaurant : listRestaurants) {
                    insertMarker(restaurant, actualLocation);
                }

                posicionaCamera(pegaPosicao());
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
}
