package com.safepayu.wallet.adapterpackage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.safepayu.wallet.model.Offer;
import com.safepayu.wallet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saksham on 2/24/2018.
 */


public class OfferAdapter  extends RecyclerView.Adapter<OfferAdapter.RechargeOfferViewHolder> {

    private List<Offer> offers;
    private List<Offer> allOffers;
    private String offerType = "Topup";
    Context context;

    public OfferAdapter(Context context, ArrayList<Offer> reports) {
        this.context = context;
        this.allOffers = reports;
        this.offers = getOffers();
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    public void setOfferType(String offerType){

        this.offerType = offerType;
        this.offers = getOffers();
        this.notifyDataSetChanged();
    }

    public List<Offer> getOffers(){//Toast.makeText(context,"50",Toast.LENGTH_SHORT).show();
        List<Offer> list = new ArrayList<>();
        for (Offer o : allOffers){
            if (o.getCategory().equals(offerType)){
                list.add(o);

            }
        }
        return list;
    }

    public String getAmount(int pos) {
        try {
            return offers.get(pos).getAmount();
        }catch (Exception e){
            return "";
        }
    }

    @Override
    public OfferAdapter.RechargeOfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;
        // For First Item with Big ImageView , Other are small
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_offers, parent, false);

        return new OfferAdapter.RechargeOfferViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OfferAdapter.RechargeOfferViewHolder holder, int position) {
        //Toast.makeText(context,"82",Toast.LENGTH_SHORT).show();

        Offer data = offers.get(position);

        holder.amount.setText(data.getAmount());
        holder.validity.setText(data.getValidity());
        holder.description.setText(data.getShortdesc());
        holder.type.setText(data.getTalktime());
    }

    public class RechargeOfferViewHolder extends RecyclerView.ViewHolder {

        public TextView amount;
        public TextView validity;
        public TextView description;
        public TextView type;

        public RechargeOfferViewHolder(View view) {
            super(view);
            amount = (TextView) view.findViewById(R.id.amount);
            validity = (TextView) view.findViewById(R.id.validity);
            description = (TextView) view.findViewById(R.id.description);
            type = (TextView) view.findViewById(R.id.type);
        }
    }
}
