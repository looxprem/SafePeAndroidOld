package com.safepayu.wallet.adapterpackage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.safepayu.wallet.R;
import com.safepayu.wallet.model.RechargeModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RechargeHIstoryAdapter extends BaseAdapter {

    Activity context;
    ArrayList<RechargeModel> rechargeModels;

    private static LayoutInflater inflater = null;

    public RechargeHIstoryAdapter(Activity context, ArrayList<RechargeModel> rechargeModels) {
        this.context = context;
        this.rechargeModels = rechargeModels;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return rechargeModels.size();
    }

    @Override
    public Object getItem(int position) {
        return rechargeModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.recharge_item, null): itemView;
        TextView rechargeType = (TextView) itemView.findViewById(R.id.rechtype);
        TextView rechargeAmount = (TextView) itemView.findViewById(R.id.amount);
        TextView rechargeTime = (TextView) itemView.findViewById(R.id.rechtime);
        TextView rechargeStatus = (TextView) itemView.findViewById(R.id.status);
        TextView rechargeOrderid = (TextView) itemView.findViewById(R.id.transc_id);
        TextView rechargeNumber = (TextView) itemView.findViewById(R.id.number);


        RechargeModel selectedRechargeModel = rechargeModels.get(position);

        rechargeAmount.setText("₹"+selectedRechargeModel.getAmount());

//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("dd MM YYYY h:m a");
//            rechargeTime.setText((CharSequence) sdf.parse(selectedRechargeModel.getRecharge_time()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//            // transactionTime.setText(selectedWalletHistoryModel.getDate());
//        }
       rechargeTime.setText(selectedRechargeModel.getRecharge_time());

        rechargeStatus.setText(selectedRechargeModel.getStatus());
        rechargeType.setText(selectedRechargeModel.getRech_type());
        if(selectedRechargeModel.getStatus().equalsIgnoreCase("Success")){
            rechargeStatus.setTextColor(Color.parseColor("#4caf50"));
            rechargeStatus.setText("Success");
        }
        else if (selectedRechargeModel.getStatus().equalsIgnoreCase("Failed")){
            rechargeStatus.setTextColor(Color.RED);
            rechargeStatus.setText("Failed");
        }
        else{
            rechargeStatus.setTextColor(Color.parseColor("#e0ab0a"));
            rechargeStatus.setText("Pending");
        }


        rechargeOrderid.setText(selectedRechargeModel.getOrder_id());
        rechargeNumber.setText(selectedRechargeModel.getCustomer_number());


        return  itemView;
    }
}
