package com.safepayu.wallet;

import com.safepayu.wallet.model.MobileOffersResponseModel;
import com.safepayu.wallet.model.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface  APIInterface {

    @Multipart
    @POST("/api/create_package?X-API-KEY=k0gkfTs1Son0BgmyuiVL77cSj35Kfyt3qkFZCfmz")
     Call<SendPackageModel> createUser(@Part("file\"; filename=\"pp.png\" ")
                                               RequestBody file, @Part("package_id") RequestBody package_id,@Part("userid") RequestBody userid,@Part("payment_mode") RequestBody payment_mode,@Part("reference_no") RequestBody reference_no,@Part("leave_amount") RequestBody leave_amount,@Part("date_auto_generated") RequestBody date_auto_generated);

    @POST("MobileOffer")
    Call<MobileOffersResponseModel> getMobileOffer(@Query("mobile") String mobile);


}
