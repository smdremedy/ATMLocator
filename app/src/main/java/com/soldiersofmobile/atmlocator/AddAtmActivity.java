package com.soldiersofmobile.atmlocator;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;


public class AddAtmActivity extends ActionBarActivity
        implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener{

    public static final String LOG_TAG = "TAG";
    public static final int RESOLUTION_REQUEST_CODE = 1234;
    private static final int REQUEST_PLACE_PICKER = 4321;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private CharSequence name;
    private CharSequence address;
    private CharSequence phone;
    private LatLng latLng;
    private Spinner spinner;
    private TextView latTextView;
    private TextView longTextView;
    private TextView addressTextView;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_atm);

        spinner = (Spinner) findViewById(R.id.banksSpinner);
        latTextView = (TextView) findViewById(R.id.latTextView);
        longTextView = (TextView) findViewById(R.id.longTextView);
        addressTextView = (TextView) findViewById(R.id.addressTextView);

        ArrayAdapter<Bank> bankArrayAdapter = new ArrayAdapter<Bank>(this, android.R.layout.simple_list_item_1);


        dbHelper = ((App) getApplication()).getDbHelper();
        try {
            Dao<Bank, String> banksDao =
                    dbHelper.getDao(Bank.class);
            List<Bank> banks = banksDao.queryForAll();
            for (Bank bank : banks) {
                bankArrayAdapter.add(bank);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        spinner.setAdapter(bankArrayAdapter);

        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build();

        locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1000);

        findViewById(R.id.pickLocationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(AddAtmActivity.this);
                    // Start the Intent by requesting a result, identified by a request code.
                    startActivityForResult(intent, REQUEST_PLACE_PICKER);

                    // Hide the pick option in the UI to prevent users from starting the picker
                    // multiple times.
                    // showPickAction(false);

                } catch (GooglePlayServicesRepairableException e) {
                    GooglePlayServicesUtil
                            .getErrorDialog(e.getConnectionStatusCode(), AddAtmActivity.this, 0);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(AddAtmActivity.this, "Google Play Services is not available.",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        findViewById(R.id.addAtmButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Atm atm = new Atm();
                atm.setBank((Bank) spinner.getSelectedItem());
                atm.setAddress(address.toString());
                atm.setLatitiude(latLng.latitude);
                atm.setLongitude(latLng.longitude);

                try {
                    Dao<Atm, Long> atmLongDao = dbHelper.getDao(Atm.class);
                    atmLongDao.create(atm);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                setResult(RESULT_OK);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // BEGIN_INCLUDE(activity_result)
        if (requestCode == REQUEST_PLACE_PICKER) {
            // This result is from the PlacePicker dialog.

            // Enable the picker option
            //showPickAction(true);

            if (resultCode == Activity.RESULT_OK) {
                /* User has picked a place, extract data.
                   Data is extracted from the returned intent by retrieving a Place object from
                   the PlacePicker.
                 */
                final Place place = PlacePicker.getPlace(data, this);

                /* A Place object contains details about that place, such as its name, address
                and phone number. Extract the name, address, phone number, place ID and place types.
                 */

                latLng = place.getLatLng();
                name = place.getName();
                address = place.getAddress();
                phone = place.getPhoneNumber();

                latTextView.setText(String.valueOf(latLng.latitude));
                longTextView.setText(String.valueOf(latLng.longitude));
                addressTextView.setText(String.valueOf(address));


                final String placeId = place.getId();
                String attribution = PlacePicker.getAttributions(data);
                if(attribution == null){
                    attribution = "";
                }



            } else {
                // User has not selected a place, hide the card.
                //getCardStream().hideCard(CARD_DETAIL);
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        // END_INCLUDE(activity_result)
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_atm, menu);
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

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if(connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, RESOLUTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.d(LOG_TAG, "Connected");
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(lastLocation != null) {
            handleLocation(lastLocation);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

    }

    private void handleLocation(Location lastLocation) {
        Log.d(LOG_TAG, "Last location:" + lastLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

    }

    @Override
    public void onLocationChanged(Location location) {
        handleLocation(location);
    }
}
