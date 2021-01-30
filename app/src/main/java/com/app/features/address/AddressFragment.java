package com.app.features.address;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.activities.BaseActivity;
import com.app.callback.LocationLisener;
import com.app.constant.AppConstant;
import com.app.controller.GooglePlacesAutocompleteAdapter;
import com.app.util.AppUtils;
import com.app.util.RestClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonObject;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.app.features.cart.CartActivity.tv_toolbar_cart;

public class AddressFragment extends Fragment implements MultiplePermissionsListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationLisener{

    View rootView;
    TextView tv_save;
    FragmentManager fragmentManager;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etMobile)
    EditText etMobile;
    @BindView(R.id.etHouseNo)
    EditText etHouseNo;
    @BindView(R.id.etArea)
    AutoCompleteTextView etArea;
    @BindView(R.id.etCity)
    EditText etCity;
    @BindView(R.id.etState)
    EditText etState;
    @BindView(R.id.etLandmark)
    EditText etLandmark;
    @BindView(R.id.etPincode)
    EditText etPincode;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.rbHome)
    RadioButton rbHome;
    @BindView(R.id.rbWork)
    RadioButton rbWork;
    @BindView(R.id.cbDefault)
    CheckBox cbDefault;

    String addressType = "";
    String isdefault = "";
    @BindView(R.id.cardViewLocation)
    CardView cardViewLocation;
    private static final int MY_PERMISSIONS = 65;
    public static double latitudePickUp = 0.00;
    public static double longitudePickUp = 0.00;
    LocationManager manager;
    Location location;
    LatLng currentLocation;
    String deliveryId="", go="";

    public AddressFragment(String deliveryId, String go) {
        this.deliveryId = deliveryId;
        this.go = go;
    }

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.address_fragment, container, false);
        ButterKnife.bind(this, rootView);

        init(rootView);
        setData();
        return rootView;
    }

    private void init(View rootView) {
        fragmentManager = getActivity().getSupportFragmentManager();
        tv_save = (TextView) rootView.findViewById(R.id.tv_save);
        etArea.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.layout_google_places));
    }

    public void setLocationByLatLong(double latitudePickUp, double longitudePickUp) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            currentLocation = new LatLng(latitudePickUp, longitudePickUp);
            addresses = geocoder.getFromLocation(latitudePickUp, longitudePickUp, 8);
            if(addresses==null){

            }else {
                Address location=addresses.get(0);
                String pincode=addresses.get(0).getPostalCode();
                String area=addresses.get(0).getLocality();
                showRatingDialog(location.getAddressLine(0), pincode, area);
                //tvAutoAdress.setText(location.getAddressLine(0));
                //pickUpAddress =location.getAddressLine(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        tv_toolbar_cart.setText("Add Address");
        if(deliveryId.equals("")){
            if (AppConstant.isLogin(getActivity())) {
                etName.setText(AppUtils.getUserDetails(getActivity()).getName());
                etEmail.setText(AppUtils.getUserDetails(getActivity()).getEmail());
                etMobile.setText(AppUtils.getUserDetails(getActivity()).getMobile());
            }
        }else{
            getAddress();
        }


        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().isEmpty()) {
                    etName.setError("Please Enter Name");
                    etName.requestFocus();
                } else if (etEmail.getText().toString().isEmpty()) {
                    etEmail.setError("Please Enter Email");
                    etEmail.requestFocus();
                } else if (etMobile.getText().toString().isEmpty()) {
                    etEmail.setError("Please Enter Mobile");
                    etEmail.requestFocus();
                } else if (etHouseNo.getText().toString().isEmpty()) {
                    etHouseNo.setError("Please Enter House No., Building Name");
                    etHouseNo.requestFocus();
                } else if (etArea.getText().toString().isEmpty()) {
                    etArea.setError("Please Enter Road Name, Area, Colony");
                    etArea.requestFocus();
                }else if (etCity.getText().toString().isEmpty()) {
                    etCity.setError("Please Enter City");
                    etCity.requestFocus();
                }else if (etState.getText().toString().isEmpty()) {
                    etState.setError("Please Enter State");
                    etState.requestFocus();
                } else if (etPincode.getText().toString().isEmpty()) {
                    etPincode.setError("Please Enter Pincode");
                    etPincode.requestFocus();
                } else {
                    addDeliveryLocation();
                }
            }
        });


        cardViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddressActivity)getActivity()).checkLocationPermission(AddressFragment.this);
            }
        });

    }



    public void showRatingDialog(String addressLine, String pincode, String area) {
        final Dialog dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.dlg_address);
        TextView tv_submit = (TextView) dialog1.findViewById(R.id.tv_submit);
        TextView tv_cancel = (TextView) dialog1.findViewById(R.id.tv_cancel);
        TextView tv_location_address = (TextView) dialog1.findViewById(R.id.tv_location_address);

        tv_location_address.setText(addressLine);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAddresLocationData(addressLine, pincode, area, dialog1);
                dialog1.dismiss();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        dialog1.show();
    }

    private void setAddresLocationData(String addressLine, String pincode, String area, Dialog dialog1){
        dialog1.dismiss();
        etPincode.setText(pincode);
        //etCity.setText(addressLine);
        //etState.setText(addressLine);
        etArea.setText(addressLine);
    }


    private void getAddress(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", AppUtils.getUserDetails(getActivity()).getLoginId());
        jsonObject.addProperty("deliveryId", deliveryId);

        new RestClient().getApiService().getSingleDeliveryLocation(jsonObject, new Callback<AddressModel>() {
            @Override
            public void success(AddressModel addressModel, Response response) {
                if(addressModel.getSuccess().equals("1")){
                    setAddress(addressModel);
                }else{
                    Toast.makeText(getActivity(), addressModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAddress(AddressModel addressModel){
        etName.setText(addressModel.getName());
        etEmail.setText(addressModel.getEmail());
        etMobile.setText(addressModel.getMobile());
        etHouseNo.setText(addressModel.getHouseNo());
        etArea.setText(addressModel.getArea());
        etCity.setText(addressModel.getCity());
        etState.setText(addressModel.getState());
        etLandmark.setText(addressModel.getLandmark());
        etPincode.setText(addressModel.getPincode());
        if(addressModel.getAddressType().equals("home")){
            rbHome.setChecked(true);
        }else{
            rbHome.setChecked(false);
        }

        if(addressModel.getAddressType().equals("work")){
            rbWork.setChecked(true);
        }else{
            rbWork.setChecked(false);
        }

        if(addressModel.getIsDefault().equals("Yes")){
            cbDefault.setChecked(true);
        }else{
            cbDefault.setChecked(false);
        }
    }

    private void addDeliveryLocation() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", AppUtils.getUserDetails(getActivity()).getLoginId());
        jsonObject.addProperty("deliveryId", deliveryId);
        jsonObject.addProperty("name", etName.getText().toString());
        jsonObject.addProperty("mobile", etMobile.getText().toString());
        jsonObject.addProperty("email", etEmail.getText().toString());
        jsonObject.addProperty("houseNo", etHouseNo.getText().toString());
        jsonObject.addProperty("area", etArea.getText().toString());
        jsonObject.addProperty("city", etCity.getText().toString());
        jsonObject.addProperty("state", etState.getText().toString());
        jsonObject.addProperty("landmark", etLandmark.getText().toString());
        jsonObject.addProperty("pincode", etPincode.getText().toString());
        if (rbHome.isChecked()) {
            addressType = "home";
        } else if (rbWork.isChecked()) {
            addressType = "work";
        } else {
            addressType = "other";
        }
        jsonObject.addProperty("addressType", addressType);
        if (cbDefault.isChecked()) {
            isdefault = "yes";
        } else {
            isdefault = "no";
        }
        jsonObject.addProperty("isdefault", isdefault);

        new RestClient().getApiService().addDeliveryLocation(jsonObject, new Callback<AddressModel>() {
            @Override
            public void success(AddressModel addressModel, Response response) {
                if (addressModel.getSuccess().equals("1")) {
                    //Toast.makeText(getActivity(), addressModel.getMessage(), Toast.LENGTH_SHORT).show();
                    //fragmentManager.beginTransaction().replace(cartContainer, new CheckOutFragment(addressModel.getDeliveryId())).commit();
                    Intent intent = new Intent(getActivity(), AddressActivity.class);
                    intent.putExtra("address", "checkout");
                    intent.putExtra("deliveryId", addressModel.getDeliveryId());
                    intent.putExtra("go", "");
                    startActivity(intent);
                    getActivity().finish();

                } else {
                    Toast.makeText(getActivity(), addressModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
        if( multiplePermissionsReport.areAllPermissionsGranted()) {
            EnableGPSAutoMatically();
        }else{
            AppUtils.setAddress("Laxminagar, Nagpur - 440022");
        }
    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

    }

    private void EnableGPSAutoMatically() {
        GoogleApiClient googleApiClient = null;
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            getLocation();
                            //getCurrentLocation();
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(getActivity(), 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            toast("Setting change not allowed");
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }

    private void toast(String message) {
        try {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            //log("Window has been closed");
        }
    }

    public void getLocation() {
        manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.w("TAG", "inside location != null");
                            latitudePickUp = location.getLatitude();
                            longitudePickUp = location.getLongitude();
                            setLocationByLatLong(latitudePickUp, longitudePickUp);
                        }else{
                            Log.w("TAG", "waiting for location");
                            //Toast.makeText(getActivity(), "waiting for location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((AddressActivity)context).locationLisener = this;
    }

    @Override
    public void locationClickLisener(double latitudePickUp, double longitudePickUp) {
        setLocationByLatLong(latitudePickUp, longitudePickUp);
    }

}
