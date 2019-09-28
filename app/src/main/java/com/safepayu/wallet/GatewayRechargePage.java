package com.safepayu.wallet;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.safepayu.wallet.Activity.Navigation;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.paymentpackage.AddMoney;
import com.safepayu.wallet.paymentpackage.PaymentStatusActivity;
import com.safepayu.wallet.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GatewayRechargePage extends AppCompatActivity {
    WebView webView;

    Context activity;
    AlertDialog.Builder builder;
    String mId;
    String str_id, formattedDate;
    private String mMerchantKey = "MkDEnAdd";
    private String mSalt = "1LnhMi4Onq";
    private String mBaseURL = "https://secure.payu.in";
    private String mAction = ""; // For Final URL
    private String mTXNId; // This will create below randomly
    private String mHash; // This will create below randomly
    private String mProductInfo = "Food Items"; //Passing String only
    private String mFirstName = "abc"; // From Previous RechargeFailed
    private String mEmailId = "xyz@gmail.com"; // From Previous RechargeFailed
    private String mAmount = "0"; // From Previous RechargeFailed
    private String sAmount; // From Previous RechargeFailed
    private String mPhone = "1234567890"; // From Previous RechargeFailed
    private String mServiceProvider = "payu_paisa";
    private String mSuccessUrl = "https://www.payumoney.com/mobileapp/payumoney/success.php";
    private String mFailedUrl = "https://www.payumoney.com/mobileapp/payumoney/failure.php";
    ProgressDialog progressDialog;
    String operator;

    boolean isFromOrder;
    /**
     * Handler
     */
    Handler mHandler = new Handler();

    /**
     * @param savedInstanceState
     */
    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled", "JavascriptInterface"})

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_recharge_page);
        //webView = new WebView(this);
        webView = (WebView) findViewById(R.id.webView);
        progressDialog = new ProgressDialog(GatewayRechargePage.this);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(c.getTime());
        /**
         * Context Variable
         */
        activity = getApplicationContext();

        /**
         * Actionbar Settings
         /* *//*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        // enabling action bar app icon and behaving it as toggle button
        ab.setHomeButtonEnabled(true);
        ab.setTitle(getString(R.string.title_activity_online_payment));
*/
        /**
         * Getting Intent Variables...
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(GatewayRechargePage.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(GatewayRechargePage.this);
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            mFirstName = bundle.getString("name");
            mEmailId = bundle.getString("email");
            mAmount = bundle.getString("amount");
            mPhone = bundle.getString("phone");
            mId = bundle.getString("id");
            isFromOrder = bundle.getBoolean("isFromOrder");
            str_id = String.valueOf(mId);
            operator = bundle.getString("operatorCode");
            sAmount = String.valueOf(mAmount);
            //Log.i(TAG, "" + mFirstName + " : " + mEmailId + " : " + mAmount + " : " + mPhone);

            /**
             * Creating Transaction Id
             */
            double am;
            ///// am=Double.parseDouble(mAmount);
            Random rand = new Random();
            String randomString = Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
            mTXNId = hashCal("SHA-256", randomString).substring(0, 20);

            am = new BigDecimal(mAmount).setScale(0, RoundingMode.UP).intValue();

            /**
             * Creating Hash Key
             */
            mHash = hashCal("SHA-512", mMerchantKey + "|" +
                    mTXNId + "|" +
                    mAmount + "|" +
                    mProductInfo + "|" +
                    mFirstName + "|" +
                    mEmailId + "|||||||||||" +
                    mSalt);
            System.out.println("hashecodeeeee: " + mHash);
            /**
             * Final Action URL...
             */
            mAction = mBaseURL.concat("/_payment");

            /**
             * WebView Client
             */
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    //  Toast.makeText(activity, "Oh no! " + error, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onReceivedSslError(WebView view,
                                               final SslErrorHandler handler, SslError error) {
                    // Toast.makeText(activity, "SSL Error! " + error, Toast.LENGTH_SHORT).show();
                    //handler.proceed();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(GatewayRechargePage.this);
                    builder.setMessage(R.string.notification_error_ssl_cert_invalid);
                    builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handler.proceed();
                        }
                    });
                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handler.cancel();
                        }
                    });
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return super.shouldOverrideUrlLoading(view, url);
                }

                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    //make sure dialog is showing
                    if (!progressDialog.isShowing()) {
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                    }
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    progressDialog.dismiss();
                    if (url.equals(mSuccessUrl)) {
                        addMoney();
                        MobRecharge();
                       /* System.out.println("status: "+true);
                        System.out.println("transaction_id: "+mTXNId);
                        System.out.println("mId: "+mId);
                        System.out.println("isFromOrder: "+isFromOrder);*/

                        Intent intent = new Intent(GatewayRechargePage.this, PaymentStatusActivity.class);
                        intent.putExtra("status", "Success Full Transaction");
                        intent.putExtra("transaction_id", mTXNId);
                        intent.putExtra("id", mId);
                        intent.putExtra("amount", mAmount);
                        intent.putExtra("isFromOrder", isFromOrder);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else if (url.equals(mFailedUrl)) {
                       /* System.out.println("false status: "+false);
                        System.out.println("false transaction_id: "+mTXNId);
                        System.out.println("false mId: "+mId);
                        System.out.println("false isFromOrder: "+isFromOrder);*/

                        Intent intent = new Intent(GatewayRechargePage.this, PaymentStatusActivity.class);
                        intent.putExtra("status", "failed Transaction");
                        intent.putExtra("amount", mAmount);
                        intent.putExtra("transaction_id", mTXNId);
                        intent.putExtra("id", mId);
                        intent.putExtra("isFromOrder", isFromOrder);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    super.onPageFinished(view, url);
                }
            });

            webView.setVisibility(View.VISIBLE);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setCacheMode(2);
            webView.getSettings().setDomStorageEnabled(true);
            webView.clearHistory();
            webView.clearCache(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setUseWideViewPort(false);
            webView.getSettings().setLoadWithOverviewMode(false);
            webView.addJavascriptInterface(new GatewayRechargePage.PayUJavaScriptInterface(GatewayRechargePage.this), "PayUMoney");
            //webView.addJavascriptInterface(new PayUJavaScriptInterface(), "PayUMoney");
            /**
             * Mapping Compulsory Key Value Pairs
             */
            Map<String, String> mapParams = new HashMap<>();

            mapParams.put("key", mMerchantKey);
            mapParams.put("txnid", mTXNId);
            mapParams.put("amount", String.valueOf(mAmount));
            mapParams.put("productinfo", mProductInfo);
            mapParams.put("firstname", mFirstName);
            mapParams.put("email", mEmailId);
            mapParams.put("phone", mPhone);
            mapParams.put("surl", mSuccessUrl);
            mapParams.put("furl", mFailedUrl);
            mapParams.put("hash", mHash);
            mapParams.put("service_provider", mServiceProvider);

            webViewClientPost(webView, mAction, mapParams.entrySet());
        } else {
            Toast.makeText(activity, "Something went wrong, Try again.", Toast.LENGTH_LONG).show();
        }


    }

    public void webViewClientPost(WebView webView, String url,
                                  Collection<Map.Entry<String, String>> postData) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head></head>");
        sb.append("<body onload='form1.submit()'>");
        sb.append(String.format("<form id='form1' action='%s' method='%s'>", url, "post"));

        for (Map.Entry<String, String> item : postData) {
            sb.append(String.format("<input name='%s' type='hidden' value='%s' />", item.getKey(), item.getValue()));
        }
        sb.append("</form></body></html>");

        Log.d("TAG", "webViewClientPost called: " + sb.toString());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading. Please wait...");
        webView.loadData(sb.toString(), "text/html", "utf-8");
    }

    /**
     * Hash Key Calculation
     *
     * @param type
     * @param str
     * @return
     */
    public String hashCal(String type, String str) {
        byte[] hashSequence = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashSequence);
            byte messageDigest[] = algorithm.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1)
                    hexString.append("0");
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException NSAE) {
        }
        return hexString.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onPressingBack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        onPressingBack();
    }

    /**
     * On Pressing Back
     * Giving Alert...
     */
    private void onPressingBack() {

        final Intent intent;

        /*if(isFromOrder)
            intent = new Intent(PayUMoneyActivity.this, ProductInCartList.class);
        else
            intent = new Intent(PayUMoneyActivity.this, MainActivity.class);*/
        intent = new Intent(GatewayRechargePage.this, AddMoney.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GatewayRechargePage.this);

        // Setting Dialog Title
        alertDialog.setTitle("Warning");

        // Setting Dialog Message
        alertDialog.setMessage("Do you cancel this transaction?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public class PayUJavaScriptInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        PayUJavaScriptInterface(Context c) {
            mContext = c;
        }

        public void success(long id, final String paymentId) {
            mHandler.post(new Runnable() {

                public void run() {
                    mHandler = null;
                    Toast.makeText(GatewayRechargePage.this, "Payment Successfully.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    void addMoney() {

        class AddMoney extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //    progressbarConformBet.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... strings) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("id", str_id);
                params.put("amount", sAmount);
                params.put("action", "credit");
                params.put("remarks", "Amount Added from your Card");
                params.put("date", formattedDate);
                params.put("transaction_id", mTXNId);
                return requestHandler.sendPostRequest(URLs.URL_ADD_MONEY, params);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // progressbarConformBet.setVisibility(View.GONE);
                try {
                    JSONObject obj = new JSONObject(s);
                    //  System.out.println("hellooooojson object"+obj);
                    if (obj.getString("status").equals("TRUE")) {
//                        Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println("elseeeeeeeeeee");
                        //  Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        AddMoney addMoney = new AddMoney();
        addMoney.execute();
        //text_money.setText("Rs. "+str_money);
    }


    private void MobRecharge() {
        class MobileRechrgeClass1 extends AsyncTask<String, String, String> {

            // final String mobile = mobileET.getText().toString();
            // String amount = amountET.getText().toString();
            // String a = operatorSP.getSelectedItem().toString();

            //  String op[] = a.split(":");

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ///  progressBar.setVisibility(rootView.VISIBLE);
            }

            @Override
            protected String doInBackground(String... param) {

                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("type", "mobile");
                System.out.println("in hash mappinnnnnnnnnn");
                params.put("domain", "whitelabel.sakshamapp.com");
                params.put("amount", sAmount);
                params.put("remarks", "ddddddddddddddddddd");
                params.put("number", mPhone);
                params.put("id", mId);
                params.put("operator", operator);
                params.put("circle", "1");
                //      String u = "http://www.x90entertainment.com/api/recharge/?X-API-KEY=525HGJLF4562bncvbmHDSEDRnvdvbzg5645454&type=mobile&circle=1&amount=" + amount + "&number=" + mobile + "&operator=" + a + "&id=" + user_id + "&password="++"";
                String u = "http://www.x90entertainment.com/api/recharge/?X-API-KEY=525HGJLF4562bncvbmHDSEDRnvdvbzg5645454&type=mobile&circle=1&amount=" + mAmount + "&number=" + mPhone + "&operator=" + operator + "&id=" + mId + "";
                return requestHandler.sendGetRequest(u, params);
            }

            @Override
            protected void onPostExecute(String result) {
                // progressBar.setVisibility(View.GONE);
                System.out.println(result + " result");
                //// data = new ArrayList<>();
                //  pdLoading.dismiss();
                try {
                    System.out.println("under try befor json array");
                    JSONObject obj = new JSONObject(result);
                    //  JSONObject obj2 = new JSONObject(obj.getString("response"));
                    // System.out.println(obj);
                    boolean error;
                    error = obj.getBoolean("error");
                    String msg = obj.getString("response");
                    if (!error) {

                        //   Toast.makeText(getActivity(),mobile+"  "+amount+"  "+a+"  ",Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();


                        builder.setTitle("Recharge  Detail")
                                .setMessage(msg)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(GatewayRechargePage.this, Navigation.class));
                                        // continue with delete
                                    }
                                })
                                // .setNegatsiveButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                //      public void onClick(DialogInterface dialog, int which) {
                                //          // do nothing
                                //       }
                                ///   })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        builder.setTitle("Recharge  Detail")
                                .setMessage("Recharge is Failure")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(GatewayRechargePage.this, Navigation.class));
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                } catch (JSONException e) {
                    builder.setTitle("Recharge Detail")
                            .setMessage("Error In Processing")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(GatewayRechargePage.this, Navigation.class));
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
//Toast.makeText(getActivity(),mobile+"  "+amount+"  "+a+"  ",Toast.LENGTH_SHORT).show();
        }
        MobileRechrgeClass1 ul = new MobileRechrgeClass1();
        ul.execute();


    }
}
