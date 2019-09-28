package com.safepayu.wallet.model;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

public class SuggesedAddressModel
{
    Address address;
    LatLng latLng;
    String fullAddress;

    public SuggesedAddressModel(Address address, LatLng latLng, String fullAddress) {
        this.address = address;
        this.latLng = latLng;
        this.fullAddress = fullAddress;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}