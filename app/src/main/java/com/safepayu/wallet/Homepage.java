package com.safepayu.wallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.safepayu.wallet.Activity.Broadband;
import com.safepayu.wallet.Activity.Dth;
import com.safepayu.wallet.Activity.ElectricityRecharge;
import com.safepayu.wallet.Activity.Gas;
import com.safepayu.wallet.Activity.MobileRecharge;
import com.safepayu.wallet.Activity.PostpaidBillpay;
import com.safepayu.wallet.Activity.RechargeHistory;
import com.safepayu.wallet.Activity.SendMoney;
import com.safepayu.wallet.Activity.WalletActivity;
import com.safepayu.wallet.Activity.Water;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.Fragments.History;
import com.safepayu.wallet.HisotyPackage.WalletHistory;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CARD_CVV;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CARD_HOLDER;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CARD_MM;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CARD_NUMBER;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CARD_YYYY;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_PAYMENT_OPTION;


public class Homepage extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, CFClientInterface {
    private LinearLayout addMoney, sendMoney, recharge, payBill, dth, payShop, sendToBank;
    private TextView walletMoney;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    String str_wallet_money, user_id;
    private boolean doubleBackToExitPressedOnce = false;
    LinearLayout layout_electricity, layout_gas, layout_water, layout_broadband;
    ImageView back_btn_from_homepage;
    private ActionBar actionBar;
    // Tab titles
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ImageView nav_icon;
    private LinearLayout payLayout;
    private LinearLayout walletLayout;
    private ImageView bellIcon;
    private ProgressDialog update_dialog;
    private AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        nav_icon = findViewById(R.id.nav_icon);
        nav_icon.setOnClickListener(nav_iconListner);
        progressDialog = new ProgressDialog(this);
        walletMoney = (TextView) findViewById(R.id.wallet_amount);
        addMoney = (LinearLayout) findViewById(R.id.layout_add_money);
        sendMoney = (LinearLayout) findViewById(R.id.layout_send_money);
        progressBar = (ProgressBar) findViewById(R.id.home_wallet_prograssbar);
        recharge = (LinearLayout) findViewById(R.id.layout_recharge);
        payBill = (LinearLayout) findViewById(R.id.layout_pay_bill);
        dth = (LinearLayout) findViewById(R.id.layout_dth);
        payShop = (LinearLayout) findViewById(R.id.pay_shop);
        sendToBank = (LinearLayout) findViewById(R.id.layout_send_bank);
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

        alert = new AlertDialog.Builder(Homepage.this);
        alert.setTitle("Update");
        alert.setMessage("Important Update");
        alert.show();


        lookForNotifications();
        lookForUpdates();
        CustomDialogClass cdd = new CustomDialogClass(Homepage.this);
        cdd.show();


        WalletAmount walletAmount = new WalletAmount();
        walletAmount.execute();





