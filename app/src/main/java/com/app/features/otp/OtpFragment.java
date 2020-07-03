package com.app.features.otp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.app.R;
import com.app.activities.MainActivity;
import com.app.constant.AppConstant;
import com.app.features.login.ModLogin;
import com.app.features.otp.mvvm.OtpMvvm;
import com.app.features.otp.mvvm.OtpPresenterImp;
import com.app.util.AppUtils;
import com.app.util.PrefUtil;
import com.app.util.RestClient;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OtpFragment extends Fragment implements OtpMvvm.OtpView {
    View rootView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.et_otp)
    PinEntryEditText etOtp;
    @BindView(R.id.tvVerify)
    TextView tvVerify;
    @BindView(R.id.tvResendOtp)
    TextView tvResendOtp;
    @BindView(R.id.mTextField)
    EditText mTextField;

    OtpMvvm.OtpPresenter presenter;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.ll_otp)
    LinearLayout llOtp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.otp_fragment, container, false);
        ButterKnife.bind(this, rootView);
        presenter = new OtpPresenterImp(this);
        click();
        return rootView;
    }

    private void click() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        tvVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                presenter.onVerifyClicked(getActivity(), etOtp.getText().toString());
            }
        });

        tvResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                callApiForResendOtp();
            }
        });
    }

    @Override
    public void onOtpInvalid(String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        //etOtp.setError(message);
    }

    @Override
    public void onOtpSuccess(ModLogin loginModel) {
        progressBar.setVisibility(View.GONE);
        PrefUtil.getInstance(getContext()).putData(AppConstant.PREF_USER_ID, loginModel.getLoginId());
        AppUtils.setUserDetails(getContext(), loginModel);
        getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }

    @Override
    public void onOtpFail(String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        //Snackbar.make(progressBar, message, Snackbar.LENGTH_SHORT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        progressBar.setVisibility(View.GONE);
        presenter.onViewDestroy();
    }


    private void callApiForResendOtp(){
        JsonObject jsonObject = new JsonObject();
        if(AppUtils.getUserDetails(getActivity()).getEmail() == null){
            jsonObject.addProperty("mobileEmail", AppUtils.getUserDetails(getActivity()).getMobile());
        }else {
            jsonObject.addProperty("mobileEmail", AppUtils.getUserDetails(getActivity()).getEmail());
        }

        new RestClient().getApiService().getResendOtp(jsonObject, new Callback<ModLogin>() {
            @Override
            public void success(ModLogin loginModel, Response response) {
                progressBar.setVisibility(View.GONE);
                if(loginModel.getSuccess().equals("1")){
                    Toast.makeText(getActivity(), "Otp send successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), loginModel.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
