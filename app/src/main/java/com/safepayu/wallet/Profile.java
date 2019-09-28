package com.safepayu.wallet;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.safepayu.wallet.Activity.AddressActivity;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManagerLogin;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.ConnectionPackage.VolleyMultipartRequest;
import com.safepayu.wallet.model.ImagePicker;
import com.safepayu.wallet.model.LoginUser;
import com.safepayu.wallet.model.RememberPassword;
import com.safepayu.wallet.R;
import com.safepayu.wallet.model.SuggesedAddressModel;
import com.safepayu.wallet.utils.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Profile extends AppCompatActivity {
    ProgressDialog pdLoading;
    Bitmap bitmap;
    byte[] byteUrlimg;
    private static final String TAG = Profile.class.getSimpleName();
    private static final int PICK_IMAGE_ID = 234;
    Button back, logout, done, submitpassword, addressupdatebtn;
    EditText name, address, pincode, gender, city, state, newpassword,oldpassword,confirmpassword;
    TextView email;
    TextView dob;
    TextView mobile;
    TextView address1;
    TextView pincode1;
    TextView packagename;
    TextView packagestatus;
    TextView packageamount;
    TextView totalamount;
    ImageView imagePicker;
    String strImageUri = null;
    int int_id, i;
    String id, date, str_name, str_mobile, str_pincode, str_city, str_state, str_email, str_address, str_dob, str_gender, str_contury = "India";
    de.hdodenhof.circleimageview.CircleImageView profileImage;
    private String fullName;
    private String phone;
    private String email_id;
    private TextView username1;
    List<SuggesedAddressModel> addressModels = new ArrayList<>();
    LoadingDialog dialog;
    String searchedAddress[];

    ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        pdLoading = new ProgressDialog(Profile.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        back = (Button) findViewById(R.id.backbtn_from_home);
        logout = (Button) findViewById(R.id.logout);
        addressupdatebtn= (Button) findViewById(R.id.p_addressupdate);
        submitpassword = (Button) findViewById(R.id.change_pass_submit);
        packagename=(TextView) findViewById(R.id.package_name_id);
        packagestatus=(TextView) findViewById(R.id.package_status_id);
        packageamount=(TextView) findViewById(R.id.package_amount_id);
        totalamount=(TextView) findViewById(R.id.total_amount_id);
        done = (Button) findViewById(R.id.correct);
        name = (EditText) findViewById(R.id.p_name);
        newpassword = (EditText) findViewById(R.id.new_password_id);
        oldpassword = (EditText) findViewById(R.id.old_password_id);
        confirmpassword = (EditText) findViewById(R.id.confirm_password_id);
        username1 = findViewById(R.id.username1);
        //pincode=(EditText)findViewById(R.id.p_pincode);
        mobile = (TextView) findViewById(R.id.p_mobile);
        //altermobile=(EditText)findViewById(R.id.p_altermobile);
        email = (TextView) findViewById(R.id.p_mail);
        //address=(EditText)findViewById(R.id.p_address);
        dob = findViewById(R.id.p_dob);
        address1 = findViewById(R.id.p_address);
        pincode1 = findViewById(R.id.p_pincode);
        //gender=(EditText)findViewById(R.id.p_gender);
        //city=(EditText)findViewById(R.id.p_city);
        //state=(EditText)findViewById(R.id.p_state);
        imagePicker = (ImageView) findViewById(R.id.image_picker);
        profileImage = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profile_img);
        final LinearLayout change_password_dialog = (LinearLayout) findViewById(R.id.change_pass_layout);
        LinearLayout change_password_button =(LinearLayout) findViewById(R.id.change_pass_button);
        LinearLayout hide_password_dialog =(LinearLayout) findViewById(R.id.externallayout);
        LoginUser user = SharedPrefManager.getInstance(this).getUser();
        id = user.getUserid();
        //int_id=Integer.parseInt(id);
        date = user.getDob();
        dob.setText(date);
        fullName = user.getFirst_name() + " " + user.getLast_name();
        name.setText(fullName);
        username1.setText(fullName);
        phone = user.getMobile();
        mobile.setText(phone);
        email_id = user.getEmail();
        email.setText(email_id);
        signIn();
        CheckPackage checkPackage=new CheckPackage();
        checkPackage.execute();
        GetAddress getAddress=new GetAddress();
        getAddress.execute();
       /* Picasso.with(this).load(user.getImage()).placeholder(R.drawable.profile_logo).error(R.drawable.profile_bg_top).into(profileImage);
        name.setText(user.getFull_name());
        city.setText(user.getCity());
        mobile.setText(user.getMobile_number());
       pincode.setText(user.getPincode());
        email.setText(user.getEmail_id());
        address.setText(user.getAddress());
        dob.setText(user.getD_o_b());
        state.setText(user.getState());
        gender.setText(user.getSex());*/
        //  occupation.setText(user.getOccupation());
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadBitmap(bitmap);
                // updateProfile();
            }
        });
      /*  profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("this profile imageeeeeeeeeeeeeee jawedddddddddddddd");
                Intent intent=new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"complete Action using"),100);
            }
        });*/
        addressupdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(getApplicationContext(), AddressActivity.class));
                Intent i = new Intent(getApplicationContext(), AddressActivity.class);
                i.putExtra("Addressupdate", true);
                startActivity(i);
            }
        });
        imagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickImage();
            }
        });
        change_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_password_dialog.setVisibility(View.VISIBLE);

            }
        });
        hide_password_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_password_dialog.setVisibility(View.INVISIBLE);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                // startActivity(new Intent(Profile.this,MainActivity.class));
            }

        });
