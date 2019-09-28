package com.safepayu.wallet.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gocashfree.cashfreesdk.CFClientInterface;
import com.gocashfree.cashfreesdk.CFPaymentService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.safepayu.wallet.AdditionalPage.ContactUs;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.CreatePasscodeDialog;
import com.safepayu.wallet.MainActivity;
import com.safepayu.wallet.Profile;
import com.safepayu.wallet.R;
import com.safepayu.wallet.fcm.FcmConfig;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.utils.PasscodeClickListener;
import com.safepayu.wallet.utils.PasscodeDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_APP_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CARD_CVV;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CARD_HOLDER;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CARD_MM;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CARD_NUMBER;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CARD_YYYY;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_NOTE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_PAYMENT_OPTION;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, CFClientInterface, PasscodeClickListener {
    private AlertDialog.Builder alertNetwork;
    private DrawerLayout drawer;
    private ImageView nav_icon, notification_icon;
    private LinearLayout addMoney, sendMoney, recharge, payBill, dth, payShop, sendToBank;
    private TextView walletMoney;
    ProgressBar progressBar;
    String str_wallet_money, user_id;
    private boolean doubleBackToExitPressedOnce = false;
    LinearLayout layout_electricity, layout_gas, layout_water, layout_broadband;
    // Tab titles
    private LinearLayout payLayout;
    private LinearLayout walletLayout;
    private LinearLayout send;
    private LinearLayout hotel_layout;
    private ProgressDialog update_dialog, progressDialog;
    private AlertDialog.Builder alert;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
TextView fcmToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        notification_icon = findViewById(R.id.notification);
        nav_icon = findViewById(R.id.nav_icon);
        nav_icon.setOnClickListener(nav_iconListner);
        progressDialog = new ProgressDialog(this);

        fcmToken = findViewById(R.id.fcmToken);
        fcmToken.setText(SharedPrefManager.getInstance(getApplicationContext()).getString("fcm_token"));
        findViewById(R.id.copyFcmToken).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyText();
            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        alertNetwork = new AlertDialog.Builder(Navigation.this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        GetAppUpdates getAppUpdates = new GetAppUpdates();
        getAppUpdates.execute();

        GetNotifications getNotifications = new GetNotifications();
        getNotifications.execute();


        if (SharedPrefManager.getInstance(Navigation.this).getUser().getPasscode() == null || SharedPrefManager.getInstance(Navigation.this).getUser().getPasscode().equals("")) {
            CreatePasscodeDialog dialog = new CreatePasscodeDialog(Navigation.this);
            dialog.show();
        }
        if (!isNetworkAvailable()) {
            showAlert();
        }
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(FcmConfig.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(FcmConfig.TOPIC_GLOBAL);
                } else if (intent.getAction().equals(FcmConfig.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };

        new sendFcmTokenToServer().execute();

        walletMoney = findViewById(R.id.wallet_amount);
        addMoney = findViewById(R.id.layout_add_money);
        sendMoney = findViewById(R.id.layout_send_money);
        progressBar = findViewById(R.id.home_wallet_prograssbar);
        recharge = findViewById(R.id.layout_recharge);
        payBill = findViewById(R.id.layout_pay_bill);
        dth = findViewById(R.id.layout_dth);
        send = findViewById(R.id.send);
        payShop = findViewById(R.id.pay_shop);
        sendToBank = findViewById(R.id.layout_send_bank);
        LoginUser user = SharedPrefManager.getInstance(this).getUser();
        payLayout = findViewById(R.id.pay_layout);
        user_id = user.getUserid();
        addMoney.setOnClickListener(this);
        sendMoney.setOnClickListener(this);
        recharge.setOnClickListener(this);
        payBill.setOnClickListener(this);
        dth.setOnClickListener(this);
        payShop.setOnClickListener(this);
        sendToBank.setOnClickListener(this);
        WalletAmount walletAmount = new WalletAmount();
        walletAmount.execute();

        walletLayout = findViewById(R.id.wallet_layout);
        walletLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Navigation.this, WalletActivity.class));
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SendMoneyActivity.class));
            }
        });
        layout_electricity = findViewById(R.id.layout_electricity);
        layout_gas = (LinearLayout) findViewById(R.id.layout_gas_recharge);
        layout_water = (LinearLayout) findViewById(R.id.layout_water);
        layout_broadband = (LinearLayout) findViewById(R.id.layout_broadband);
        layout_electricity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Navigation.this, ElectricityRecharge.class));
            }
        });
        payLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasscodeDialog passcodeDialog = new PasscodeDialog(Navigation.this, Navigation.this, "");
                passcodeDialog.show();


            }
        });
        layout_gas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Navigation.this, Gas.class));
            }
        });
        layout_water.setOnClickListener(this);
        layout_broadband.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Navigation.this, PostpaidBillpay.class));
            }
        });

        hotel_layout = findViewById(R.id.hotel_layout);

        hotel_layout.setOnClickListener(this);
    }

    private View.OnClickListener nav_iconListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            drawer.openDrawer(GravityCompat.START);

        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(FcmConfig.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(FcmConfig.PUSH_NOTIFICATION));

        if (!isNetworkAvailable()) {

            //alertNetwork.show();
            alertNetwork.setTitle("No Network");
            DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Navigation.super.onRestart();
                }
            };
            alertNetwork.setPositiveButton("Try again", onClickListener);

            AlertDialog alert = alertNetwork.create();
            alert.show();

        }


