package com.plsr.sunil.trips;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Settings extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference databaseReference,userReference;
    TextView emailid,gender,firstname,lastname;
    Button firstnameedit,lastnameedit,genderedit,passwordedit,imageedit;
    User user;
    ImageView imageView;
    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference reference;
    Uri path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        emailid= (TextView) findViewById(R.id.settingsuserid);
        gender= (TextView) findViewById(R.id.settingsgender);
        firstname= (TextView) findViewById(R.id.settingsfirstname);
        lastname= (TextView) findViewById(R.id.settingslastname);
        firstnameedit= (Button) findViewById(R.id.firstnameedit);
        lastnameedit= (Button) findViewById(R.id.lastnameedit);
        genderedit= (Button) findViewById(R.id.genderedit);
        imageedit= (Button) findViewById(R.id.imageedit);
        imageView= (ImageView) findViewById(R.id.settingsimageView);
        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        userReference=databaseReference.child("users").child(auth.getCurrentUser().getUid());
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user=dataSnapshot.getValue(User.class);
                firstname.setText("First Name: "+user.getFirstName());
                lastname.setText("Last Name: "+user.getLastName());
                gender.setText("Gender: "+user.gender);
                emailid.setText("Email id: "+user.getEmail());
                if(user.getImageUrl()!=null){
                    Picasso.with(Settings.this).load(user.getImageUrl()).into(imageView);
                }
                Log.d("demoo",user.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        firstnameedit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                LayoutInflater factory = LayoutInflater.from(Settings.this);
                final View textEntryView = factory.inflate(R.layout.oneedittext, null);
                final EditText input = (EditText) textEntryView.findViewById(R.id.oneedittext1);
                final AlertDialog.Builder alert = new AlertDialog.Builder(Settings.this);





                alert.setTitle("Enter your first name: ").setView(textEntryView).setPositiveButton("Set",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {


                                //http://dataservice.accuweather.com/locations/v1/US/search?apikey=wYGcg2O89sIzJM2D6zGCkNyREc8Trkwb&q=Charlotte
                                //shoudl write code to save to preferences
                                if(user!=null&&input.getText().toString()!=""){
                                    user.setFirstName(input.getText().toString());
                                    databaseReference.child("users").child(auth.getCurrentUser().getUid()).setValue(user);
                                    Toast.makeText(Settings.this,"frist name updated",Toast.LENGTH_LONG).show();
                                    firstname.setText("First Name: "+input.getText().toString());
                                }else {
                                    Toast.makeText(Settings.this,"frist name not updated",Toast.LENGTH_LONG).show();
                                }




                            }
                        }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
     /*
     * User clicked cancel so do some stuff
     */
                            }
                        });
                alert.show();


            }
        });
        lastnameedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater factory = LayoutInflater.from(Settings.this);
                final View textEntryView = factory.inflate(R.layout.oneedittext, null);
                final EditText input = (EditText) textEntryView.findViewById(R.id.oneedittext1);
                final AlertDialog.Builder alert = new AlertDialog.Builder(Settings.this);





                alert.setTitle("Enter your first name: ").setView(textEntryView).setPositiveButton("Set",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {


                                //http://dataservice.accuweather.com/locations/v1/US/search?apikey=wYGcg2O89sIzJM2D6zGCkNyREc8Trkwb&q=Charlotte
                                //shoudl write code to save to preferences
                                if(user!=null&&!input.getText().toString().trim().isEmpty()&&input.getText().toString().trim()!=""){
                                    user.setLastName(input.getText().toString());
                                    databaseReference.child("users").child(auth.getCurrentUser().getUid()).setValue(user);
                                    Toast.makeText(Settings.this,"last name updated",Toast.LENGTH_LONG).show();
                                    lastname.setText("First Name: "+input.getText().toString());
                                }else {
                                    Toast.makeText(Settings.this,"last name not updated",Toast.LENGTH_LONG).show();
                                }




                            }
                        }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
     /*
     * User clicked cancel so do some stuff
     */
                            }
                        });
                alert.show();


            }
        });
        genderedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test",user.getGender());
                if(user!=null){
                    if(user.getGender().equals("Male")){
                        user.setGender("Female");
                        databaseReference.child("users").child(auth.getCurrentUser().getUid()).setValue(user);
                        Toast.makeText(Settings.this,"gender updated",Toast.LENGTH_LONG).show();
                        gender.setText("Gender: "+"Female");
                    }else if(user.getGender().equals("Female")) {
                        user.setGender("Male");
                        databaseReference.child("users").child(auth.getCurrentUser().getUid()).setValue(user);
                        Toast.makeText(Settings.this,"gender updated",Toast.LENGTH_LONG).show();
                        gender.setText("Gender: "+"Male");
                    }
                }
            }
        });
        imageedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select the image"),100);

            }
        });


        //disabling changing password if authentication is done through google
        if (user.getPassword() == "google"){

        }


//        passwordedit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LayoutInflater factory = LayoutInflater.from(Settings.this);
//
//
//                //text_entry is an Layout XML file containing two text field to display in alert dialog
//                final View textEntryView = factory.inflate(R.layout.twoedittext, null);
//                final EditText input1 = (EditText) textEntryView.findViewById(R.id.edittextpass1);
//                final EditText input2 = (EditText) textEntryView.findViewById(R.id.edittextpass2);
//
//                final AlertDialog.Builder alert = new AlertDialog.Builder(Settings.this);
//                alert.setTitle("Enter City Details:").setView(textEntryView).setPositiveButton("Set",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                                int whichButton) {
//
//
//                                //http://dataservice.accuweather.com/locations/v1/US/search?apikey=wYGcg2O89sIzJM2D6zGCkNyREc8Trkwb&q=Charlotte
//
//                                //Log.i("demo","TextEntry 1 Entered "+input1.getText().toString());
//                                //Log.i("demo","TextEntry 2 Entered "+input2.getText().toString());
//                                //shoudl write code to save to preferences
//                                Log.d("demo","input 1"+input1.getText().toString());
//                                Log.d("demo","input 2"+input2.getText().toString());
//
//
//
//                            }
//                        }).setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                                int whichButton) {
//     /*
//     * User clicked cancel so do some stuff
//     */
//                            }
//                        });
//                alert.show();
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            if(resultCode==RESULT_OK){
                path=data.getData();
                Log.d("demo",path+"     "+(new File(String.valueOf(Uri.parse(data.getDataString())))).getName());
                try {
                    Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                    imageView.setImageBitmap(bitmap);
                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                    //imageView.setDrawingCacheEnabled(false);
                    byte[] data1=baos.toByteArray();
                    String firepath="firememes/"+ UUID.randomUUID()+".png";
                    StorageReference firememeref=storage.getReference(firepath);
                    StorageMetadata metadata=new StorageMetadata.Builder()
                            .setCustomMetadata("text","Amruth")
                            .build();
                    UploadTask uploadTask=firememeref.putBytes(data1,metadata);
                    uploadTask.addOnSuccessListener(Settings.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri uri=taskSnapshot.getDownloadUrl();
                            Log.d("demo",uri+"");
                            userReference.child("imageUrl").setValue(uri+"");

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
