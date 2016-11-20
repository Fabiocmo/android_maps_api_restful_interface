package com.example.phil.mapsrestfulinterfaceexample;

import android.support.v4.app.FragmentActivity;

/**
 * Created by Phil on 19-Nov-16.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    public static final String BASE_URL = "http://192.168.1.14";
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private TextView failedLoginMessage;
    View focusView = null;
    private String email;
    private String password;
    private InputValidator inputValidator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //// TODO: 20-Nov-16 : Check if user details are in shared prefs, if they are then log in automatically (if auto-login in shared prefs enabled) 
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        failedLoginMessage = (TextView)findViewById(R.id.failed_login);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        Button registrationButton = (Button)findViewById(R.id.registration_button);

        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                failedLoginMessage.setText("");
                attemptLogin();
            }
        });

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(registerIntent);

            }
        });

        inputValidator = new InputValidator();
    }

    private void attemptLogin(){
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
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
        }
        if (mCancel>0) {
            focusView.requestFocus();
        } else {
            loginProcessWithRetrofit(email, password);
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

    private void loginProcessWithRetrofit(final String email, String password){
        ApiInterface mApiService = this.getInterfaceService();
        Call<Login> mService = mApiService.tryLogin(email, password);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login mLoginObject = response.body();
                String returnedResponse = mLoginObject.getId();//use this to determine if user exists or if login details are correct
                Toast.makeText(LoginActivity.this, "Returned " + returnedResponse, Toast.LENGTH_LONG).show();
                //showProgress(false);

                if(returnedResponse.trim().equals("bad_email")){
                    // use the registration button to register
                    failedLoginMessage.setText(getResources().getString(R.string.registration_message));
                    mPasswordView.requestFocus();
                }else if(returnedResponse.trim().equals("bad_password")){
                    // use the registration button to register
                    failedLoginMessage.setText(getResources().getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }else if(returnedResponse.trim().equals("bad_sql")){
                    // use the registration button to register
                    failedLoginMessage.setText(getResources().getString(R.string.database_error));
                    mPasswordView.requestFocus();
                }else{ //if the email and password are good...
                    // redirect to Main Activity page
                    //TODO: store user details in shared prefs, along with auto login
                    Intent loginIntent = new Intent(LoginActivity.this, MapsActivity.class);
                    loginIntent.putExtra("EMAIL", email);
                    startActivity(loginIntent);
                }
            }
            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                Toast.makeText(LoginActivity.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
            }
        });
    }

}