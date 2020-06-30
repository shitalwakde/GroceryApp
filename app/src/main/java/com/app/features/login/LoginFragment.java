package com.app.features.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.R;
import com.app.activities.MainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {
    View rootView;
    ImageView iv_back;
    TextView tv_login, tv_singup, tv_forgot_password;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.login_fragment, container, false);
        init(rootView);
        click();

        return rootView;
    }

    private void init(View rootView){
        iv_back = (ImageView)rootView.findViewById(R.id.iv_back);
        tv_login = (TextView)rootView.findViewById(R.id.tv_login);
        tv_singup = (TextView)rootView.findViewById(R.id.tv_singup);
        tv_forgot_password = (TextView)rootView.findViewById(R.id.tv_forgot_password);
    }

    private void click(){
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
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
}
