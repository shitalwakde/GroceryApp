package com.app.features.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.R;
import com.app.activities.MainActivity;
import com.app.features.login.ModLogin;
import com.app.features.signup.mvvm.SignUpMvvm;
import com.app.features.signup.mvvm.SignUpPresenterImp;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.signup_fragment, container, false);
        init(rootView);
        click();
        presenter = new SignUpPresenterImp(this);

        return rootView;
    }

    private void init(View rootView) {
        ivBack = (ImageView) rootView.findViewById(R.id.iv_back);
        tvSignup = (TextView) rootView.findViewById(R.id.tv_login);

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

            }
        });
    }

    @Override
    public void onNameInvalid(String message) {
        tvName.setError(message);
    }

    @Override
    public void onEmailInvalid(String message) {
        tvEmail.setError(message);
    }

    @Override
    public void onMobileInvalid(String message) {
        tvMobile.setError(message);
    }

    @Override
    public void onPasswordInvalid(String message) {
        tvPassword.setError(message);
    }

    @Override
    public void onConfirmPasswordInvalid(String message) {
        tvComfirmPassword.setError(message);
    }

    @Override
    public void onSignUpSuccess(ModLogin loginModel) {

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSignUpFail(String message) {

    }
}
