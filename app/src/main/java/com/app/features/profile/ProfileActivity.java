package com.app.features.profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.activities.MainActivity;
import com.app.features.login.ModLogin;
import com.app.util.AppUtils;
import com.app.util.RestClient;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.tv_toolbar_offer)
    TextView tvToolbarOffer;
    @BindView(R.id.iv_lock)
    ImageView ivLock;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_profile)
    CircleImageView imgProfile;
    @BindView(R.id.iv_edit_profile)
    ImageView ivEditProfile;
    @BindView(R.id.rl_image)
    RelativeLayout rlImage;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.view_profile)
    View viewProfile;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.lbl_Address)
    LinearLayout lblAddress;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.sc_view)
    ScrollView scView;
    private final int CAMERA_PIC_REQUEST = 2, REQUEST_CAMERA = 301, REQUEST_WRITE_STORAGE = 112, SELECT_PHOTO = 1;
    String captureImg = "", filePath = "";
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        init();
        click();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void init() {
        etName.setText(AppUtils.getUserDetails(this).getName());
        etEmail.setText(AppUtils.getUserDetails(this).getEmail());
        etMobile.setText(AppUtils.getUserDetails(this).getMobile());
        if (AppUtils.getUserDetails(this).getAddress() != null) {
            etAddress.setText(AppUtils.getUserDetails(this).getAddress());
        }
    }


    private void click() {
        getProfile();
        ivLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String mobile = etMobile.getText().toString();

                if (AppUtils.isNullOrEmpty(name)) {
                    etName.setError("Please Enter Full Name");
                    etName.requestFocus();
                } else if (AppUtils.isNullOrEmpty(email)) {
                    etEmail.setError("Please Enter Email Address");
                    etEmail.requestFocus();
                } else if (!AppUtils.isValidEmail(ProfileActivity.this, email)) {
                    etEmail.setError("Please Enter Valid Email Address");
                    etEmail.requestFocus();
                } else if (AppUtils.isNullOrEmpty(mobile)) {
                    etMobile.setError("Please Enter Mobile Number");
                    etMobile.requestFocus();
                } else if (!AppUtils.isValidMobile(ProfileActivity.this, mobile)) {
                    etMobile.setError("Please Enter Valid Mobile Number");
                    etMobile.requestFocus();
                } else {
                    updateProfile(name, email, mobile);
                }
            }
        });

        ivEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);
                startActivityForResult(intent, 1213);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
            filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
            imgProfile.setImageBitmap(selectedImage);
            //Bitmap bitmap=((BitmapDrawable)imgProfile.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            captureImg = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }

    }


    private void updateProfile(String name, String email, String mobile) {
        progressBar.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("loginId", AppUtils.getUserDetails(this).getLoginId());
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("mobile", mobile);
        jsonObject.addProperty("address", etAddress.getText().toString());
        jsonObject.addProperty("image", captureImg);
        //jsonObject.addProperty("image", "");

        new RestClient().getApiService().updateProfile(jsonObject, new Callback<ModLogin>() {
            @Override
            public void success(ModLogin modLogin, Response response) {
                progressBar.setVisibility(View.GONE);
                if (modLogin.getSuccess().equals("1")) {
                    modLogin.setLoginId(AppUtils.getUserDetails(ProfileActivity.this).getLoginId());
                    AppUtils.updateUserDetails(ProfileActivity.this, modLogin);
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    Toast.makeText(ProfileActivity.this, modLogin.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getProfile() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("loginId", AppUtils.getUserDetails(this).getLoginId());

        new RestClient().getApiService().getProfile(jsonObject, new Callback<ProfileModel>() {
            @Override
            public void success(ProfileModel profileModel, Response response) {
                if (profileModel.getSuccess().equals("1")) {
                    setData(profileModel);
                } else {
                    Toast.makeText(ProfileActivity.this, profileModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData(ProfileModel profileModel) {
        etName.setText(profileModel.getName());
        etEmail.setText(profileModel.getEmail());
        etMobile.setText(profileModel.getMobile());
        etAddress.setText(profileModel.getAddress());
        Picasso.with(this).load(profileModel.getImage()).error(R.drawable.profile).into(imgProfile);
    }


    public void showDialog() {
        final Dialog dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.dlg_change_password);
        ImageView iv_cross = (ImageView) dialog1.findViewById(R.id.iv_cross);
        TextView tv_submit = (TextView) dialog1.findViewById(R.id.tv_submit);
        EditText et_confirmPassword = (EditText) dialog1.findViewById(R.id.et_confirmPassword);
        EditText et_newPassword = (EditText) dialog1.findViewById(R.id.et_newPassword);
        EditText et_oldPassword = (EditText) dialog1.findViewById(R.id.et_oldPassword);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = et_oldPassword.getText().toString();
                String newPassword = et_newPassword.getText().toString();
                String confirmPassword = et_confirmPassword.getText().toString();

                if (AppUtils.isNullOrEmpty(oldPassword)) {
                    et_oldPassword.setError("Please Enter Old Password");
                    et_oldPassword.requestFocus();
                } else if (AppUtils.isNullOrEmpty(newPassword)) {
                    et_newPassword.setError("Please Enter New Password");
                    et_newPassword.requestFocus();
                } else if (AppUtils.isNullOrEmpty(confirmPassword)) {
                    et_confirmPassword.setError("Please Enter Confirm Password");
                    et_confirmPassword.requestFocus();
                } else if (!AppUtils.isNullOrEmpty(newPassword) && !AppUtils.isNullOrEmpty(confirmPassword) && !newPassword.equals(confirmPassword)) {
                    et_confirmPassword.setError("Password does not match");
                    et_confirmPassword.requestFocus();
                } else {
                    changePassword(dialog1, oldPassword, newPassword);
                }
            }
        });

        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }


    private void changePassword(Dialog dialog1, String oldPassword, String newPassword) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("loginId", AppUtils.getUserDetails(this).getLoginId());
        jsonObject.addProperty("oldPassword", oldPassword);
        jsonObject.addProperty("newPassword", newPassword);

        new RestClient().getApiService().changePassword(jsonObject, new Callback<ModLogin>() {
            @Override
            public void success(ModLogin modLogin, Response response) {
                if (modLogin.getSuccess().equals("1")) {
                    dialog1.dismiss();
                } else {
                    Toast.makeText(ProfileActivity.this, "Invalid data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                //finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
