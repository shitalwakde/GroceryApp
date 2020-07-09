package com.app.features.checkout;

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
import com.app.features.address.AddressFragment;
import com.app.features.address.AddressListFragment;
import com.app.features.navmenu.OrderDetailFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static com.app.features.cart.CartActivity.cartContainer;
import static com.app.features.cart.CartActivity.tv_toolbar_cart;

public class CheckOutFragment extends Fragment {

    View rootView;
    TextView tv_order, tv_change;
    FragmentManager fragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.checkout_fragment, container, false);
        init(rootView);
        click();

        return rootView;
    }

    private void init(View rootView){
        fragmentManager = getActivity().getSupportFragmentManager();
        tv_order = (TextView)rootView.findViewById(R.id.tv_order);
        tv_change = (TextView)rootView.findViewById(R.id.tv_change);
    }

    private void click(){

        tv_toolbar_cart.setText("CheckOut");


        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fragmentManager.beginTransaction().replace(cartContainer, new AddressListFragment()).addToBackStack(null).commit();
                fragmentManager.beginTransaction().replace(cartContainer, new AddressFragment()).addToBackStack(null).commit();
            }
        });

        tv_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
                //fragmentManager.beginTransaction().replace(cartContainer, new OrderDetailFragment()).addToBackStack(null).commit();
            }
        });
    }

    public void showRatingDialog(){
        final Dialog dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.dlg_thankyou);
        TextView tv_submit = (TextView)dialog1.findViewById(R.id.tv_submit);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                //saveRatings(String.valueOf(ratingBar.getRating()), et_review.getText().toString(), dialog1);
            }
        });

        dialog1.show();
    }
}
