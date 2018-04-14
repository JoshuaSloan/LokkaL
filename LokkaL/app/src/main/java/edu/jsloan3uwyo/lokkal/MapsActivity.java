package edu.jsloan3uwyo.lokkal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, AddFriendFragment.OnFragmentInteractionListener {


    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Marker previousMarker = null;

    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerlayout;
    private NavigationView navView;

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

<<<<<<< HEAD
=======
        //Toast.makeText(getApplicationContext(),acc.FirstName + " " +acc.LastName, Toast.LENGTH_LONG).show();
>>>>>>> origin/master
        /////////////////////////////////////////////////////////////////////////////////////////

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
                    Toast.makeText(getApplicationContext(), "Go to group management", Toast.LENGTH_LONG).show();
                    drawerlayout.closeDrawers();
                    return true;
                } else if (id == R.id.group_requests) {
                    Toast.makeText(getApplicationContext(), "Go to group requests", Toast.LENGTH_LONG).show();
                    drawerlayout.closeDrawers();
                    return true;
                } else if (id == R.id.create_group) {
                    Toast.makeText(getApplicationContext(), "Create New Group Fragment", Toast.LENGTH_LONG).show();
                    drawerlayout.closeDrawers();
                    return true;
                } else if (id == R.id.friends) {
                    Toast.makeText(getApplicationContext(), "Friends Fragment", Toast.LENGTH_LONG).show();
                    drawerlayout.closeDrawers();
                    return true;
                } else if (id == R.id.friend_requests) {
                    Toast.makeText(getApplicationContext(), "Friend Request Fragment", Toast.LENGTH_LONG).show();
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
            /*getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment())
                    .addToBackStack("settings")
                    .commit();
                    */
            Toast.makeText(getApplicationContext(), "Go to settings fragment", Toast.LENGTH_LONG).show();
            return true;

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
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
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
                Log.i("update", "Last location updated sucessfully.");
                updateMap();
            }
        };
    }

    public void updateMap() {
        // remove old markers
        mMap.clear();

        // create latlng for marker position
        LatLng mLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        // create marker with title of the latlng and bitmap as the icon
        final Marker mMarker = mMap.addMarker(new MarkerOptions().position(mLatLng).title(mLastLocation.getLatitude() + ", " + mLastLocation.getLongitude()));

        // move the map over the new marker
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));

        mMap.setMinZoomPreference(15);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {}
}