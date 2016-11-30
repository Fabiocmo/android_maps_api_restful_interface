package com.example.phil.mapsrestfulinterfaceexample;

import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Phil on 20-Nov-16.
 */

public class InputValidator {

    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String username;


    public int loginValidation(String mEmail, String mPassword) {
        this.email = mEmail;
        this.password = mPassword;
        int error = 0;
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            error = 1;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            error = 2;
        } else if (!isEmailValid(email)) {
          error = 3;
        }
        return error;
    }

    public int registrationValidation(String mEmail, String mPassword, String mFirstName, String mLastName, String mUsername) {
        this.email = mEmail;
        this.password = mPassword;
        this.firstName = mFirstName;
        this.lastName = mLastName;
        this.username = mUsername;
        int error = 0;
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            error = 1;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            error = 2;
        } else if (!isEmailValid(email)) {
            error = 3;
        } else if (TextUtils.isEmpty(firstName)) {
            error = 4;
        }else if (TextUtils.isEmpty(lastName)) {
            error = 5;
        }else if (TextUtils.isEmpty(username)) {
            error = 6;
        }
        return error;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }
}
