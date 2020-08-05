package com.app.features.address;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.constant.AppConstant;
import com.app.util.AppUtils;
import com.app.util.RestClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.app.features.cart.CartActivity.tv_toolbar_cart;

public class AddressFragment extends Fragment {

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
    EditText etArea;
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
                if (Build.VERSION.SDK_INT >= 23) {
                    takePermissionsForMarsh();
                }
                getLocation();
            }
        });

    }


    public void takePermissionsForMarsh() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS);
        }

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
                setAddresLocationData(addressLine, pincode, area);
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

    private void setAddresLocationData(String addressLine, String pincode, String area){
        etPincode.setText(pincode);
        //etCity.setText(addressLine);
        //etState.setText(addressLine);
        etArea.setText(addressLine);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.w("TAG", "goToLogin from onRequestPermissionsResult");
                    Log.w("TAG", "GRANT RESULT " + grantResults.toString());

                } else {

                }
                return;
            }

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
        location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        Log.w("TAG", "outside location : " + manager + "location : " + location);
        if (location != null) {
            Log.w("TAG", "inside location != null");
            latitudePickUp = location.getLatitude();
            longitudePickUp = location.getLongitude();

            Toast.makeText(getActivity(), "Location : #latitude : " + latitudePickUp+"##longitude : " + longitudePickUp, Toast.LENGTH_SHORT).show();

            Log.w("TAG", "##latitude : " + latitudePickUp);
            Log.w("TAG", "##longitude : " + longitudePickUp);

            //etLocation.setAdapter(new GooglePlacesAutocompleteAdapter(ActBusiness.this, R.layout.layout_google_places));
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
        } else {
            manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                buildAlertMessageNoGps();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 5000);

                //Snackbar.make(v, "Pick location will take sometime.", Snackbar.LENGTH_LONG).show();
                Log.w("TAG", "waiting for location");
                Toast.makeText(getActivity(), "waiting for location", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Location Permission");
        builder.setMessage("This app requires location services to be enable, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.dismiss();
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }

                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //getActivity().finish();
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
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

}