//        dob.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Get Current Date
//                final Calendar c = Calendar.getInstance();
//              int  mYear = c.get(Calendar.YEAR);
//                int mMonth = c.get(Calendar.MONTH);
//                int  mDay = c.get(Calendar.DAY_OF_MONTH);
//
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(Profile.this,
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//
//                                dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                            }
//                        }, mYear, mMonth, mDay);
//                datePickerDialog.show();
//            }
//
//
//
//
//
//        });
        submitpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userid = SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserid();
                final String password=SharedPrefManagerLogin.getInstance(getApplicationContext()).getUser().getPassword();
                final String oldpasswords=oldpassword.getText().toString();
                final String newpasswords=newpassword.getText().toString();
                final String confirmpasswords=confirmpassword.getText().toString();

                if (password.equals(oldpasswords)){
                    if (newpasswords.equals(confirmpasswords)) {
                        changepass(userid, newpasswords);
                    }else {
                        Toast.makeText(getApplicationContext(), "New Password not matched with Confirm Password", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Old Password didn't matched", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

        private void changepass(final String userid, final String password) {


            class PasswordChange extends AsyncTask<Void, Void, String> {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pdLoading.setMessage("\t Loading...");
                    pdLoading.show();
                    pdLoading.setCancelable(false);
                    //progressbar.setVisibility(View.VISIBLE);
                }

                @Override
                protected String doInBackground(Void... voids) {
                            RequestHandler requestHandler = new RequestHandler();
                            HashMap<String, String> params = new HashMap<>();
                          //  Log.e("ggggggggggggg",userid);
                          //  System.out.println("Userid: -"+id);
                            params.put("userid", userid);
                            params.put("password", password);
                            // params.put("otp",otpget);
//                            Toast.makeText(getApplicationContext(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                             return  requestHandler.sendPostRequest(URLs.URL_CHANGE_PASSWORD, params);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    pdLoading.dismiss();
                    Log.e("aaaaaaaaaaa",s);

                    try {
                        JSONObject res = new JSONObject(s);
                        if(res.optString("status").equalsIgnoreCase("success")){

                            Toast.makeText(getApplicationContext(),"Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                SharedPrefManagerLogin.getInstance(getApplicationContext()).changePassword(password);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println(s);
                }
            }
            PasswordChange ul = new PasswordChange();
            ul.execute();

        }

    public void onPickImage() {
        // Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        //startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_ID && resultCode == RESULT_OK) {
            // filePath = data.getData();
            try {
                Uri uri = data.getData();
                System.out.println("uuuurrrriiiiiii" + uri);
                bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                profileImage.setImageBitmap(bitmap);
                //  strImageUri=  getStringImage(bitmap);
                System.out.println("strImageUri ee wwwwwww: " + bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //https://www.androstock.com/tutorials/create-a-file-uploader-with-progress-in-android-android-studio.html
        //https://www.simplifiedcoding.net/android-upload-image-to-server-using-php-mysql/
       /* switch(requestCode) {
            case PICK_IMAGE_ID:
                 bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                // TODO use bitmap
                System.out.println("bitmappp: "+bitmap);
                profileImage.setImageBitmap(bitmap);
                getStringImage(bitmap);
               // byteUrlimg=  getFileDataFromDrawable(bitmap);
                // strImageUri = new String(byteUrlimg);
                //System.out.println("profile image urlllllll:  "+byteUrlimg);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }*/
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        byte[] imageBytes = baos.toByteArray();
        String strImagebase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        System.out.println("encodedImage wwwwwww: " + strImagebase64);
        return strImagebase64;
    }


    private void uploadBitmap(final Bitmap bitmap) {

        str_name = name.getText().toString().trim();
        str_city = city.getText().toString().trim();
        str_pincode = pincode.getText().toString().trim();
        str_email = email.getText().toString().trim();
        str_address = address.getText().toString().trim();
        str_dob = dob.getText().toString().trim();
        str_state = state.getText().toString().trim();
        str_gender = gender.getText().toString().trim();
        str_mobile = mobile.getText().toString().trim();

        if (TextUtils.isEmpty(str_name)) {
            name.setError("Please enter Full Name");
            name.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {
            email.setError("Enter a valid email");
            email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_city)) {
            city.setError("Please enter city");
            city.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_pincode)) {
            pincode.setError("Please enter pincode");
            pincode.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_address)) {
            address.setError("Please enter address");
            address.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_dob)) {
            dob.setError("Please enter dob");
            dob.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_state)) {
            state.setError("Please enter state");
            state.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_gender)) {
            gender.setError("Please enter gender");
            gender.requestFocus();
            return;
        }
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLs.URL_EDIT_PROFILE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        // pdLoading.dismiss();
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // pdLoading.dismiss();
                        Toast.makeText(getApplicationContext(), "plz select Image", Toast.LENGTH_SHORT).show();
                        // error.getMessage()
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //     pdLoading.setMessage("\tLoading...");
//                pdLoading.show();
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("full_name", str_name);
                params.put("address", str_address);
                params.put("email_id", str_email);
                params.put("d_o_b", str_dob);
                params.put("sex", str_gender);
                params.put("zip_code", str_pincode);
                params.put("city", str_city);
                params.put("state", str_state);
                params.put("country", str_contury);
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis() + int_id;
                params.put("image", new DataPart(imagename + ".jpeg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
    class GetAddress extends AsyncTask<Void, Void, String> {
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
            params.put("userid", id);
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
            String mss=null;
            if (mss==null){

            }

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {
                    JSONArray userJson = jsonObject.getJSONArray("msg");
                    JSONObject user = userJson.getJSONObject(0);
                    String location=user.getString("location");
                    String city=user.getString("city");
                    String state=user.getString("state");
                    String pincode=user.getString("pin");
                    Log.e("City",city);
                    address1.setText(location+","+" "+city+","+" "+state);
                    pincode1.setText(pincode);
                }
            } catch (Exception e) {
                Log.e("Exception",e.toString());
            }

        }
    }
    class CheckPackage extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //otp = otpNum;
            //progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();
            HashMap<String, String> params = new HashMap<>();
            System.out.println("Userid: -"+id);
            params.put("userid", id);
            // params.put("otp",otpget);
            return requestHandler.sendPostRequest(URLs.URL_GET_PACKAGES, params);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("ERRORFORCHECKMOBILE", s);
            System.out.print("BLAAAAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHH");
            System.out.print(s.getClass());

            //progressbar.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.optString("status").equalsIgnoreCase("Success")) {
                    // ValidateOtp.Registeration registeration = new ValidateOtp.Registeration();
                    //registeration.execute();
                    // Toast.makeText(getApplicationContext(), "User Mobile already exists", Toast.LENGTH_SHORT).show();
                    // editmobile.setText("");
                   // signIn();

                    JSONObject userJson = jsonObject.getJSONObject("msg");
                    String package_name=userJson.getString("package_name");
                    String package_status=userJson.getString("status");
                    String package_amount=userJson.getString("package_amount");
                    String total_amount=userJson.getString("total_amount");

                    packagename.setText(package_name);
                    packagestatus.setText(package_status);
                    packageamount.setText("₹ "+package_amount);
                    totalamount.setText("₹ "+total_amount);
                    if (package_status.equalsIgnoreCase("Approved")){
                        packagestatus.setTextColor( Color.GREEN);
                    }
                    else if (package_status.equalsIgnoreCase("Rejected")){
                        packagestatus.setTextColor( Color.RED);
                    }
                    else{
                        packagestatus.setTextColor(Color.YELLOW);
                    }





                    // Log.e("ERROR FOR CHECK MOBILE", s);
                } else {
                    // Log.e("ERROR FOR CHECK MOBILE", s);

                  //  Toast.makeText(getApplicationContext(), "User Not Registered", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    private void signIn() {

        RememberPassword rememberPassword = SharedPrefManagerLogin.getInstance(this).getUser();
        final String rememberMobile = rememberPassword.getMobile();
        final String remember_password = rememberPassword.getPassword();
        class UserLogin extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pdLoading.setMessage("\t Loading...");
                pdLoading.show();
                pdLoading.setCancelable(false);
                //progressbar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("mobile", rememberMobile);
                params.put("password", remember_password);
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //  progressbar.setVisibility(View.GONE);
                pdLoading.dismiss();
                try {
                    JSONObject obj = new JSONObject(s);
                    if (obj.getString("status").equals("Success")) {
                        JSONArray userJson = obj.getJSONArray("msg");
                        LoginUser user = null;
                        for (int i = 0; i < userJson.length(); i++) {
                            JSONObject jsonObject = userJson.getJSONObject(i);
                            user = new LoginUser();
                            user.setUserid(jsonObject.optString("userid"));
                            user.setFirst_name(jsonObject.optString("first_name"));
                            user.setLast_name(jsonObject.optString("last_name"));
                            user.setEmail(jsonObject.optString("email"));
                            user.setMobile(jsonObject.optString("mobile"));
                            user.setDob(jsonObject.optString("dob"));
                            user.setReferral_code(jsonObject.optString("referral_code"));
                        }
/*
                        JSONObject userJson = obj.getJSONObject("data");
                        User user1 = new User();
                        user1.setImage(userJson.getString("image"));
                        user1.setFull_name(userJson.getString("full_name"));
                        user1.setMobile_number(userJson.getString("mobile_number"));
                        user1.setAddress(userJson.getString("address"));
                        user1.setEmail_id(userJson.getString("email_id"));
                        user1.setD_o_b(userJson.getString("d_o_b"));
                        user1.setDate(userJson.getString("date"));
                        user1.setSex(userJson.getString("sex"));
                        user1.setPincode(userJson.getString("zip_code"));
                        user1.setCity(userJson.getString("city"));
                        user1.setState(userJson.getString("state"));
                        user1.setContry(userJson.getString("country"));

                        String imageUrl = user1.getImage();
*/
/*
                        if (imageUrl != null) {
                            Picasso.with(getApplicationContext()).load(imageUrl).placeholder(R.drawable.profile_logo).error(R.drawable.profile_bg_top).into(profileImage);
                        }
*/
                        profileImage.setImageResource(R.drawable.profile_logo);
                        //Picasso.with(this).load(user1.getImage()).placeholder(R.drawable.profile_logo).error(R.drawable.profile_bg_top).into(profileImage);
                        name.setText(user.getFirst_name());
                        mobile.setText(user.getMobile());
                        //address.setText(user1.getAddress());
                        email.setText(user.getEmail());
                        dob.setText(user.getDob());
                        //date.setText(user1.getDate());
//                        gender.setText(user1.getSex());
//                        pincode.setText(user1.getPincode());
//                        city.setText(user1.getCity());
//                        state.setText(user1.getState());
//                        //cont.setText(user1.getAddress());
                        Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
                        //  finish();
                        //  startActivity(new Intent(getApplicationContext(),Profile.class));
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        UserLogin ul = new UserLogin();
        ul.execute();

    }

    private void updateProfile() {
        str_name = name.getText().toString().trim();
        str_city = city.getText().toString().trim();
        str_pincode = pincode.getText().toString().trim();
        str_email = email.getText().toString().trim();
        str_address = address.getText().toString().trim();
        str_dob = dob.getText().toString().trim();
        str_state = state.getText().toString().trim();
        str_gender = gender.getText().toString().trim();
        str_mobile = mobile.getText().toString().trim();

        if (TextUtils.isEmpty(str_name)) {
            name.setError("Please enter Full Name");
            name.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {
            email.setError("Enter a valid email");
            email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_city)) {
            city.setError("Please enter city");
            city.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_pincode)) {
            pincode.setError("Please enter pincode");
            pincode.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_address)) {
            address.setError("Please enter address");
            address.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_dob)) {
            dob.setError("Please enter dob");
            dob.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_state)) {
            state.setError("Please enter state");
            state.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(str_gender)) {
            gender.setError("Please enter gender");
            gender.requestFocus();
            return;
        }
        class updateProfile extends AsyncTask<Void, Void, String> {

            ProgressDialog pdLoading = new ProgressDialog(Profile.this);
            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pdLoading.setMessage("\tLoading...");
                pdLoading.show();
                System.out.println("reconstitutedStringsssssssssssssssssssssssss ");
                //pdLoading.setCancelable(true);

            }

            @Override
            protected String doInBackground(Void... voids) {


//                Bitmap bitmap = voids[0];
                //   String uploadImage = getStringImage(bitmap);
                //   System.out.println("uploadImage------ "+uploadImage);
                RequestHandler requestHandler = new RequestHandler();
                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("full_name", str_name);
                params.put("address", str_address);
                params.put("email_id", str_email);
                params.put("d_o_b", str_dob);
                params.put("sex", str_gender);
                params.put("zip_code", str_pincode);
                params.put("city", str_city);
                params.put("state", str_state);
                params.put("country", str_contury);
                params.put("image", strImageUri);
                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_EDIT_PROFILE, params);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pdLoading.dismiss();
                try {
                    JSONObject obj = new JSONObject(s);

                    if (obj.getString("status").equals("TRUE")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                        SharedPrefManager.getInstance(getApplicationContext()).logoutprofile();
                        // User user =new User(id,image,str_name,str_mobile,str_address,str_email,str_dob,date,str_gender,str_pincode,str_city,str_state,str_contury);
                        // SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                        startActivity(new Intent(getApplicationContext(), Profile.class));

                        Toast.makeText(getApplicationContext(), "Update SuccessFully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error occur", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        updateProfile ru = new updateProfile();
        ru.execute();
    }


}






// Picasso.with(context).load(user.getCat_image()).placeholder(R.drawable.default_product_img).error(R.drawable.no_internet_connection).into(myHolder.cat_image);
/*
profileImage.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        System.out.println("this profile imageeeeeeeeeeeeeee jawedddddddddddddd");
        Intent intent=new Intent();
        intent.setType("image*/
/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"complete Action using"),100);
        }
        });*/
