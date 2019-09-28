package com.safepayu.wallet.adapterpackage;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.safepayu.wallet.R;
import com.safepayu.wallet.model.UpiBenModel;
import com.safepayu.wallet.model.WalletHistoryModel;

import java.util.ArrayList;

public class UpiBenAdapter extends BaseAdapter {
    Activity context;
    ArrayList<UpiBenModel> upiBenModels;
    private static LayoutInflater inflater;


    public UpiBenAdapter(Activity activity, ArrayList<UpiBenModel> upiBenModels) {
        this.context = activity;
        this.upiBenModels = upiBenModels;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return upiBenModels.size();
    }

    @Override
    public Object getItem(int position) {
        return upiBenModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.upiben_item, null): itemView;
        TextView UpiBenNumber = (TextView) itemView.findViewById(R.id.UpiBenNumber);

        UpiBenModel selectedUpiBenModel = upiBenModels.get(position);
        //  Date d = null ;

        UpiBenNumber.setText(selectedUpiBenModel.getUpi());
        return itemView;
    }
}
