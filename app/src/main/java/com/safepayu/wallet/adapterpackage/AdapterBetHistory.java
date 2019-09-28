package com.safepayu.wallet.adapterpackage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import com.safepayu.wallet.model.User;
import com.safepayu.wallet.R;

public class AdapterBetHistory extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<User> data= Collections.emptyList();
    public AdapterBetHistory(Context context, List<User> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.bet_history, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final MyHolder myHolder= (MyHolder) holder;
        User user=data.get(position);
        // Picasso.with(context).load(product.getProduct_image()).placeholder(R.drawable.default_product_img).error(R.drawable.no_internet_connection).into(myHolder.product_image);
        myHolder.bet_number.setText(user.getBet_number());
        myHolder.date.setText(user.getBet_date());
        myHolder.coins.setText("Coins. "+user.getCoins());
        myHolder.status.setText(user.getBetStatus());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class MyHolder extends RecyclerView.ViewHolder {

        TextView bet_number,coins,date,status;

        public MyHolder(View itemView) {
            super(itemView);
            bet_number = (TextView) itemView.findViewById(R.id.bet_history_action);
            coins = (TextView) itemView.findViewById(R.id.bet_history_amount);
            date=(TextView)itemView.findViewById(R.id.bet_history_date);
            status=(TextView)itemView.findViewById(R.id.bet_history_status);
        }
    }
}
