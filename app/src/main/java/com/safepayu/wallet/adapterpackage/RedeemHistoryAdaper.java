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

public class RedeemHistoryAdaper extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<User> data= Collections.emptyList();
    public RedeemHistoryAdaper(Context context, List<User> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.redeem_history, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyHolder myHolder= (MyHolder) holder;
        User user=data.get(position);
        // Picasso.with(context).load(product.getProduct_image()).placeholder(R.drawable.default_product_img).error(R.drawable.no_internet_connection).into(myHolder.product_image);
        myHolder.redeem_id.setText("Redeem id: "+user.getRedeemId());
        myHolder.date.setText(user.getRedeemDate());
        myHolder.redeemAmount.setText("Rs. "+user.getRedeemAmount());
        myHolder.holder_name.setText(user.getAcHolderName());
        myHolder.bank_name.setText(user.getBankName());
        myHolder.acc_number.setText(user.getAcNumber());
        myHolder.ifsccode.setText(user.getIfscCode());
        myHolder.panCard.setText(user.getPancard());
        myHolder.adhaarCard.setText(user.getAdharcard());
        myHolder.status.setText(user.getStatus());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class MyHolder extends RecyclerView.ViewHolder {

        TextView redeem_id,date,redeemAmount,holder_name,bank_name,acc_number,ifsccode,panCard,adhaarCard,status;

        public MyHolder(View itemView) {
            super(itemView);
            date=(TextView)itemView.findViewById(R.id.redeem_history_date);
            redeem_id = (TextView) itemView.findViewById(R.id.redeem_id);
            redeemAmount = (TextView) itemView.findViewById(R.id.redeemAmount);
            holder_name=(TextView)itemView.findViewById(R.id.holder_name);
            bank_name = (TextView) itemView.findViewById(R.id.bank_name);
            acc_number = (TextView) itemView.findViewById(R.id.acc_number);
            ifsccode=(TextView)itemView.findViewById(R.id.ifsccode);
            panCard = (TextView) itemView.findViewById(R.id.panCard);
            adhaarCard = (TextView) itemView.findViewById(R.id.adhaarCard);
            status=(TextView)itemView.findViewById(R.id.status);

        }
    }
}
