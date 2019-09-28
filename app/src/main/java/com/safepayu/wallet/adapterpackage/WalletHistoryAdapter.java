package com.safepayu.wallet.adapterpackage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.safepayu.wallet.HisotyPackage.WalletHistory;
import com.safepayu.wallet.model.RechargeModel;
import com.safepayu.wallet.model.User;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.WalletHistoryModel;
public class WalletHistoryAdapter extends BaseAdapter {

    Activity context;
    ArrayList<WalletHistoryModel> walletHistoryModels;

    private static LayoutInflater inflater = null;

    public WalletHistoryAdapter(Activity context, ArrayList<WalletHistoryModel> walletHistoryModels) {
        this.context = context;
        this.walletHistoryModels = walletHistoryModels;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return walletHistoryModels.size();
    }

    @Override
    public Object getItem(int position) {
        return walletHistoryModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.wallet_history_item, null): itemView;
        TextView walletDescription = (TextView) itemView.findViewById(R.id.description);
        TextView walletAmount = (TextView) itemView.findViewById(R.id.amount);
        TextView transactionTime = (TextView) itemView.findViewById(R.id.rechtime);
        TextView transactionStatus = (TextView) itemView.findViewById(R.id.status);
        TextView transactionId = (TextView) itemView.findViewById(R.id.transc_id);
        TextView transactionOperation = (TextView) itemView.findViewById(R.id.operation);


        WalletHistoryModel selectedWalletHistoryModel = walletHistoryModels.get(position);
      //  Date d = null ;

        walletDescription.setText(selectedWalletHistoryModel.getDescription());

        if(selectedWalletHistoryModel.getDescription().equalsIgnoreCase("WALLET_TOPUP")){
            walletDescription.setText("Wallet Topup");
        }

//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("dd MM YYYY h:m a");
//            transactionTime.setText((CharSequence) sdf.parse(selectedWalletHistoryModel.getDate()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//           transactionTime.setText(selectedWalletHistoryModel.getDate());
//        }



        transactionStatus.setText(selectedWalletHistoryModel.getStatus());
        transactionOperation.setText(selectedWalletHistoryModel.getOperation());
        if(selectedWalletHistoryModel.getStatus().equalsIgnoreCase("Success")){
            transactionStatus.setTextColor( Color.parseColor("#4caf50"));
            transactionStatus.setText("Success");

        }
        else if (selectedWalletHistoryModel.getStatus().equalsIgnoreCase("Failed")){
            transactionStatus.setTextColor(Color.RED);
            transactionStatus.setText("Failed");
        }
        else{
            transactionStatus.setTextColor(Color.parseColor("#e0ab0a"));
            transactionStatus.setText("Pending");
        }
//        try {
//            Date date= parse( selectedWalletHistoryModel.getDate() );
//            SimpleDateFormat ft =
//                    new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
//            transactionTime.setText(ft.format(date));
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//            Log.e("Error",e.toString());
//        }

        transactionTime.setText(selectedWalletHistoryModel.getDate());
        transactionId.setText(selectedWalletHistoryModel.getTransaction_id());

        if(selectedWalletHistoryModel.getOperation().equalsIgnoreCase("Debit")){
            walletAmount.setText("- ₹"+selectedWalletHistoryModel.getAmount());
        }else {
            walletAmount.setText("+ ₹"+selectedWalletHistoryModel.getAmount());
        }



        return  itemView;
    }
}
