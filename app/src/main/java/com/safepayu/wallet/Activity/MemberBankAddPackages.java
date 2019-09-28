package com.safepayu.wallet.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.safepayu.wallet.APIClient;
import com.safepayu.wallet.APIInterface;
import com.safepayu.wallet.ConnectionPackage.RequestHandler;
import com.safepayu.wallet.ConnectionPackage.SharedPrefManager;
import com.safepayu.wallet.ConnectionPackage.URLs;
import com.safepayu.wallet.PackageRequestProcessing;
import com.safepayu.wallet.R;
import com.safepayu.wallet.SendPackageModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.safepayu.wallet.ConnectionPackage.URLs.URL_CREATE_PACKAGE;


public class MemberBankAddPackages extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final int PERMISSIONS_REQUEST_CODE = 0;
    public static final int FILE_PICKER_REQUEST_CODE = 1;
    private TextView tv_dateofbirth,tv_choosefile;
    private EditText tv_referencenumber,tv_amountpaid;
    private Button btn_packages;
    private Spinner spinner;
    private String categories[];
    private String bankcategories[];
    APIInterface apiInterface;
    private String imagePath="";
    private static final int RESULT_LOAD_IMAGE = 101;
    private int requestCode;
    private int resultCode;
    private Intent data;
    private String packageid;
    private String paymentmode;
    private String packageName;
    private String referencenumber;
    private RequestBody requestBody;
    private File f;
    private String content_type;
    private  String file_path;
    private Button mem_back_btn;
    private Spinner bankname;
    private String selectedbank;
    private EditText UPIorbankaccount;
    ProgressDialog progressDialog;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_bank_add_packages);
        Bundle bundle = getIntent().getExtras();
        packageid = bundle.getString("packageid");
        packageName=bundle.getString("packagename");
        apiInterface = APIClient.getClient().create(APIInterface.class);
        tv_dateofbirth=findViewById(R.id.tv_dateofbirth);
        tv_referencenumber=findViewById(R.id.tv_referencenumber);
        tv_amountpaid=findViewById(R.id.tv_amountpaid);
        //tv_choosefile=findViewById(R.id.tv_choosefile);
        btn_packages=findViewById(R.id.btn_packages);
        UPIorbankaccount=findViewById(R.id.tv_amountpaidfrom);
         spinner =findViewById(R.id.spinner_banktype);
         bankname=findViewById(R.id.spinner_amountpaidto);
        mem_back_btn = findViewById(R.id.mem_back_btn);
        mem_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        progressDialog = new ProgressDialog(this);


        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MemberBankAddPackages.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);

        }

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        categories = new String[]{"Payment Mode", "Net Banking", "Bank Challan", "Bank Transfer NEFT/RTGS","UPI Transfer"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                paymentmode=categories[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        paymentmode = spinner.getSelectedItem().toString();

        bankname.setOnItemSelectedListener(this);
        bankcategories=new String[]{"Select Bank to which amount is paid", "AXIS BANK","UNION BANK","UPI (safepe@upi)" };

        ArrayAdapter<String> bankAdapter= new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,bankcategories);
        // Drop down layout style - list view with radio button
        bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinner
        bankname.setAdapter(bankAdapter);
        bankname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                selectedbank=bankcategories[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        selectedbank = bankname.getSelectedItem().toString();


//        tv_choosefile.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View view) {
////                Intent i = new Intent(Intent.ACTION_GET_CONTENT,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////                startActivityForResult(i, RESULT_LOAD_IMAGE);
//               // checkPermissionsAndOpenFilePicker();
//                new MaterialFilePicker().withActivity(MemberBankAddPackages.this).withRequestCode(10).start();
//
//
//            }
//        });

       // File file = new File("luser.jpg");
       // RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), ));





        tv_dateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });
        btn_packages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //callPackages();
                /*AddMembershipPackages addMembershipPackages=new AddMembershipPackages();
                addMembershipPackages.execute();*/
                if (tv_dateofbirth.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Enter Date of payment", Toast.LENGTH_SHORT).show();

                }
                else if (tv_referencenumber.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Enter Reference no", Toast.LENGTH_SHORT).show();

                }
                else if (tv_amountpaid.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Enter Amount Paid", Toast.LENGTH_SHORT).show();

                }
                else if (paymentmode.equals("Payment Mode")){
                    Toast.makeText(getApplicationContext(), "Please Select Payment Mode", Toast.LENGTH_SHORT).show();
                }
                else if (selectedbank.equalsIgnoreCase("Select Bank to which amount is paid")){
                    Toast.makeText(getApplicationContext(), "Please Select Bank", Toast.LENGTH_SHORT).show();
                }
                else if (UPIorbankaccount.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Enter UPI or Account no", Toast.LENGTH_SHORT).show();

                }
                else {
                    SendDetails();
            }
        }
        });

    }



    private void SendDetails() {
        referencenumber= String.valueOf(tv_referencenumber.getText());
        final String paidfrom = String.valueOf(UPIorbankaccount.getText());
        final String buydate = tv_dateofbirth.getText().toString();


        class SendDetails extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setMessage("\t Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                //progressbar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... strings) {
                final String userid = SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserid();

                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("package_id",packageid);
                params.put("userid",userid);
                params.put("payment_mode",paymentmode);
                //params.put("package_name",packageName);
                params.put("reference_no",referencenumber);
                params.put("paidto",selectedbank);
                params.put("paidfrom",paidfrom);
                params.put("buydate",buydate);
                return requestHandler.sendPostRequest(URLs.URL_CREATE_PACKAGE, params);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                startActivity(new Intent(getApplicationContext(), PackageRequestProcessing.class));


            }
        }
        SendDetails sd = new SendDetails();
        sd.execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10 && resultCode==RESULT_OK){
            progressDialog=new ProgressDialog(MemberBankAddPackages.this);
            progressDialog.setTitle("Uploading");
            progressDialog.setMessage("Please Wait");
            progressDialog.show();
            tv_choosefile.setText("File Selected");

            Thread t=new Thread(new Runnable() {
                @Override
                public void run() {
                    f=new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    content_type=getMimeType(f.getPath());
                    file_path=f.getAbsolutePath();
                    progressDialog.dismiss();



                }


            });
            t.start();
        }
    }
    private String getMimeType(String path) {
        String extension= MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
    private void Selectdocument(){
        final String userid = SharedPrefManager.getInstance(getApplicationContext()).getUser().getUserid();
        OkHttpClient client=new OkHttpClient();
        RequestBody file_body=RequestBody.create(MediaType.parse(content_type),f);
        requestBody=new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type",content_type)
                .addFormDataPart("document_attached",file_path.substring(file_path.lastIndexOf("/")+1),file_body)
                .addFormDataPart("package_id",packageid)
                .addFormDataPart("userid",userid)
                .addFormDataPart("payment_mode",paymentmode)
                .addFormDataPart("package_name",packageName)
                .addFormDataPart("reference_no",referencenumber)
                .build();
        Request request=new Request.Builder()
                .url(URL_CREATE_PACKAGE)
                .post(requestBody)
                .build();
        try {
            okhttp3.Response response=client.newCall(request).execute();

            if (!response.isSuccessful()){
                Log.e("dddddddddddd",response.body().string());

                Log.e("ssssssssssssss",response.toString());
                throw new IOException("Error"+response);
            }
            else if (response.isSuccessful()) {
                Log.e("cccccccccccccc",response.body().string());
            }
            else{
                Log.e("cccccccccccccc","Failure");                        }
        } catch (IOException e) {
            Log.e("aaaaaaaaaaaaaaaa",e.toString());
            e.printStackTrace();
        }

    }
    private void Sendfile() {
        referencenumber= String.valueOf(tv_referencenumber.getText());

        class SendDocument extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("\t Loading...");
            progressDialog.show();
            //progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Selectdocument();
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "File upladed successfully.", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Your package approval is under process.", Toast.LENGTH_SHORT).show();


        }
    }
    SendDocument ul = new SendDocument();
            ul.execute();

}

    private void checkPermissionsAndOpenFilePicker() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                showError();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            openFilePicker();
        }
    }

    private void showError() {
        Toast.makeText(this, "Allow external storage reading", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFilePicker();
                } else {
                    showError();
                }
            }
        }
    }

    private void openFilePicker() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .withHiddenFiles(true)
                .withTitle("Sample title")
                .start();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
