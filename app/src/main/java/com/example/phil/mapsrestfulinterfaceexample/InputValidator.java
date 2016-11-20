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


    public int loginValidation(String email, String password) {
        this.email = email;
        this.password = password;
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

    public int registrationValidation(String email, String password, String firstName, String lastName, String username) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
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
        return true;//email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        return true;// password.length() > 6;
    }
}
