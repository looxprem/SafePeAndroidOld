package com.safepayu.wallet.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.Profile;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.model.SuggesedAddressModel;
import com.safepayu.wallet.utils.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddressActivity extends AppCompatActivity {
    private TextView heading;
    private EditText city, state, pincode, country;
    AutoCompleteTextView location;
    private Button addbutton;
    private ProgressBar reg_progres1;
    private Boolean addressupdate;
    private CheckBox current_location;
    public String userid;
    public ProgressDialog progressDialog;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    List<SuggesedAddressModel> addressModels = new ArrayList<>();
    LoadingDialog dialog;
    ArrayAdapter<String> adapter;
    String searchedAddress[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        Button backbtn = (Button) findViewById(R.id.send_back_btn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        location = findViewById(R.id.location);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        pincode = (EditText) findViewById(R.id.pin);
        country = (EditText) findViewById(R.id.country);
        addbutton = (Button) findViewById(R.id.add_address);
        addbutton.setOnClickListener(btn_addListner);
        current_location = findViewById(R.id.current_location);
        heading = (TextView) findViewById(R.id.heading);
        reg_progres1 = findViewById(R.id.reg_progres1);
        addressupdate = false;
        Bundle extras = getIntent().getExtras();
        addressupdate = extras.getBoolean("Addressupdate");

        LoginUser user = SharedPrefManager.getInstance(this).getUser();
        userid = user.getUserid();

        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        checkLocationPermission();


        if (addressupdate) {
            heading.setText("Update Address");
            addbutton.setText("Update");

            GetAddress getAddress = new GetAddress();
            getAddress.execute();
        }

        current_location.setVisibility(View.GONE);

        current_location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && isInternetAvailable() && checkLocationPermission()) {
                    requestSingleUpdate(AddressActivity.this, new LocationCallback() {
                        @Override
                        public void onNewLocationAvailable(GPSCoordinates location) {
                            getAddress(location.latitude, location.longitude);
                        }
                    });

                } else if (isChecked && !isInternetAvailable()) {
                    current_location.setChecked(false);
                    Toast.makeText(AddressActivity.this, "No internet connected", Toast.LENGTH_SHORT).show();
                    GetAddress getAddress = new GetAddress();
                    getAddress.execute();
                } else if (isChecked && isInternetAvailable() && !checkLocationPermission()) {
                    current_location.setChecked(false);
                    Toast.makeText(AddressActivity.this, "Location permission not granted", Toast.LENGTH_SHORT).show();
                    GetAddress getAddress = new GetAddress();
                    getAddress.execute();
                } else {
                    GetAddress getAddress = new GetAddress();
                    getAddress.execute();
                }
            }
        });


        location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
