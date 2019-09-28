package com.safepayu.wallet.model;

/**
 * Created by Saksham on 2/22/2018.
 */

public class OperatorDetail {
    private String id;
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OperatorDetail(String id, String name) {

        this.id = id;
        this.name = name;

    }
}
