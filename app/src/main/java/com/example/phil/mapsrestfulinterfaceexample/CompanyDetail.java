package com.example.phil.mapsrestfulinterfaceexample;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phil.mapsrestfulinterfaceexample.POJO.Company;
import com.example.phil.mapsrestfulinterfaceexample.POJO.Review;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.string.copy;
import static com.example.phil.mapsrestfulinterfaceexample.LoginActivity.BASE_URL;

public class CompanyDetail extends AppCompatActivity {

    private SharedPreferences companyPrefs;
    private SharedPreferences userPrefs;
    private String company_ID;
    private String user_ID;
    public Company company;
    private ArrayList<Review> reviewsArray;
    private TextView companyAddressText;
    private RatingBar companyRatingBar;
    private  Toolbar toolbar;
    private ReviewsAdapter reviewsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_detail);
        toolbar = (Toolbar) findViewById(R.id.company_toolbar);
        setSupportActionBar(toolbar);
        companyAddressText = (TextView)findViewById(R.id.company_address);
        companyRatingBar = (RatingBar)findViewById(R.id.ratingBarCompanyDetails);

        companyPrefs = getSharedPreferences("companyDetails", Context.MODE_PRIVATE);
        company_ID= companyPrefs.getString("detailCompanyID", "");
        company = new Company();

        userPrefs = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        user_ID= userPrefs.getString("id", "");

        reviewsArray = new ArrayList<Review>();
         reviewsAdapter = new ReviewsAdapter(this, reviewsArray);
        ListView listView = (ListView) findViewById(R.id.reviews_list);
        listView.setAdapter(reviewsAdapter);

        getCompany(company_ID);
        getAllReviews(company_ID);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CompanyDetail.this);
                // Get the layout inflater
                LayoutInflater  inflater = (CompanyDetail.this).getLayoutInflater();
               View mView = inflater.inflate(R.layout.add_review, null);
                builder.setCancelable(false);
                final EditText reviewTitleText = (EditText)mView.findViewById(R.id.review_title);
                final EditText reviewBodyText = (EditText)mView.findViewById(R.id.review_body);
                final RatingBar reviewRatingBar = (RatingBar)mView.findViewById(R.id.ratingBar);
                builder.setView(mView)
                        // Add action buttons
                        .setPositiveButton("Add review", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                String reviewTitle = reviewTitleText.getText().toString();
                                String reviewBody = reviewBodyText.getText().toString();
                                float reviewRating = reviewRatingBar.getRating();

                                Log.d("feedback", ""+ reviewBody+reviewTitle+reviewRating);
                                addReview(company_ID,user_ID,reviewTitle,reviewBody,reviewRating);
                            }
            })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                });
            builder.create();
            builder.show();

            }
        });



    }

    private ApiInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiInterface mInterfaceService = retrofit.create(ApiInterface.class);
        return mInterfaceService;
    }

    private void addReview(final String company_ID, String user_ID, String title, String text_body, double star_rating){
        ApiInterface mApiService = this.getInterfaceService();
        Call<Review> mService = mApiService.addReview(company_ID, user_ID, title, text_body, star_rating);
        mService.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                Review mReviewObject = response.body();
                if (mReviewObject.getReview_ID().trim().equals("value_missing")){
                    Toast.makeText(CompanyDetail.this, "Value missing", Toast.LENGTH_LONG).show();
                } else if (mReviewObject.getReview_ID().trim().equals("insert_error")){
                    Toast.makeText(CompanyDetail.this, "Database insert error", Toast.LENGTH_LONG).show();
                } else {
                    String returnedResponse = mReviewObject.getReview_ID();//use this to determine if re already exists
                    Toast.makeText(CompanyDetail.this, "Successfully inserted" + returnedResponse, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                call.cancel();
                Toast.makeText(CompanyDetail.this, "Please check your network connection and internet permission"+t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getCompany(final String company_ID){
        ApiInterface mApiService = this.getInterfaceService();
        Call<Company> mService = mApiService.getCompany(company_ID);
        mService.enqueue(new Callback<Company>() {
            @Override
            public void onResponse(Call<Company> call, Response<Company> response) {
                Company mCompany = response.body();
                company = mCompany;

                getSupportActionBar().setTitle(company.getCompany_name());
                companyAddressText.setText( company.getAddress());
                companyRatingBar.setRating((float)company.getAvg_rating());

            }

            @Override
            public void onFailure(Call<Company> call, Throwable t) {
                call.cancel();
                Toast.makeText(CompanyDetail.this, "Please check your network connection and internet permission"+t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getAllReviews(final String company_ID){
        ApiInterface mApiService = this.getInterfaceService();
        Call<List<Review>> mService = mApiService.getAllReviewsForCompany(company_ID);
        mService.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                List<Review> mReviews = response.body();
                for (Review rev:mReviews
                        ) {
                    reviewsAdapter.add(rev);
                   Log.d("review: ",""+rev.getText_body());
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                call.cancel();
                Toast.makeText(CompanyDetail.this, "Please check your network connection and internet permission"+t, Toast.LENGTH_LONG).show();
            }
        });
    }




}