//        PasscodeDialog passcodeDialog = new PasscodeDialog(Navigation.this, Navigation.this, "");
//        passcodeDialog.show();


    }
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    @Override
    protected void onRestart() {
        super.onRestart();

        if (!isNetworkAvailable()) {

            //alertNetwork.show();
            alertNetwork.setTitle("No Network");
            DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Navigation.super.onRestart();
                }
            };
            alertNetwork.setPositiveButton("Try again", onClickListener);

            AlertDialog alert = alertNetwork.create();
            alert.show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action

        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(Navigation.this, Profile.class));
        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(Navigation.this, PackageDetailsActivity.class));
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(Navigation.this, Membership.class));
        } else if (id == R.id.wallet) {
            startActivity(new Intent(Navigation.this, WalletActivity.class));
        } else if (id == R.id.shoping) {
            Toast.makeText(this, "Coming Sooon", Toast.LENGTH_SHORT).show();
            drawer.closeDrawers();
        } else if (id == R.id.orders) {
            Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_LONG).show();
            // startActivity(new Intent(Navigation.this, OrderActivity.class));
        } else if (id == R.id.passcode) {
            startActivity(new Intent(Navigation.this, ChangePasscodeActivity.class));
        } else if (id == R.id.history) {
            startActivity(new Intent(Navigation.this, HistoryActivity.class));
        } else if (id == R.id.geneology) {
            Intent intent = new Intent(getApplicationContext(), GeneologyActivity.class);
            startActivity(intent);
        } else if (id == R.id.refer) {
            Intent intent = new Intent(getApplicationContext(), ReferralActivity.class);
            startActivity(intent);

        } else if (id == R.id.kyc) {

        } else if (id == R.id.logout) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            Toast.makeText(getApplicationContext(), "Log out", Toast.LENGTH_LONG).show();
            SharedPrefManager.getInstance(getApplicationContext()).logout();
            startActivity(intent);
            finish();

        } else if (id == R.id.contact) {
            Intent intent = new Intent(getApplicationContext(), ContactUs.class);
            startActivity(intent);
        } else if (id == R.id.commission) {
            Intent intent = new Intent(getApplicationContext(), Commission.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    void showAlert() {
        //alertNetwork.show();
        alertNetwork.setTitle("No Network");
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isNetworkAvailable()) {
                    showAlert();
                }
            }
        };
        alertNetwork.setPositiveButton("Try again", onClickListener);

        AlertDialog alert = alertNetwork.create();
        alert.show();
    }

    @Override
    public void onClick(View view) {
        LinearLayout l = (LinearLayout) view;
        switch (l.getId()) {
            case R.id.layout_add_money:
                //startActivity(new Intent(this, AddmoneyThroughcall.class));
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_send_money:
//                Toast.makeText(getApplicationContext(), "train ticket", Toast.LENGTH_LONG).show();
                //triggerPayment(true);
                //startActivity(new Intent(this, SendMoney.class));
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_recharge:
                startActivity(new Intent(this, MobileRecharge.class));
                break;
            case R.id.layout_pay_bill:
                startActivity(new Intent(this, PostpaidBillpay.class));
                break;
            case R.id.layout_dth:
                startActivity(new Intent(this, Dth.class));
                break;
            case R.id.pay_shop:
                //startActivity(new Intent(this, SendMoney.class));
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_send_bank:
                //startActivity(new Intent(this, SendToBank.class));
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_water:
                //startActivity(new Intent(this, SendToBank.class));
                startActivity(new Intent(this, Water.class));
                break;
            case R.id.hotel_layout:
                //startActivity(new Intent(this, SendToBank.class));
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
            /*case R.id.nav_icon:
                drawer.openDrawer(GravityCompat.START);
                break;*/
        }
    }

    @Override
    public void onPasscodeMatch(boolean isPasscodeMatched) {
        if (isPasscodeMatched){
            startActivity(new Intent(getApplicationContext(), SendMoney.class));
        }
    }


    class WalletAmount extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("userid", SharedPrefManager.getInstance(Navigation.this).getUser().getUserid());
            //params.put("password", str_edit_password);
            return requestHandler.sendPostRequest(URLs.URL_GET_WALLET, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("WALLET", s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("msg");
                    walletMoney.setText(jsonObject1.optString("amount"));
                }
            } catch (Exception e) {
            }

        }
    }

    private void triggerPayment(boolean isUpiIntent) {
        /*
         * token can be generated from your backend by calling cashfree servers. Please
         * check the documentation for details on generating the token.
         * READ THIS TO GENERATE TOKEN: https://bit.ly/2RGV3Pp
         */
        //String token = "N59JCN4MzUIJiOicGbhJCLiQ1VKJiOiAXe0Jye.wC9JiM0YGZ0UmM2QTY5MWNiojI0xWYz9lIsIDO2QTM2MTN1EjOiAHelJCLiIlTJJiOik3YuVmcyV3QyVGZy9mIsICM1IiOiQnb19WbBJXZkJ3biwiI2UDNzITMiojIklkclRmcvJye.5GDXBNHNOKFnS5uhLP2EbyJ2MiSnhrop_ElD1JlCB-XO0qWS90sCgS617lZfAkoFr9";
        String token = "tN9JCN4MzUIJiOicGbhJCLiQ1VKJiOiAXe0Jye.PG0nIwUzYjhjY3AzMllzY1IiOiQHbhN3XiwSM3ITM3gzM1UTM6ICc4VmIsIiUOlkI6ISej5WZyJXdDJXZkJ3biwiI1IiOiQnb19WbBJXZkJ3biwiI1ITMzITMzITMiojIklkclRmcvJye.Qn1-1JGbJAxWs6S-IHWjcyMhmZKwtvDiX5Z1H_YMhDFUf-I1smOy-nU2F7MnqdukaC";


        /*
         * stage allows you to switch between sandboxed and production servers
         * for CashFree Payment Gateway. The possible values are
         *
         * 1. TEST: Use the Test server. You can use this service while integrating
         *      and testing the CashFree PG. No real money will be deducted from the
         *      cards and bank accounts you use this stage. This mode is thus ideal
         *      for use during the development. You can use the cards provided here
         *      while in this stage: https://docs.cashfree.com/docs/resources/#test-data
         *
         * 2. PROD: Once you have completed the testing and integration and successfully
         *      integrated the CashFree PG, use this value for stage variable. This will
         *      enable live transactions
         */
        String stage = "PROD";

        /*
         * appId will be available to you at CashFree Dashboard. This is a unique
         * identifier for your app. Please replace this appId with your appId.
         * Also, as explained below you will need to change your appId to prod
         * credentials before publishing your app.
         */
        String appId = "93391d04c9a85fcdcd6c67d29339";
        String orderId = "123123125";
        String orderAmount = "5";
        String orderNote = "Test Order";
        String customerName = "arif";
        String customerPhone = "7861056786";
        String customerEmail = "mdalisharique@gmail.com";

        Map<String, String> params = new HashMap<>();
        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, orderId);
        params.put(PARAM_ORDER_AMOUNT, orderAmount);
        params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, customerName);
        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
        params.put(PARAM_PAYMENT_OPTION, "card");
        params.put(PARAM_CARD_NUMBER, "4691980003637641");//Replace Card number
        params.put(PARAM_CARD_MM, "03"); // Card Expiry Month in MM
        params.put(PARAM_CARD_YYYY, "2022"); // Card Expiry Year in YYYY
        params.put(PARAM_CARD_HOLDER, "Arif ali"); // Card Holder name
        params.put(PARAM_CARD_CVV, "755");


        for (Map.Entry entry : params.entrySet()) {
            Log.d("CFSKDSample", entry.getKey() + " " + entry.getValue());
        }

        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);
        cfPaymentService.doPayment(this, params, token, this, stage);

        /*if (isUpiIntent) {
            // Use the following method for initiating UPI Intent Payments
            cfPaymentService.upiPayment(this, params, token, this, stage);
        }
        else {
            // Use the following method for initiating regular Payments
            cfPaymentService.doPayment(this, params, token, this, stage);
        }*/

    }

    public void doPayment(Context context, Map<String, String> params, String token, CFClientInterface callback, String stage) {
        this.triggerPayment(true);
    }

    @Override
    public void onSuccess(Map<String, String> params) {

    }

    @Override
    public void onFailure(Map<String, String> params) {

    }

    @Override
    public void onNavigateBack() {

    }


    class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {

        public Activity c;

        public Dialog d;
        public Button updateBtn, no;

        public CustomDialogClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_update_dialog);
            updateBtn = (Button) findViewById(R.id.btn_update_app);
            updateBtn.setOnClickListener(this);


            //no.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_update_app:
                    c.finish();

                    final String appPackageName = getPackageName(); // getPackageName() from Context or RechargeFailed object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }


    //    class AlertNetworkDialog extends Dialog implements android.view.View.OnClickListener {
