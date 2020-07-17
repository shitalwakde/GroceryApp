package com.app.features.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.activities.MainActivity;
import com.app.constant.AppConstant;
import com.app.features.login.LoginActivity;
import com.app.features.login.ModLogin;
import com.app.features.otp.OtpFragment;
import com.app.features.signup.mvvm.SignUpMvvm;
import com.app.features.signup.mvvm.SignUpPresenterImp;
import com.app.util.AppUtils;
import com.app.util.PrefUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class SignupFragment extends Fragment implements SignUpMvvm.SignUpView {
    View rootView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.tv_signup)
    TextView tvSignup;
    @BindView(R.id.tv_name)
    TextInputLayout tvName;
    @BindView(R.id.tv_email)
    TextInputLayout tvEmail;
    @BindView(R.id.tv_mobile)
    TextInputLayout tvMobile;
    @BindView(R.id.tv_password)
    TextInputLayout tvPassword;
    @BindView(R.id.tv_comfirmPassword)
    TextInputLayout tvComfirmPassword;

    SignUpMvvm.SignUpPresenter presenter;
    ProgressBar progressBar;
    FragmentManager fragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.signup_fragment, container, false);
        ButterKnife.bind(this, rootView);
        init(rootView);
        click();
        presenter = new SignUpPresenterImp(this);

        return rootView;
    }

    private void init(View rootView) {
        fragmentManager = getActivity().getSupportFragmentManager();
        progressBar = (ProgressBar)rootView.findViewById(R.id.progress);
    }

    private void click() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                presenter.onSignUpClicked(getActivity(), etName.getText().toString(), etEmail.getText().toString(), etMobile.getText().toString(),
                        etPassword.getText() .toString(), etConfirmPassword.getText().toString());
            }
        });
    }

    @Override
    public void onNameInvalid(String message) {
        progressBar.setVisibility(GONE);
        tvName.setError(message);
        tvName.requestFocus();
    }

    @Override
    public void onEmailInvalid(String message) {
        progressBar.setVisibility(GONE);
        tvEmail.setError(message);
        tvEmail.requestFocus();
    }

    @Override
    public void onMobileInvalid(String message) {
        progressBar.setVisibility(GONE);
        tvMobile.setError(message);
        tvMobile.requestFocus();
    }

    @Override
    public void onPasswordInvalid(String message) {
        progressBar.setVisibility(GONE);
        tvPassword.setError(message);
        tvPassword.requestFocus();
    }

    @Override
    public void onConfirmPasswordInvalid(String message) {
        progressBar.setVisibility(GONE);
        tvComfirmPassword.setError(message);
        tvComfirmPassword.requestFocus();
    }

    @Override
    public void onSignUpSuccess(ModLogin loginModel) {
        progressBar.setVisibility(GONE);
        //PrefUtil.getInstance(getContext()).putData(AppConstant.PREF_USER_ID, loginModel.getLoginId());
        AppUtils.setUserDetails(getContext(),loginModel);
        fragmentManager.beginTransaction().replace(R.id.login_container, new OtpFragment()).addToBackStack(null).commit();
        //(getActivity()).finish();
    }

    @Override
    public void onSignUpFail(String message) {
        progressBar.setVisibility(GONE);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        //Snackbar.make(progressBar, message, Snackbar.LENGTH_SHORT);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        progressBar.setVisibility(GONE);
        presenter.onViewDestroy();
    }
}