//                    searchAddress(s.toString());
                }
            }
        });


        /*location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                LatLng ltln = addressModels.get(position).getLatLng();
//                getAddress(ltln.latitude,  ltln.longitude);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


    }


    private void searchAddress(String keyword) {
        List<Address> addressList = null;
        if (!TextUtils.isEmpty(keyword.toString()) && keyword.length() >= 3) {
            addressModels.clear();
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                addressList = geocoder.getFromLocationName(keyword.toString(), 6);
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (addressList != null) {
                searchedAddress = new String[addressList.size()];
                for (int i = 0; i < addressList.size(); i++) {

//                    Address suggestedAddress = addressList.get(i);
//                    LatLng latLng = new LatLng(suggestedAddress.getLatitude(), suggestedAddress.getLongitude());
//                    addressModels.add(new SuggesedAddressModel(suggestedAddress, latLng, suggestedAddress.getAddressLine(0)));
//                    searchedAddress[i] = suggestedAddress.getAddressLine(0);


                }
            }

            adapter = new ArrayAdapter<String>(AddressActivity.this,
                    android.R.layout.simple_list_item_1, searchedAddress);

            location.setAdapter(adapter);
        }


//        address.setAdapter(adapter);
    }


    private void getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            location.setText(address);
            this.city.setText(city);
            this.state.setText(state);
            pincode.setText(postalCode);
            this.country.setText(country);


            Log.e("Address ", address + " " + city + " " + state + " " + country + " " + postalCode + " " + knownName);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private String location2, city2, state2, pincode2;


    private View.OnClickListener btn_addListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (checkValidation()) {
                location2 = location.getText().toString();
                city2 = city.getText().toString();
                state2 = state.getText().toString();
                pincode2 = pincode.getText().toString() + " " + country.getText().toString();

                AddAddress addAddress = new AddAddress();
                addAddress.execute();


            }
        }
    };

    private boolean checkValidation() {
        if (location.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter location", Toast.LENGTH_LONG).show();
            return false;

        } else if (city.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter city", Toast.LENGTH_LONG).show();
            return false;

        } else if (state.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please enter state", Toast.LENGTH_LONG).show();
            return false;

        } else if (pincode.getText().toString().length() != 6) {
            Toast.makeText(getApplicationContext(), "Please enter valid pincode", Toast.LENGTH_LONG).show();
            return false;

        } else if (country.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Enter country name", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;

        }

    }

    class AddAddress extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            reg_progres1.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("userid", SharedPrefManager.getInstance(AddressActivity.this).getUser().getUserid());
            params.put("location", location2);
            params.put("city", city2);
            params.put("state", state2);
            params.put("pin", pincode2);
            Log.e("addresss", "kkkkkkkkkkkk");
            Log.e("addresssupdate", addressupdate.toString());
            if (addressupdate == false) {
                return requestHandler.sendPostRequest(URLs.URL_Add_Address, params);
            } else {
                return requestHandler.sendPostRequest(URLs.URL_Update_Address, params);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            reg_progres1.setVisibility(View.GONE);
            if (addressupdate == false) {
                Toast.makeText(AddressActivity.this, "Address Details saved", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Navigation.class));
            } else {
                Toast.makeText(AddressActivity.this, "Address Updated Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Profile.class));
            }


        }
    }


    class GetAddress extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("userid", userid);
            return requestHandler.sendPostRequest(URLs.URL_Get_Address, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressDialog.dismiss();

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {
                    JSONArray userJson = jsonObject.getJSONArray("msg");
                    JSONObject user = userJson.getJSONObject(0);
                    String locationFetched = user.getString("location");
                    String cityFetched = user.getString("city");
                    String stateFetched = user.getString("state");
                    String pincodeFetched = user.getString("pin");


                    location.setText(locationFetched);
                    city.setText(cityFetched);
                    state.setText(stateFetched);
                    if (pincodeFetched != "") {
                        if (pincodeFetched.split(" ").length == 2) {
                            pincode.setText(pincodeFetched.split(" ")[0]);
                            country.setText(pincodeFetched.split(" ")[1]);
                        } else {
                            pincode.setText(pincodeFetched);
                            country.setText("");
                        }
                    }


                }
            } catch (Exception e) {
                Log.e("Exception", e.toString());
            }

        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Locaion Permission")
                        .setMessage("Location Permission Required for getting your current address")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(AddressActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        requestSingleUpdate(AddressActivity.this, new LocationCallback() {
                            @Override
                            public void onNewLocationAvailable(GPSCoordinates location) {
                                getAddress(location.latitude, location.longitude);
                            }
                        });
                    }

                } else {

                    current_location.setChecked(false);
                    GetAddress getAddress = new GetAddress();
                    getAddress.execute();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }


    public interface LocationCallback {
        public void onNewLocationAvailable(GPSCoordinates location);
    }

    // calls back to calling thread, note this is for low grain: if you want higher precision, swap the
    // contents of the else and if. Also be sure to check gps permission/settings are allowed.
    // call usually takes <10ms
    public void requestSingleUpdate(final Context context, final LocationCallback callback) {
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isNetworkEnabled) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.requestSingleUpdate(criteria, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        callback.onNewLocationAvailable(new GPSCoordinates(location.getLatitude(), location.getLongitude()));
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                }, null);

            }

        } else {
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);

                locationManager.requestSingleUpdate(criteria, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        callback.onNewLocationAvailable(new GPSCoordinates(location.getLatitude(), location.getLongitude()));
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                }, null);
            }
        }
    }


    // consider returning Location instead of this dummy wrapper class
    public class GPSCoordinates {
        public float longitude = -1;
        public float latitude = -1;

        public GPSCoordinates(float theLatitude, float theLongitude) {
            longitude = theLongitude;
            latitude = theLatitude;
        }

        public GPSCoordinates(double theLatitude, double theLongitude) {
            longitude = (float) theLongitude;
            latitude = (float) theLatitude;
        }
    }

    private boolean isInternetAvailable() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        return connected;
    }


}