//            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
//
//            if (path != null) {
//                Log.d("Path: ", path);
//                Toast.makeText(this, "Picked file: " + path, Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    public void callPackages() {
        MultipartBody.Part image=null;
        RequestBody requestBody=null;
        imagePath="";
        if(imagePath.toString().equalsIgnoreCase("")){
            image=MultipartBody.Part.createFormData("image","");
        }else{
            File file=new File(imagePath);
             requestBody=RequestBody.create(MediaType.parse("image/*"),file);
            image=MultipartBody.Part.createFormData("document_attached",file.getName(),requestBody);
        }
        RequestBody request_id=RequestBody.create(MediaType.parse("text/plain"),"PROM");
        RequestBody userid=RequestBody.create(MediaType.parse("text/plain"),"u7861056786");
        RequestBody payment_mode=RequestBody.create(MediaType.parse("text/plain"),"upi");
        RequestBody reference_no=RequestBody.create(MediaType.parse("text/plain"),"reference_no");
        RequestBody leave_amount=RequestBody.create(MediaType.parse("text/plain"),"500");
        RequestBody date_auto_generated=RequestBody.create(MediaType.parse("text/plain"),"01-05-2019");
       /* PackageDataSend user = new PackageDataSend();
        user.package_id="PROM";
        user.userid="u7861056786";
        user.payment_mode="upi";
        user.reference_no="reference_no";
        user.leave_amount="500";
        user.date_auto_generated="01-05-2019";*/


        Call<SendPackageModel> call1 = apiInterface.createUser(requestBody,request_id,userid,payment_mode,reference_no,leave_amount,date_auto_generated);
        call1.enqueue(new Callback<SendPackageModel>() {
            @Override
            public void onResponse(Call<SendPackageModel> call, Response<SendPackageModel> response) {


                //Toast.makeText(getApplicationContext(), user1.name + " " + user1.job + " " + user1.id + " " + user1.createdAt, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<SendPackageModel> call, Throwable t) {
                call.cancel();
            }
        });
    }



//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        this.requestCode = requestCode;
//        this.resultCode = resultCode;
//        this.data = data;
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
//            Uri selectedImageUri = data.getData();
//            imagePath= getPathFromURI(selectedImageUri);
//          //  editText1.setText(s);
//            if(imagePath!=null)
//            Log.d("Picture Path", imagePath);
//
//           /* ImageView imageView = (ImageView) findViewById(R.id.imgView);
//            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));*/
//        }
//    }
//
//    public String getPathFromURI(Uri contentUri) {
//        String res = null;
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
//        if (cursor.moveToFirst()) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            res = cursor.getString(column_index);
//        }
//        cursor.close();
//        return res;
//    }
    public void datePicker(){
        final Calendar c = Calendar.getInstance();
        int  mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int  mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        tv_dateofbirth.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
