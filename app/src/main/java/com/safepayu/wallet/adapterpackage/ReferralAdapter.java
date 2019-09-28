package com.safepayu.wallet.adapterpackage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.safepayu.wallet.R;
import com.safepayu.wallet.model.ReferralUsers;
import com.safepayu.wallet.model.WalletHistoryModel;

import java.sql.Ref;
import java.util.ArrayList;

public class ReferralAdapter extends BaseAdapter {

    Activity context;
    ArrayList<ReferralUsers> referralUsers;

    private static LayoutInflater inflater = null;

    public ReferralAdapter(Activity context, ArrayList<ReferralUsers> referralsUsers) {
        this.context = context;
        this.referralUsers = referralsUsers;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return referralUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return referralUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.refer_user_item, null): itemView;


        TextView userId = (TextView) itemView.findViewById(R.id.tv_userid);
        TextView firstName = (TextView) itemView.findViewById(R.id.tv_firstName);
        TextView lastName = (TextView) itemView.findViewById(R.id.tv_lastName);
        TextView mobileNumber = (TextView) itemView.findViewById(R.id. tv_mobileNumber);


        ReferralUsers selectedReferralUsers = referralUsers.get(position);
        userId.setText(selectedReferralUsers.getUser_id());
        firstName.setText(selectedReferralUsers.getFirst_name());
        lastName.setText(selectedReferralUsers.getLast_name());
        mobileNumber.setText(selectedReferralUsers.getMobile());

        return  itemView;
    }
}
