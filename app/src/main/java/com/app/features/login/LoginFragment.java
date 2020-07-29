package com.app.features.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.R;
import com.app.activities.MainActivity;
import com.app.constant.AppConstant;
import com.app.features.login.mvvm.LoginPresenter;
import com.app.features.login.mvvm.LoginPresenterImp;
import com.app.features.login.mvvm.LoginView;
import com.app.util.AppUtils;
import com.app.util.PrefUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.view.View.GONE;

public class LoginFragment extends Fragment implements LoginView {
    View rootView;
    ImageView iv_back;
    TextView tv_login, tv_singup, tv_forgot_password;
    EditText et_username, et_password;
    TextInputLayout tv_username, tv_password;
    LoginPresenter presenter;
    ProgressBar progressBar;
    AppConstant appConstant;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //rootView = inflater.inflate(R.layout.login_fragment, container, false);
        rootView = inflater.inflate(R.layout.login_temp_layout, container, false);
        init(rootView);
        presenter=new LoginPresenterImp(this);
        click();

        return rootView;
    }


    private void init(View rootView){
        et_username = (EditText)rootView.findViewById(R.id.et_username);
        et_password = (EditText)rootView.findViewById(R.id.et_password);
        tv_username = (TextInputLayout) rootView.findViewById(R.id.tv_username);
        tv_password = (TextInputLayout)rootView.findViewById(R.id.tv_password);
        iv_back = (ImageView)rootView.findViewById(R.id.iv_back);
        tv_login = (TextView)rootView.findViewById(R.id.tv_login);
        tv_singup = (TextView)rootView.findViewById(R.id.tv_singup);
        tv_forgot_password = (TextView)rootView.findViewById(R.id.tv_forgot_password);
        progressBar = rootView.findViewById(R.id.progress);
    }

    private void click(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_username.getText().toString();
                String password = et_password.getText().toString();
                if(AppUtils.isNullOrEmpty(email)){
                    progressBar.setVisibility(GONE);
                    et_username.setError("Please Enter User Name");
                    et_username.requestFocus();
                }else
                if(AppUtils.isNullOrEmpty(password)){
                    progressBar.setVisibility(GONE);
                    et_password.setError("Please Enter Password");
                    et_password.requestFocus();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    presenter.onLoginClicked(et_username.getText().toString(),et_password.getText().toString());
                }
            }
        });

        tv_singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("type", "singup");
                startActivity(intent);
            }
        });

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });

    }


    public void showRatingDialog(){
        final Dialog dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.dlg_forgot_password);
        ImageView iv_cross = (ImageView)dialog1.findViewById(R.id.iv_cross);
        TextView tv_forgot_password = (TextView)dialog1.findViewById(R.id.tv_forgot_password);

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                //saveRatings(String.valueOf(ratingBar.getRating()), et_review.getText().toString(), dialog1);
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

    @Override
    public void onUsernameEmpty() {
        progressBar.setVisibility(View.GONE);
        //tv_username.setError("Please Enter Username");
        //tv_username.requestFocus();
    }

    @Override
    public void onUsernameInvalid() {
        progressBar.setVisibility(View.GONE);
        //tv_username.setError("Please Enter Valid Username");
        //tv_username.requestFocus();
    }

    @Override
    public void onPasswordInvalid() {
        progressBar.setVisibility(View.GONE);
        //tv_password.setError("Please Enter Password");
        //tv_password.requestFocus();
    }

    @Override
    public void onLoginSuccess(ModLogin loginModel) {
        progressBar.setVisibility(View.GONE);
        PrefUtil.getInstance(getContext()).putData(AppConstant.PREF_USER_ID,loginModel.getLoginId());
        AppUtils.setUserDetails(getContext(),loginModel);
        getActivity().startActivity(new Intent(getContext(),MainActivity.class));
        getActivity().finishAffinity();
        //AppUtils.getUserDetails(getContext()).getLoginId();
    }

    @Override
    public void onLoginFail(String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
       // Snackbar.make(progressBar,message,Snackbar.LENGTH_SHORT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        progressBar.setVisibility(View.GONE);
        presenter.onViewDestroy();
    }
}
