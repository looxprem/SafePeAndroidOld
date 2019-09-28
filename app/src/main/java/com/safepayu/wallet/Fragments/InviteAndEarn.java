package com.safepayu.wallet.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.LoginUser;

public class InviteAndEarn extends Fragment {
        Button inviteFriends_btn;
        TextView referralCode;
        String strReferalcode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invite_and_earn, container, false);
        inviteFriends_btn=(Button)rootView.findViewById(R.id.invite_frind_btn);
        referralCode=(TextView)rootView.findViewById(R.id.referalcodeInvite);
        LoginUser user= SharedPrefManager.getInstance(getActivity()).getUser();
        strReferalcode=user.getReferral_code();
        referralCode.setText(strReferalcode);
        inviteFriends_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"X90 Entertainment game ");
                String shareMessage=" Use this code ' "+strReferalcode+" 'to signUp and share this App to make Money. just download X90 http://bit.ly/2obnPHZ ";
                intent.putExtra(Intent.EXTRA_TEXT,shareMessage);
                startActivity(Intent.createChooser(intent,"Sharing via"));
            }
        });
        return rootView;
    }

}
