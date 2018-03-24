package edu.jsloan3uwyo.lokkal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Replace back to not assuming access to location sharing activity
                showMap(); //for now we will just assume access is granted
                //attemptLogin();
            }
        });
        Button mEmailRegisterButton = (Button) findViewById(R.id.register_button);
        mEmailRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //Layout inflater for displaying the register screen in the Alert Dialog
                LayoutInflater li = LayoutInflater.from(view.getContext());
                View promptsView = li.inflate(R.layout.register_screen, null);
                //Settings for the Alert Dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Register New Account");
                builder.setView(promptsView);
                builder.setPositiveButton("Register",null);
                builder.setNegativeButton(android.R.string.cancel, null);
                builder.create();
                //Having to create a new one so we can set the OnShowListener
                final AlertDialog mAlertDialog = builder.create();
                //Each TextView/DatePicker on the alert dialog page
                final TextView firstName_tv = (TextView) promptsView.findViewById(R.id.regFirstName);
                final TextView lastName_tv = (TextView) promptsView.findViewById(R.id.regLastName);
                final DatePicker dateOfBirth_dp = (DatePicker) promptsView.findViewById(R.id.regDateOfBirth);
                final TextView email_tv = (TextView) promptsView.findViewById(R.id.regEmail);
                final TextView password_tv = (TextView) promptsView.findViewById(R.id.regEnterPassword);
                final TextView cpassword_tv = (TextView) promptsView.findViewById(R.id.regConfirmPassword);
                //Setting the max value of the date picker to today's date
                dateOfBirth_dp.setMaxDate(new Date().getTime());
                //OnShowListener prevents the Alert Dialog from closing if the Register button is clicked
                mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Keeps track of whether a valid registration is occurring.
                                boolean registerAccount = true;
                                //Gets text from each input field
                                String firstName = firstName_tv.getText().toString();
                                String lastName = lastName_tv.getText().toString();
                                String regEmail = email_tv.getText().toString();
                                String regPassword = password_tv.getText().toString();
                                String confirmPassword = cpassword_tv.getText().toString();
                                //Used for moving the focus back to a specific textview if it missing data
                                View focusview = null;
                                //Makes sure the FirstName textview has a value
                                if(TextUtils.isEmpty(firstName))
                                {
                                    Toast.makeText(LoginActivity.this, "Need to enter a first name!", Toast.LENGTH_SHORT).show();
                                    focusview = firstName_tv;
                                    registerAccount = false;
                                }
                                //Makes sure the LastName textview has a value
                                else if(TextUtils.isEmpty(lastName))
                                {
                                    Toast.makeText(LoginActivity.this, "Need to enter a last name!", Toast.LENGTH_SHORT).show();
                                    focusview = lastName_tv;
                                    registerAccount = false;
                                }
                                //Makes sure the RegEmail textview has a value and it contains a '@' sign.
                                else if(TextUtils.isEmpty(regEmail) || !regEmail.contains("@"))
                                {
                                    Toast.makeText(LoginActivity.this, "Need to enter a valid email!", Toast.LENGTH_SHORT).show();
                                    focusview = email_tv;
                                    registerAccount = false;
                                }
                                //Makes sure the RegEnterPassword textview has a value and it's lenght is greater than 5
                                else if(TextUtils.isEmpty(regPassword) || regPassword.length() <= 5)
                                {
                                    Toast.makeText(LoginActivity.this, "Need to enter a valid password!", Toast.LENGTH_SHORT).show();
                                    focusview = password_tv;
                                    registerAccount = false;
                                }
                                //Makes sure the RegConfirmPassword textview is not empty and that is matches the first password
                                else if(TextUtils.isEmpty(confirmPassword) || !confirmPassword.equals(regPassword))
                                {
                                    Toast.makeText(LoginActivity.this, "Passwords needs to match!", Toast.LENGTH_SHORT).show();
                                    focusview = cpassword_tv;
                                    registerAccount = false;
                                }
                                //If no errors, create the account and dismiss the alert dialog
                                if(registerAccount)
                                {
                                    /*
                                            Send Account Information to database goes here
                                     */
                                    mAlertDialog.dismiss();
                                }
                                //Else, set the focus on the textview that had an error associated with it.
                                else
                                {
                                    focusview.requestFocus();
                                }
                            }
                        });
                    }
                });
                mAlertDialog.show();

            }
        });

    }

    private void showMap() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}

