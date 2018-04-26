package edu.jsloan3uwyo.lokkal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, AddFriendFragment.OnFragmentInteractionListener, FriendRequestFragment.OnListFragmentInteractionListener,
        FriendFragment.OnListFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener, GroupRequestFragment.OnListFragmentInteractionListener,
    CreateGroupFragment.OnFragmentInteractionListener, SharedPreferences.OnSharedPreferenceChangeListener, GroupFragment.OnListFragmentInteractionListener{

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Marker previousMarker = null;

    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerlayout;
    private NavigationView navView;

    String updateInterval;

    Person acc;

    Location mLastLocation; //TODO need to possibly make this local and pass it instead
    LatLng lastLocationCoordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        acc = (Person) getIntent().getSerializableExtra("Person");

        URI localuri = null;
        MapsActivity myData;
        try {
            localuri = new URI("http://www.cs.uwyo.edu/~kfenster/query_mygroup.php");
            new queryMyG().execute(new sendToDatabase(localuri,acc.PersonID));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //create the main toolbar
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setLogo(R.drawable.lokkal1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        //getSupportActionBar().setTitle(R.string.app_name);

        //create the drawer
        drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //TODO: Remove the shadow from the last selected item in navigation drawer
        //drawerlayout.setScrimColor(Color.TRANSPARENT);
        //drawerlayout.setScrimColor(getResources().getColor(android.R.color.transparent));
        drawerToggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerlayout.setDrawerListener(drawerToggle);

        navView = (NavigationView) findViewById(R.id.navview);

        //access the menu item options
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.group_management) {
                    Bundle args = new Bundle();
                    args.putSerializable("Person",acc);
                    GroupFragment groupFragment = new GroupFragment();
                    groupFragment.setArguments(args);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.map, groupFragment);
                    transaction.commit();
                    drawerlayout.closeDrawers();
                    return true;
                } else if (id == R.id.group_requests) {
                    //Toast.makeText(getApplicationContext(), "Go to group requests", Toast.LENGTH_LONG).show();
                    Bundle args = new Bundle();
                    args.putSerializable("Person",acc);
                    GroupRequestFragment groupRequestFragment = new GroupRequestFragment();
                    groupRequestFragment.setArguments(args);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.map, groupRequestFragment);
                    transaction.commit();
                    drawerlayout.closeDrawers();
                    return true;
                } else if (id == R.id.create_group) {
                    Bundle args = new Bundle();
                    args.putSerializable("Person",acc);
                    CreateGroupFragment  createGroupFragment = new CreateGroupFragment();
                    createGroupFragment.setArguments(args);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.map, createGroupFragment);
                    transaction.commit();

                    drawerlayout.closeDrawers();
                    return true;
                } else if (id == R.id.friends) {
                    //Toast.makeText(getApplicationContext(), "Friends Fragment", Toast.LENGTH_LONG).show();
                    Bundle args = new Bundle();
                    args.putSerializable("Person",acc);
                    FriendFragment friendFragment = new FriendFragment();
                    friendFragment.setArguments(args);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.map, friendFragment);
                    transaction.commit();
                    drawerlayout.closeDrawers();
                    return true;
                } else if (id == R.id.friend_requests) {
                    //Toast.makeText(getApplicationContext(), "Friend Request Fragment", Toast.LENGTH_LONG).show();
                    Bundle args = new Bundle();
                    args.putSerializable("Person",acc);
                    FriendRequestFragment friendRequestFragment = new FriendRequestFragment();
                    friendRequestFragment.setArguments(args);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    transaction.addToBackStack(null);
                    transaction.replace(R.id.map, friendRequestFragment);
                    transaction.commit();
                    drawerlayout.closeDrawers();
                    return true;
                } else if (id == R.id.add_friend) {
                    //Toast.makeText(getApplicationContext(), "Add Friend Fragment", Toast.LENGTH_LONG).show();
                    Bundle args = new Bundle();
                    args.putSerializable("Person",acc);
                    AddFriendFragment addFriendFragment = new AddFriendFragment();
                    addFriendFragment.setArguments(args);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.map, addFriendFragment);
                    transaction.commit();

                    drawerlayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //TODO: Remove once permissions are fixed in the main login activity
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        }

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                //mLocationRequest = LocationRequest.create();
                createLocationRequest();
                createLocationCallback();
                //mLocationCallback = new LocationCallback();
                startLocationUpdates();
                //mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    // TODO: fix this properly (i.e. don't just comment it out for the sake of compilation)
                    /*try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MapsActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                    */
                }
            }
        });

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            mLastLocation = location;
                            Log.i("update", "Last location updated sucessfully.");
                            Log.i("update", String.valueOf(location));
                            updateMap();
                        }
                    }
                });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    //drop down items in the action bar, only settings currently, but more can easily be added (ReadMe?)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // Display the fragment as the main content.
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment())
                    .addToBackStack("settings")
                    .commit();
            //Toast.makeText(getApplicationContext(), "Go to settings fragment", Toast.LENGTH_LONG).show();
            return true;

        }
        else if (id == R.id.sign_out)
        {
            this.finish();
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mLastLocation != null) {
            //mMap.addMarker(new MarkerOptions().position(lastLocationCoordinates).title("Me"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(lastLocationCoordinates));
        }
    }

    protected void createLocationRequest() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        mLocationRequest = new LocationRequest();
        //mLocationRequest.setInterval(10000);
        if (sharedPref.getString("pref_update_speed","2").equals("1"))
        {
            mLocationRequest.setInterval(5000);
            Log.i("speed", "5 seconds");
        }
        else if (sharedPref.getString("pref_update_speed","2").equals("2"))
        {
            mLocationRequest.setInterval(10000);
            Log.i("speed", "10 seconds");
        }
        else if (sharedPref.getString("pref_update_speed","2").equals("3"))
        {
            mLocationRequest.setInterval(30000);
            Log.i("speed", "30 seconds");
        }
        else if (sharedPref.getString("pref_update_speed","2").equals("4"))
        {
            mLocationRequest.setInterval(60000);
            Log.i("speed", "1 minute");
        }
        else
        {
            mLocationRequest.setInterval(10000);
            Log.i("speed", "default: 10 seconds");
        }
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // TODO: See performance hint available at: https://developer.android.com/training/location/change-location-settings.html
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mLastLocation = locationResult.getLastLocation();
                URI localuri = null;
                MapsActivity myData;
                //Return Group Requests
                try {
                    localuri = new URI("http://www.cs.uwyo.edu/~kfenster/insert_groupmemberlocation.php");
                    new insertGML().execute(new myLocation(localuri, acc.myGroupMemberID, mLastLocation.getLatitude(), mLastLocation.getLongitude()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                Log.i("update", "Last location updated sucessfully.");
                updateMap();
            }
        };
    }

    public void updateMap() {
        // remove old markers
        mMap.clear();
        if(acc.myGroup !=  null) {
            URI localuri = null;
            MapsActivity myData;
            //Return Friends
            try {
                acc.myGroup.logm.clear();
                localuri = new URI("http://www.cs.uwyo.edu/~kfenster/query_groupmembers.php");
                new queryGM().execute(new myLocation(localuri, acc.myGroup.GroupID));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        else {
            // create latlng for marker position
            LatLng mLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            // create marker with title of the latlng and bitmap as the icon
            final Marker mMarker = mMap.addMarker(new MarkerOptions().position(mLatLng).title(mLastLocation.getLatitude() + ", " + mLastLocation.getLongitude()));

            // move the map over the new marker
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));

            mMap.setMinZoomPreference(15);
        }
    }
    @Override
    public void onFragmentInteraction(Uri uri) {}


    @Override
    public void onListFragmentInteraction(FriendRequest fr) {

    }

    @Override
    public void onListFragmentInteraction(Friend f) {

    }

    @Override
    public void onListFragmentInteraction(GroupRequest gr) {

    }

    @Override
    public void onListFragmentInteraction(GroupMember item) {

    }

    class sendToDatabase {
        URI uri;
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

        //Find My Group
        sendToDatabase(URI myuri, int pid) {
            uri = myuri;
            HashMap<String, String> hmap = new HashMap<String, String>();
            hmap.put("PersonID", String.valueOf(pid));
            try {
                data = getPostDataString(hmap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }

    class myLocation {
        URI uri;
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

        //Insert My Location Into the Database
        myLocation(URI myuri, int gmid, double lat, double lon) {
            uri = myuri;
            HashMap<String, String> hmap = new HashMap<String, String>();
            hmap.put("GroupMemberID", String.valueOf(gmid));
            hmap.put("Latitude", String.valueOf(lat));
            hmap.put("Longitude", String.valueOf(lon));
            try {
                data = getPostDataString(hmap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        myLocation(URI myuri, int gid) {
            uri = myuri;
            HashMap<String, String> hmap = new HashMap<String, String>();
            hmap.put("GroupID", String.valueOf(gid));
            try {
                data = getPostDataString(hmap);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }

    private class queryMyG extends AsyncTask<sendToDatabase, String, String> {

        @Override
        protected String doInBackground(sendToDatabase... params) {
            try {
                //setup the url
                URL url = params[0].uri.toURL();
                Log.wtf("network", url.toString());
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
                Log.wtf("Response Code", String.valueOf(responseCode));
                Log.wtf("Message", con.getResponseMessage());
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
                    if (response == "") {
                        Log.wtf("QUERY", "Line was empty");
                        response = "0";
                    }
                } else
                    response = "0";
                Log.wtf("RESPONSE", response);
                if(response.compareTo("0") != 0)
                {
                    onProgressUpdate(response);
                }
                return response;
            } catch (Exception e) {
                // failure of some kind.  uncomment the stacktrace to see what happened if it is
                // permit error.
                e.printStackTrace();
                return "0";
            }

        }
        protected void onProgressUpdate(String... progress) {
            //build the data structure as we go.
            try
            {
                String parts[] = progress[0].split(",");
                acc.setGroup(Integer.valueOf(parts[0]), parts[1]);
                acc.myGroupMemberID = Integer.valueOf(parts[2]);
                Log.v("GroupID", String.valueOf(acc.myGroup.GroupID));
                Log.v("GroupName", acc.myGroup.GroupName);
                Log.v("GroupMemberID ", String.valueOf(acc.myGroupMemberID));
            }
            catch(Exception e) {
                Log.v("donetwork", "Error line: onProgressUpdate");
                Log.v("Error", e.getMessage());
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() != 0) //checks if settings fragment is open
        {
            getFragmentManager().popBackStackImmediate();

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            //createLocationRequest(); //TODO: check about updating the speed?
        }
        clearStack();
    }

    public void clearStack() {
        //Here we are clearing back stack fragment entries
        int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    }

    private class insertGML extends AsyncTask<myLocation, String, String> {

        //how to write the parameters via a post method were used from here:
        //http://stackoverflow.com/questions/29536233/deprecated-http-classes-android-lollipop-5-1

        @Override
        protected String doInBackground(myLocation... params) {
            try {
                //setup the url
                URL url = params[0].uri.toURL();
                Log.wtf("network", url.toString());
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
                Log.wtf("Response Code", String.valueOf(responseCode));
                Log.wtf("Message", con.getResponseMessage());
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
                    if (response == "") {
                        Log.wtf("QUERY", "Line was empty");
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
    }
    private class queryGM extends AsyncTask<myLocation, String, List<String>> {

        @Override
        protected List<String> doInBackground(myLocation... params) {
            try {
                //setup the url
                URL url = params[0].uri.toURL();
                Log.wtf("network", url.toString());
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
                Log.wtf("Response Code", String.valueOf(responseCode));
                Log.wtf("Message", con.getResponseMessage());
                List<String> los = new ArrayList<String>();
                //the return is a single number, so simple to read like this:
                //note the while loop should not be necessary, but just in case.
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        Log.wtf("LINE", line);
                        if(line.compareTo("") != 0)
                        {
                            los.add(line);
                            Log.v("LOS", String.valueOf(los.size()));
                        }

                    }

                }
                Log.v("LOS - After loop", String.valueOf(los.size()));
                onProgressUpdate(los);
                return los;
            } catch (Exception e) {
                // failure of some kind.  uncomment the stacktrace to see what happened if it is
                // permit error.
                e.printStackTrace();
                return new ArrayList<String>();
            }

        }
        protected void onProgressUpdate(List<String> progress) {
            //build the data structure as we go.
            try {
                Log.v("Progress", String.valueOf(progress.size()));

                //Splits results by CSV values
                for(int i = 0; i < progress.size(); i++)
                {
                    String parts[] = progress.get(i).split(",");
                    acc.myGroup.logm.add(new GroupMember(Integer.valueOf(parts[0]), parts[1], Double.valueOf(parts[2]), Double.valueOf(parts[3])));
                    //Log.v("OPU", parts[0] + parts[1] + parts[2]);
                    Log.v("Output:", parts[0] + parts[1] + parts[2] + parts[3]);
                }
            }
            catch(Exception e) {
                Log.v("donetwork", "Error line: onProgressUpdate");
                Log.v("Error", e.getMessage());
            }
        }
        protected void onPostExecute(List<String> result) {
            //Calls this at the end of the Async Task
            updateGroupLocation();  //data has been added/removed, update the recyclerview.
        }
    }
    public void updateGroupLocation()
    {
        mMap.clear();
        for(int i =0; i < acc.myGroup.logm.size(); i++) {
            LatLng mLatLng = new LatLng(acc.myGroup.logm.get(i).Latitude, acc.myGroup.logm.get(i).Longitude);

            // create marker with title of the latlng and bitmap as the icon
            final Marker mMarker = mMap.addMarker(new MarkerOptions().position(mLatLng).title(acc.myGroup.logm.get(i).GroupMemberName));
        }
        // move the map over the new marker
        LatLng moveCamera = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(moveCamera));

        mMap.setMinZoomPreference(15);
    }

}