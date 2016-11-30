package com.example.phil.mapsrestfulinterfaceexample;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phil.mapsrestfulinterfaceexample.POJO.Company;
import com.example.phil.mapsrestfulinterfaceexample.POJO.Login;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private SharedPreferences prefs;
    private SharedPreferences companyPrefs;
    private GoogleApiClient mGoogleApiClient;
    private TextView header_name;
    private TextView header_email;
    private String firstName;
    private String lastName;
    private String reviewsWritten;
    private String email;
    private String id;
    private TextView mAttributions;
    private MapInfoWindowAdapter mapInfoWindowAdapter;
    public static final String BASE_URL = "http://192.168.1.14";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //show error dialog if Google Play Services not available
        if (!isGooglePlayServicesAvailable()) {
            Log.d("onCreate", "Google Play Services not available. Ending Test case.");
            finish();
        }
        else {
            mGoogleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //TODO: add something here?
            }
        });

      DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        prefs = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        firstName = prefs.getString("firstName", "");
        lastName = prefs.getString("lastName", "");
        email = prefs.getString("email", "");
        reviewsWritten = prefs.getString("rewviewsWritten", "");
        id = prefs.getString("id", "");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
       navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        header_name = (TextView)header.findViewById(R.id.header_name);
        header_email = (TextView)header.findViewById(R.id.header_email);
        header_name.setText(firstName);
        header_email.setText(email);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapInfoWindowAdapter = new MapInfoWindowAdapter(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(mapInfoWindowAdapter);
        getAllCompanies();
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent detailIntent = new Intent(MainActivity.this, CompanyDetail.class);
                startActivity(detailIntent);
                companyPrefs = getSharedPreferences("companyDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = companyPrefs.edit();
                String tempCompanyID = (String) marker.getTag();
                editor.putString("detailCompanyID",tempCompanyID);
                editor.commit();
            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here. If anything is selected, currently just closes MainActivity
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            logout();

        }  else if (id == R.id.nav_add_business) {
            LatLngBounds search_bounds = mMap.getProjection().getVisibleRegion().latLngBounds;

            try {
                AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                        .build();
                Intent intent =
                        new PlaceAutocomplete
                                .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .setFilter(typeFilter)
                                .setBoundsBias(search_bounds)
                                .build(this);
                startActivityForResult(intent, 1);
            } catch (GooglePlayServicesRepairableException e) {
                Log.d("autocomplete error", "repairable");
            } catch (GooglePlayServicesNotAvailableException e) {
                Log.d("autocomplete error", "not available");
            }

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout(){
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("email", "");
                        editor.putString("password","");
                        editor.putString("firstName","");
                        editor.putString("lastName", "");
                        editor.putString("reviewsWritten","");
                        editor.putString("id", "");
                        editor.putBoolean("autoLogin", false);
                        editor.commit();
                        Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(logoutIntent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(this, data);
                final String tempCompanyName = (String) place.getName();
                final String tempCompanyAddress = (String) place.getAddress();
                final LatLng tempCompanyLatLng = place.getLatLng();
                final String tempCompanyGooglePlaceID = place.getId();
                Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());

                final double tempLat = tempCompanyLatLng.latitude;
                final double tempLong = tempCompanyLatLng.longitude;


                new AlertDialog.Builder(this)
                        .setTitle("Confirm addition")
                        .setMessage("Are you sure you want to add this company?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            addCompany(tempCompanyName,tempCompanyAddress,tempLat, tempLong,tempCompanyGooglePlaceID);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);

                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private ApiInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiInterface mInterfaceService = retrofit.create(ApiInterface.class);
        return mInterfaceService;
    }

    private void addCompany(final String name, String address, double lat, double lng, String googleId){
        ApiInterface mApiService = this.getInterfaceService();
        Call<Company> mService = mApiService.addCompany(name, address, lat,lng,googleId);
        mService.enqueue(new Callback<Company>() {
            @Override
            public void onResponse(Call<Company> call, Response<Company> response) {
                Company mCompanyObject = response.body();
                String returnedResponse = mCompanyObject.getCompany_ID();//use this to determine if company already exists


                if(returnedResponse.trim().equals("company_exists")){
                    displayDialog("Company already exists","This company is already in our database");
                } else  if(returnedResponse.trim().equals("value_missing")){
                    displayDialog("Value missing","One or more values needed to create the company don't exist");
                } else  if(returnedResponse.trim().equals("sql_error")){
                    displayDialog("Database error","Sorry, our database is experiencing problems, please try again later");
                } else{
                    displayDialog("Company successfully added", "Company has been added to database");
                }
                mMap.clear();
                getAllCompanies();
            }

            @Override
            public void onFailure(Call<Company> call, Throwable t) {
                call.cancel();
                Toast.makeText(MainActivity.this, "Please check your network connection and internet permission"+t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayDialog(String dialogTitle, String dialogText){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(dialogTitle)
                .setMessage(dialogText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void getAllCompanies(){
        ApiInterface mApiService = this.getInterfaceService();
        Call<List<Company>> mService = mApiService.getAllCompanies();
        mService.enqueue(new Callback<List<Company>>() {
            @Override
            public void onResponse(Call<List<Company>> call, Response<List<Company>> response) {
                List<Company> companyData = response.body();
                for (Company com:companyData
                     ) {
                    LatLng companyLatLng = new LatLng(com.getLat(), com.getLng());
                    Marker tempMarker = mMap.addMarker(new MarkerOptions()
                            .position(companyLatLng)
                            .title(com.getCompany_name())
                             .snippet("Rating: "+com.getAvg_rating()+"\n\n"+com.getAddress()));
                    tempMarker.setTag(com.getCompany_ID());
                }

            }

            @Override
            public void onFailure(Call<List<Company>> call, Throwable t) {
                call.cancel();
                Toast.makeText(MainActivity.this, "Please check your network connection and internet permission"+t, Toast.LENGTH_LONG).show();
            }
        });
    }



}
