package com.safepayu.wallet.adapterpackage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.safepayu.wallet.R;
import com.safepayu.wallet.model.transaction_hist;

import java.util.List;

/**
 * Created by Saksham on 2/24/2018.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ReportViewHolder> {

    private List<transaction_hist> reports;
    Context context;

    public HistoryAdapter(Context context, List<transaction_hist> reports) {
        this.context = context;
        this.reports = reports;
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }


    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        // For First Item with Big ImageView , Other are small
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_report, parent, false);

        return new ReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReportViewHolder holder, int position) {
        transaction_hist data = reports.get(position);

        holder.idView.setText(data.getTxnID());
        holder.mobileView.setText(data.getMobileNo());
        holder.operatorView.setText(data.getOperator());
        holder.statusView.setText(data.getStatus());
        holder.amountView.setText(data.getCR());
       // holder.usernameView.setText(data.getUsername());
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder {

        public TextView idView;
        public TextView mobileView;
        public TextView operatorView;
        public TextView statusView,amountView;
       /// public TextView usernameView;

        public ReportViewHolder(View view) {
            super(view);
            idView = (TextView) view.findViewById(R.id.transactionid);
            mobileView = (TextView) view.findViewById(R.id.mobile);
            operatorView = (TextView) view.findViewById(R.id.operator);
            statusView = (TextView) view.findViewById(R.id.status);
            amountView = (TextView) view.findViewById(R.id.amount);
         //   usernameView = (TextView) view.findViewById(R.id.username);
        }
    }
}