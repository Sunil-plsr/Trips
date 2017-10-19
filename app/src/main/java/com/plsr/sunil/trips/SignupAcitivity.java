package com.plsr.sunil.trips;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupAcitivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener  {
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    EditText firstname,lastname,email,password;
    Button signup;
    RadioGroup radioGroup;

    //googleSignIn vars
    int RC_SIGN_IN = 22;
    private GoogleApiClient mGoogleApiClient;
    GoogleSignInOptions gso;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String TAG = "demo";
    Intent signInIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_acitivity);
        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        firstname= (EditText) findViewById(R.id.signupfirstname);
        lastname= (EditText) findViewById(R.id.signuplastname);
        email= (EditText) findViewById(R.id.signupemail);
        password= (EditText) findViewById(R.id.signuppassword);
        signup= (Button) findViewById(R.id.signupbutton1);
        radioGroup= (RadioGroup) findViewById(R.id.radiogroup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em=email.getText().toString();
                String pass=password.getText().toString();
                final String fn=firstname.getText().toString();
                final String ln=lastname.getText().toString();
                int selectedid=radioGroup.getCheckedRadioButtonId();
                final RadioButton rb= (RadioButton) findViewById(selectedid);
                if(rb!=null&&!em.isEmpty()&&!pass.isEmpty()&&!fn.isEmpty()&&!ln.isEmpty()){
                    String gen=rb.getText().toString();
                    Log.d("demo",em+pass+fn+ln+gen);
                    auth.createUserWithEmailAndPassword(em,pass).addOnCompleteListener(SignupAcitivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser u=auth.getCurrentUser();
                                User user=new User();
                                user.setFirstName(firstname.getText().toString());
                                user.setLastName(lastname.getText().toString());
                                user.setEmail(email.getText().toString());
                                user.setPassword(password.getText().toString());
                                user.setUserID(auth.getCurrentUser().getUid());
                                if(rb.getId()==R.id.radiomale){
                                    user.setGender("Male");
                                }else if(rb.getId()==R.id.radiofemale){
                                    user.setGender("Female");
                                }

                                databaseReference.child("users").child(u.getUid()).setValue(user);
                                Intent intent=new Intent(SignupAcitivity.this,HomeActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(SignupAcitivity.this,"sing up failed",Toast.LENGTH_LONG).show();
                            }

                        }
                    });


                }else{
                    Toast.makeText(SignupAcitivity.this,"please enter all the details",Toast.LENGTH_LONG).show();
                }



            }
        });





        //google sigin button
        mAuth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        Log.d("googlee", getString(R.string.default_web_client_id));

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //athentication
        findViewById(R.id.gsign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
//                Person person  = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

                Log.d("AccInfo", "Lname:"+account.getFamilyName());
                Log.d("AccInfo", "Fname:"+account.getGivenName());
                Log.d("AccInfo", "Email:"+account.getEmail());
                Log.d("AccInfo", "Display pic:"+account.getPhotoUrl());
//                Log.d("AccInfo", "Gender:"+person.getGender());







            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(this, "Google Sign In Failed", Toast.LENGTH_SHORT).show();
                Log.d("googlee", result.getStatus()+"   "+result.isSuccess());
            }
        }
    }



    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignupAcitivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();



                        } else {
                            Toast.makeText(SignupAcitivity.this, "Authentication Sucessfull", Toast.LENGTH_SHORT).show();
//                            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(signInIntent);
//                            GoogleSignInAccount acct = result.getSignInAccount();
//                            String personName = acct.getDisplayName();
//                            String personGivenName = acct.getGivenName();
//                            String personFamilyName = acct.getFamilyName();
//                            String personEmail = acct.getEmail();
//                            String personGender = acct.get
//                            Log.d("demo", personGivenName);
//
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            UserInfo ui = (UserInfo) user.getProviderData();


                            String em=acct.getEmail();
                            String pass="google";
                            final String fn=acct.getGivenName();
                            final String ln=acct.getFamilyName();
//                            int selectedid=radioGroup.getCheckedRadioButtonId();
//                            final RadioButton rb= (RadioButton) findViewById(selectedid);
//                            if(rb!=null&&!em.isEmpty()&&!pass.isEmpty()&&!fn.isEmpty()&&!ln.isEmpty()){
//                                String gen=rb.getText().toString();
                                Log.d("goog",em+pass+fn+ln);

                                FirebaseUser u=auth.getCurrentUser();
                                User user=new User();
                                user.setFirstName(fn);
                                user.setLastName(ln);
                                user.setEmail(em);
                                user.setPassword(pass);
                                user.setUserID(auth.getCurrentUser().getUid());
                                user.setGender("Male");
                                user.setImageUrl(acct.getPhotoUrl()+"");
                                databaseReference.child("users").child(u.getUid()).setValue(user);
                                Intent intent=new Intent(SignupAcitivity.this,HomeActivity.class);
                                startActivity(intent);


//                                auth.createUserWithEmailAndPassword(em,pass).addOnCompleteListener(SignupAcitivity.this, new OnCompleteListener<AuthResult>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<AuthResult> task) {
//                                        if(task.isSuccessful()){
//                                            FirebaseUser u=auth.getCurrentUser();
//                                            User user=new User();
//                                            user.setFirstName(firstname.getText().toString());
//                                            user.setLastName(lastname.getText().toString());
//                                            user.setEmail(email.getText().toString());
//                                            user.setPassword(password.getText().toString());
//                                            user.setUserID(auth.getCurrentUser().getUid());
//                                            if(rb.getId()==R.id.radiomale){
//                                                user.setGender("Male");
//                                            }else if(rb.getId()==R.id.radiofemale){
//                                                user.setGender("Female");
//                                            }
//
//                                            databaseReference.child("users").child(u.getUid()).setValue(user);
//                                            Intent intent=new Intent(SignupAcitivity.this,HomeActivity.class);
//                                            startActivity(intent);
//                                        }else {
//                                            Toast.makeText(SignupAcitivity.this,"sing up failed",Toast.LENGTH_LONG).show();
//                                        }
//
//                                    }
//                                });


//                            }else{
//                                Toast.makeText(SignupAcitivity.this,"please enter all the details",Toast.LENGTH_LONG).show();
//                            }









                        }
                        // ...
                    }
                });
    }



}