        walletLayout = findViewById(R.id.wallet_layout);
        walletLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), WalletActivity.class));

            }
        });

        layout_electricity = (LinearLayout) findViewById(R.id.layout_electricity);
        layout_gas = (LinearLayout) findViewById(R.id.layout_gas_recharge);
        layout_water = (LinearLayout) findViewById(R.id.layout_water);
        layout_broadband = (LinearLayout) findViewById(R.id.layout_broadband);
        layout_electricity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homepage.this, ElectricityRecharge.class));
            }
        });
        payLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SendMoney.class));
            }
        });
        layout_gas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homepage.this, Gas.class));
            }
        });
        layout_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homepage.this, Water.class));                //  startActivity(new Intent(Utilities.this,Water.class));
            }
        });
        layout_broadband.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Homepage.this, Broadband.class));
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.common_open_on_phone, R.string.common_open_on_phone);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        LinearLayout layout_home = headerview.findViewById(R.id.layout_home);
        layout_home.setOnClickListener(layout_homeListner);
        LinearLayout layout_myprofile = headerview.findViewById(R.id.layout_myprofile);
        layout_home.setOnClickListener(layout_myprofileListner);
        LinearLayout layout_membership = headerview.findViewById(R.id.layout_membership);
        layout_home.setOnClickListener(layout_membershipListner);
        LinearLayout layout_mywallet = headerview.findViewById(R.id.layout_mywallet);
        layout_home.setOnClickListener(layout_mywalletListner);
        LinearLayout layout_shopping = headerview.findViewById(R.id.layout_shopping);
        layout_home.setOnClickListener(layout_shoppingListner);
        LinearLayout layout_myorder = headerview.findViewById(R.id.layout_myorder);
        layout_home.setOnClickListener(layout_myorderListner);
        LinearLayout layout_history = headerview.findViewById(R.id.layout_history);
        layout_home.setOnClickListener(layout_historyListner);
        LinearLayout layout_genealogy = headerview.findViewById(R.id.layout_genealogy);
        layout_home.setOnClickListener(layout_genealogyListner);
        LinearLayout layout_updatekyc = headerview.findViewById(R.id.layout_updatekyc);
        layout_home.setOnClickListener(layout_updatekycListner);
        LinearLayout layout_logout = headerview.findViewById(R.id.layout_logout);
        layout_home.setOnClickListener(layout_logoutListner);
        navigationView.setNavigationItemSelectedListener(this);
        triggerPayment(true);
    }

    public void  lookForUpdates(){
        GetAppUpdates getAppUpdates = new GetAppUpdates();
        getAppUpdates.execute();
    }
    public void lookForNotifications(){
        Log.e("Notification", "bbbbbbbbbbbb");
        GetNotifications getNotifications= new GetNotifications();
        getNotifications.execute();
    }



    private View.OnClickListener layout_homeListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(getApplicationContext(), Homepage.class);
            startActivity(intent);
            finish();
            drawer.closeDrawer(GravityCompat.START);
        }
    };
    private View.OnClickListener nav_iconListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            drawer.openDrawer(GravityCompat.START);

        }
    };

    private View.OnClickListener layout_myprofileListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), Profile.class));
            drawer.closeDrawer(GravityCompat.START);
        }
    };

    private View.OnClickListener layout_membershipListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // startActivity(new Intent(getApplicationContext(), Profile.class));
        }
    };
    private View.OnClickListener layout_mywalletListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), History.class));
            drawer.closeDrawer(GravityCompat.START);
        }
    };

    private View.OnClickListener layout_shoppingListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // startActivity(new Intent(getApplicationContext(), Profile.class));
        }
    };
    private View.OnClickListener layout_myorderListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // startActivity(new Intent(getApplicationContext(), Profile.class));

        }
    };
    private View.OnClickListener layout_historyListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), RechargeHistory.class));
            drawer.closeDrawer(GravityCompat.START);
        }
    };
    private View.OnClickListener layout_genealogyListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // startActivity(new Intent(getApplicationContext(), Profile.class));
        }
    };
    private View.OnClickListener layout_updatekycListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // startActivity(new Intent(getApplicationContext(), Profile.class));
        }
    };
    private View.OnClickListener layout_logoutListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // startActivity(new Intent(getApplicationContext(), Profile.class));
        }
    };

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View view) {
        LinearLayout l = (LinearLayout) view;
        switch (l.getId()) {
            case R.id.layout_add_money:
                //startActivity(new Intent(this, AddmoneyThroughcall.class));
                break;
            case R.id.layout_send_money:
                Toast.makeText(getApplicationContext(), "train ticket", Toast.LENGTH_LONG).show();

                //startActivity(new Intent(this, SendMoney.class));
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
                break;
            case R.id.layout_send_bank:
                //startActivity(new Intent(this, SendToBank.class));
                break;
            /*case R.id.nav_icon:
                drawer.openDrawer(GravityCompat.START);
                break;*/
        }
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
            params.put("userid", SharedPrefManager.getInstance(Homepage.this).getUser().getUserid());
            //params.put("password", str_edit_password);
            return requestHandler.sendPostRequest(URLs.URL_GET_WALLET, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

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
            Log.i("UPDATE", s);
            progressDialog.dismiss();

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {

                    CustomDialogClass cdd = new CustomDialogClass(Homepage.this);
                    cdd.show();
                }
            } catch (Exception e) {
            }

        }
    }
    class GetNotifications extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressbar.setVisibility(View.VISIBLE);
            Log.e("Notification", "aaaaaaaaaa");

        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            params.put("userid", SharedPrefManager.getInstance(Homepage.this).getUser().getUserid());
            //params.put("password", str_edit_password);
            return requestHandler.sendPostRequest(URLs.URL_Get_Address, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println(s);
            Log.e("Notification", s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {


                }
            } catch (Exception e) {
            }

        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.layout_home) {
            Toast.makeText(getApplicationContext(), "this is home", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private void triggerPayment(boolean isUpiIntent) {
        /*
         * token can be generated from your backend by calling cashfree servers. Please
         * check the documentation for details on generating the token.
         * READ THIS TO GENERATE TOKEN: https://bit.ly/2RGV3Pp
         */
        String token = "AN9JCN4MzUIJiOicGbhJCLiQ1VKJiOiAXe0Jye.yuQfiIzN0ETYlBDNykTOjVjI6ICdsF2cfJCLwEDNwQTNzUTNxojIwhXZiwiIS5USiojI5NmblJnc1NkclRmcvJCLiADM0IiOiQnb19WbBJXZkJ3biwiI2UDNzITMiojIklkclRmcvJye.4gzxnqQ0XfKWMiMVNUwF642cmyPZDOuKef3AHxVg9_vetST9ZlNeUzenzbZ5Apztl0";


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
        String stage = "TEST";

        /*
         * appId will be available to you at CashFree Dashboard. This is a unique
         * identifier for your app. Please replace this appId with your appId.
         * Also, as explained below you will need to change your appId to prod
         * credentials before publishing your app.
         */
        String appId = "YOUR_APP_ID_HERE";
        String orderId = "Order0001";
        String orderAmount = "1";
        String orderNote = "Test Order";
        String customerName = "John Doe";
        String customerPhone = "9900012345";
        String customerEmail = "test@gmail.com";

        Map<String, String> params = new HashMap<>();

        params.put(PARAM_PAYMENT_OPTION, "card");
        params.put(PARAM_CARD_NUMBER, "4444333322221111");//Replace Card number
        params.put(PARAM_CARD_MM, "07"); // Card Expiry Month in MM
        params.put(PARAM_CARD_YYYY, "2023"); // Card Expiry Year in YYYY
        params.put(PARAM_CARD_HOLDER, "Test"); // Card Holder name
        params.put(PARAM_CARD_CVV, "123");


        for(Map.Entry entry : params.entrySet()) {
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

    /*public void upiPayment(View view) {
        this.triggerPayment(true);
    }*/


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
}