//
//        public Activity c;
//
//        public Dialog d;
//        public Button updateBtn, no;
//
//        public AlertNetworkDialog(Activity a) {
//            super(a);
//            // TODO Auto-generated constructor stub
//            this.c = a;
//        }
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//            setContentView(R.layout.custom_update_dialog);
//            updateBtn = (Button) findViewById(R.id.btn_update_app);
//            updateBtn.setOnClickListener(this);
//            //no.setOnClickListener(this);
//
//        }
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.btn_update_app:
//                    c.finish();
//
//                    final String appPackageName = getPackageName(); // getPackageName() from Context or RechargeFailed object
//                    try {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//                    } catch (android.content.ActivityNotFoundException anfe) {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//                    }
//                    break;
//                default:
//                    break;
//            }
//            dismiss();
//        }
//    }
//


    class GetNotifications extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressbar.setVisibility(View.VISIBLE);
            Log.e("Notification", "aaaaaaaaaa");
            Log.e("Notification", "zzzzzzzzzzz");

        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("userid", user_id);
            //params.put("password", str_edit_password);
            //Log.e("Notification", "bbbbbbbbb");
            return requestHandler.sendPostRequest(URLs.URL_Get_Address, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println(s);
            Log.e("Notification", "ccccccccccc");
            Log.e("Notification", s);
            Log.e("Notification", "ffffffffff");
            String mss = null;
            if (mss == null) {

            }

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("Failed")) {
                    notification_icon.setImageResource(R.drawable.bell6);
                    notification_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                        }
                    });

                    startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                    finish();
                }
            } catch (Exception e) {
            }

        }


    }

    class GetAppUpdates extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressbar.setVisibility(View.VISIBLE);
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Looking for updates");
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            //  params.put("userid", SharedPrefManager.getInstance(Homepage.this).getUser().getUserid());
            //params.put("password", str_edit_password);
            return requestHandler.sendPostRequest(URLs.URL_GET_APP_UPDATE, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println(s);
            Log.e("UPDATE", "mmmmmmmmmmm");

            Log.e("UPDATE", s);
            Log.e("UPDATE", "kkkkkkkkkkk");
            progressDialog.dismiss();

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {


                    //Version 1.1.0 , versionCode 10 ==> if update for 9/10 then update
                    int msg = Integer.parseInt(jsonObject.optString("msg"));

                    if (msg >= 18) {
                        CustomDialogClass cdd = new CustomDialogClass(Navigation.this);
                        cdd.show();
                    }


                }
            } catch (Exception e) {
            }

        }
    }
    class sendFcmTokenToServer extends AsyncTask<Void, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("userid", SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserid());
            params.put("mobilekey", SharedPrefManager.getInstance(getApplicationContext()).getString("fcm_token"));

            return requestHandler.sendPostRequest(URLs.UPDATE_FCM_TOKEN, params);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("Responce",s);
        }
    }


    private void copyText(){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", fcmToken.getText().toString());
        clipboard.setPrimaryClip(clip);
    }
}
