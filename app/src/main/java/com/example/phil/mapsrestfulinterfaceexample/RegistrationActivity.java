package com.example.phil.mapsrestfulinterfaceexample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phil.mapsrestfulinterfaceexample.POJO.Login;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Phil on 20-Nov-16.
 */

public class RegistrationActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://192.168.1.14";
    // UI references.
    private EditText mEmailView;
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private EditText mUsernameView;
    private EditText mPasswordView;
    private TextView failedRegisterMessage;
    View focusView = null;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private InputValidator inputValidator;
    private SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.Remail);
        failedRegisterMessage = (TextView)findViewById(R.id.failed_register);
        mPasswordView = (EditText) findViewById(R.id.Rpassword);
        mFirstNameView = (EditText) findViewById(R.id.Rfirst_name);
        mLastNameView = (EditText) findViewById(R.id.Rlast_name);
        mUsernameView = (EditText) findViewById(R.id.Rusername);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        Button registrationButton = (Button)findViewById(R.id.Rregistration_button);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegistration();
            }
        });
        inputValidator = new InputValidator();
    }

    private void attemptRegistration(){
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        firstName = mFirstNameView.getText().toString();
        lastName = mLastNameView.getText().toString();
        username = mUsernameView.getText().toString();
        int mCancel = inputValidator.loginValidation(email, password);
        if (mCancel ==1) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
        } else if (mCancel ==2 ) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
        } else if (mCancel ==3 ) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
        }else if (mCancel ==4 ) {
            mFirstNameView.setError(getString(R.string.error_invalid_first_name));
            focusView = mFirstNameView;
        }else if (mCancel ==5 ) {
            mLastNameView.setError(getString(R.string.error_invalid_last_name));
            focusView = mLastNameView;
        }else if (mCancel ==6 ) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
        }
        if (mCancel>0) {
            focusView.requestFocus();
        } else {
            RegistrationProcessWithRetrofit(email, password, firstName, lastName, username);
        }
    }


    private ApiInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiInterface mInterfaceService = retrofit.create(ApiInterface.class);
        return mInterfaceService;
    }

    private void RegistrationProcessWithRetrofit(final String email, String password, String firstName, String lastName, String username){
        ApiInterface mApiService = this.getInterfaceService();
        Call<Login> mService = mApiService.register(email,firstName, lastName, username, password);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login mLoginObject = response.body();
                String returnedResponse = mLoginObject.getId();//use this to determine if user exists or if login details are correct
                Toast.makeText(RegistrationActivity.this, "Returned " + returnedResponse, Toast.LENGTH_LONG).show();
                //showProgress(false);

                if(returnedResponse.trim().equals("username_exists")){
                    // use the registration button to register
                    failedRegisterMessage.setText(getResources().getString(R.string.registration_message));
                    mPasswordView.requestFocus();
                }else if(returnedResponse.trim().equals("email_exists")){
                    // use the registration button to register
                    failedRegisterMessage.setText(getResources().getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }else if(returnedResponse.trim().equals("error")){
                    // use the registration button to register
                    failedRegisterMessage.setText(getResources().getString(R.string.database_error));
                    mPasswordView.requestFocus();
                }else if(returnedResponse.trim().equals("values_missing")){
                    // use the registration button to register
                    failedRegisterMessage.setText(getResources().getString(R.string.database_error));
                    mPasswordView.requestFocus();
                }else{ //if the email and password are good...
                    // redirect to Main Activity page
                    prefs =  getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                }
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("email", mLoginObject.getEmail());
                editor.putString("password", mLoginObject.getPassword());
                editor.putBoolean("autoLogin", true);
                editor.commit();
                    Intent registeredIntent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(registeredIntent);
                }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                Toast.makeText(RegistrationActivity.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
            }
        });
    }

}