package com.safepayu.wallet.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.R;

public class GeneologyActivity extends AppCompatActivity {
    //private ProgressBar progressBar;
//private String firstname,lastname,mobile;
//private TextView tv_firstName,tv_lastName,tv_mobileNumber;
    private Button send_back_btn;
    private Button refresh_btn;
    //private ArrayList<ReferralUsers> referrals;
//private ListView referralListView;
//Activity activity = this;
    String url;
    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geneology);
        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            webSettings.setDatabasePath("/data/data/" + this.getPackageName() + "/databases/");
        }
        myWebView.setWebChromeClient(new WebChromeClient());

        String userid = SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserid();
        url = "https://abhi.safepayu.com/genealogy?userid=" + userid + "&token=kjhgdwergnmkiuhbv3456uhvcxANEisorbenoinc0fc";
        myWebView.loadUrl(url);
//        progressBar=findViewById(R.id.progressBar);
////        tv_firstName=findViewById(R.id.tv_firstName);
////        tv_lastName=findViewById(R.id.tv_lastName);
////        tv_mobileNumber=findViewById(R.id.tv_mobileNumber);
        send_back_btn = findViewById(R.id.send_back_btn);
        refresh_btn = findViewById(R.id.refresh_btn);
        send_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myWebView.loadUrl(url);
            }
        });
//
//        referrals = new ArrayList<>();

//
//        GeneologyAsyn geneologyAsyn = new GeneologyAsyn();
//        geneologyAsyn.execute();
    }


//    class GeneologyAsyn extends AsyncTask<Void, Void, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected String doInBackground(Void... voids) {
//            RequestHandler requestHandler = new RequestHandler();
//            HashMap<String, String> params = new HashMap<>();
//            params.put("referral_code", SharedPrefManager.getInstance(getApplicationContext()).getUser().getReferral_code());
//
//            return requestHandler.sendPostRequest(URLs.URL_GENEOLOGY, params);
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            progressBar.setVisibility(View.GONE);
//            try {
//                JSONObject obj = new JSONObject(s);
//                if (obj.optString("status").equalsIgnoreCase("success")) {
//
//
//                    JSONArray jsonArray = obj.getJSONArray("msg");
//
//                  //  System.out.println(s);
////                    ReferralUsers myreferralUsers;
//
//                    for(int i = 0; i < jsonArray.length(); i++){
//
//                        System.out.println(i);
//
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                        ReferralUsers myreferralUsers = new ReferralUsers(
//                                jsonObject.optString("userid"),
//                                jsonObject.optString("first_name"),
//                                jsonObject.optString("last_name"),
//                                jsonObject.optString("mobile")
//                        );
//
//                        System.out.println(myreferralUsers.getFirst_name());
//
//                        referrals.add(myreferralUsers);
//
//                    }
//
//                    populateListItems();
//
//                }
//                else{
//
//                    TextView nofererrals = (TextView) findViewById(R.id.noreferrals);
//                    nofererrals.setVisibility(View.VISIBLE);
//
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//    }
//
//    void populateListItems(){
//        referralListView = (ListView) findViewById(R.id.listViewReferrals);
//        referralListView.setDivider(null);
//
//      //  System.out.println(referrals);
//
//        ReferralAdapter referralAdapter = new ReferralAdapter(activity , referrals);
//        referralListView.setAdapter(referralAdapter);
//    }

}



