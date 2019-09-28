package com.safepayu.wallet.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;


import com.safepayu.wallet.Activity.RechargeHistory;
import com.safepayu.wallet.HisotyPackage.RedeemHistory;
import com.safepayu.wallet.HisotyPackage.WalletHistory;
import com.safepayu.wallet.R;
public class History extends Fragment {
    LinearLayout wallet_history,reddem_history,recharge_history;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        wallet_history=(LinearLayout)rootView.findViewById(R.id.wallet_history);
        reddem_history=(LinearLayout)rootView.findViewById(R.id.redeem_history);
        recharge_history=(LinearLayout)rootView.findViewById(R.id.recharge_history);

        wallet_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),WalletHistory.class));
            }
        });
        reddem_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),RedeemHistory.class));
            }
        });
        recharge_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),RechargeHistory.class));
            }
        });

        return rootView;
    }


}
