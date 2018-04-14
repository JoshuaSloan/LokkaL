package edu.jsloan3uwyo.lokkal;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    URI uri;
    ArrayList<Person> list = null;
    EditText email;
    EditText pass;
    Person acc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        acc = new Person();
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: Replace back to not assuming access to location sharing activity
                try {
                    uri = new URI("http://www.cs.uwyo.edu/~kfenster/query_signin.php");
                    Log.v("PARAMS", email.getText().toString() + pass.getText().toString());
                    //Queries the database for a person record with the email/passsword.
                    new doRest().execute(new myDataAsync(uri,email.getText().toString(), pass.getText().toString()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
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
                final EditText firstName_tv = (EditText) promptsView.findViewById(R.id.regFirstName);
                final EditText lastName_tv = (EditText) promptsView.findViewById(R.id.regLastName);
                final DatePicker dateOfBirth_dp = (DatePicker) promptsView.findViewById(R.id.regDateOfBirth);
                final EditText email_tv = (EditText) promptsView.findViewById(R.id.regEmail);
                final EditText password_tv = (EditText) promptsView.findViewById(R.id.regEnterPassword);
                final EditText cpassword_tv = (EditText) promptsView.findViewById(R.id.regConfirmPassword);
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
                                String dateOfBirth = String.valueOf(dateOfBirth_dp.getYear()) + '-' + String.valueOf(dateOfBirth_dp.getMonth() + 1) + '-' + String.valueOf(dateOfBirth_dp.getDayOfMonth());
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
                                    URI localuri = null;
                                    myDataAsync myData;
                                    try {
                                        localuri = new URI("http://www.cs.uwyo.edu/~kfenster/insert_registration.php");
                                        boolean update = false;
                                        Log.v("SERVER", "Accessed insert_registration.php");
                                        new doRest().execute(new myDataAsync(localuri, firstName, lastName, dateOfBirth,regEmail, regPassword));
                                        Toast.makeText(LoginActivity.this, "Account has been registered!", Toast.LENGTH_SHORT).show();
                                    } catch (URISyntaxException e) {
                                        e.printStackTrace();
                                    }
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
        intent.putExtra("Person", (Serializable) acc);

        startActivity(intent);
    }
    //Ward's Code
    //help class to send all the data to the async task.  mostly because setting up all the
    //post data is just a pain in the but now.
    //This is what we pass parameters to for the account registration and checking for sign in
    class myDataAsync {
        URI uri;
        boolean update;
        String data;
        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }
        //constructor to create the data structure.
        //Account Registration Constructor
        myDataAsync(URI myuri, String fn, String ln, String dob, String em, String p) {
            uri = myuri;
            HashMap<String, String> hmap = new HashMap<String, String>();
            hmap.put("FirstName", fn);
            hmap.put("LastName", ln);
            hmap.put("DateOfBirth", dob);
            hmap.put("Email", em);
            hmap.put("Password", p);
            try {
                data = getPostDataString(hmap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        //Sign-In Constructor
        myDataAsync(URI myuri, String em, String p) {
            this.update = update;
            uri = myuri;
            HashMap<String, String> hmap = new HashMap<String, String>();
            hmap.put("Email", em);
            hmap.put("Password", p);
            try {
                data = getPostDataString(hmap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }
    //Checks to see if they query returned anything
    public void doDataUpdate() {
        //Record was found!
        if(acc.PersonID != -1)
        {
            showMap();
        }
        //Else Email/Password was not valid
        else
        {
            Toast.makeText(this, "Invalid Email/Password!", Toast.LENGTH_SHORT).show();
        }
    }
    /*
     *this asynctask is passing parameters via post to the rest service.
     * The post parameters are setup correctly in the dataAsync method that is
     * passed to the task.  It then open the connection, passes the parameters, authenicates
     * and toasts the return value.
     */
    //Code For Receiving Data From Query
    private class doRest extends AsyncTask<myDataAsync, String, String> {

        //how to write the parameters via a post method were used from here:
        //http://stackoverflow.com/questions/29536233/deprecated-http-classes-android-lollipop-5-1

        @Override
        protected String doInBackground(myDataAsync... params) {
            try {
                //setup the url
                URL url = params[0].uri.toURL();
                Log.wtf("network", url.toString() );
                //make the connection
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                //setup as post method and write out the parameters.
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(params[0].data);
                writer.flush();
                writer.close();
                os.close();

                //get the response code (ie success 200 or something else
                int responseCode = con.getResponseCode();
                Log.wtf("Response", String.valueOf(responseCode));
                String response = "";
                //the return is a single number, so simple to read like this:
                //note the while loop should not be necessary, but just in case.
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        Log.wtf("LINE", line);
                        response += line;

                    }
                    if(response == "")
                    {
                        Log.wtf("QUERY", "Line was empty?");
                        response = "0";
                    }
                } else
                    response = "0";
                Log.wtf("RESPONSE", response);
                onProgressUpdate(response);
                return response;
            } catch (Exception e) {
                // failure of some kind.  uncomment the stacktrace to see what happened if it is
                // permit error.
                e.printStackTrace();
                return "0";
            }

        }
        /*
         * build the data structure.
         */
        protected void onProgressUpdate(String... progress) {
            //build the data structure as we go.
            Log.wtf("Progress", progress[0]);
            try {
                //Splits results by CSV values
                String parts[] = progress[0].split(",");
                Log.wtf("PersonID", parts[0]);
                //Sets the acc varaible to be the information returned by the query
                acc = new Person(Integer.valueOf(parts[0]), parts[1], parts[2], parts[3],parts[4],parts[5]);
            } catch (Exception e) {
                Log.v("donetwork", "Error line: " + progress[0]);
            }
        }
        protected void onPostExecute(String result) {
            //Calls this at the end of the Async Task
            doDataUpdate();  //data has been added/removed, update the recyclerview.
        }
    }
    //End Of Ward's Code
}

