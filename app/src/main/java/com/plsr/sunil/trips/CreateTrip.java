package com.plsr.sunil.trips;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class CreateTrip extends AppCompatActivity {
    ImageView imageView;
    Button addphoto,addtrip;
    EditText title;
    //Button locationB;
    String imageurl;
    Uri path;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    FirebaseStorage storage=FirebaseStorage.getInstance();



    //maps
    int PLACE_CODE = 12;
    private String selectedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        imageView= (ImageView) findViewById(R.id.imageView2);
        addphoto= (Button) findViewById(R.id.button3);
        addtrip= (Button) findViewById(R.id.button2);
        title= (EditText) findViewById(R.id.editText);
//        locationB= (Button) findViewById(R.id.BplacePicker);
        addtrip.setEnabled(false);
        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select the image"),200);

            }
        });
        addtrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!title.getText().toString().trim().equals("")){
                    final Trip trip=new Trip();
                    trip.setTitle(title.getText().toString());
                    trip.setLocation(selectedName);
                    if(imageurl!=null){
                        trip.setImageurl(imageurl);
                    }
                    final String id=UUID.randomUUID().toString();
                    trip.setTripID(id);
                    databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("joinedtrips").child(id).setValue(trip);
                    DatabaseReference ref=databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("friends");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                String s=dataSnapshot1.getValue(String.class);
                                DatabaseReference ref1=databaseReference.child("users").child(s);
                                ref1.child("trips").child(id).setValue(trip);
                            }
                            Toast.makeText(CreateTrip.this,"trip added",Toast.LENGTH_LONG).show();
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }else {
                    Toast.makeText(CreateTrip.this,"please enter the details",Toast.LENGTH_LONG).show();
                }
            }
        });





        //Place Picker
//        locationB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//                Intent intent ;
//                try {
//                    intent  = builder.build(CreateTrip.this);
//                    startActivityForResult(intent, PLACE_CODE);
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }
//            }
//        });



        //place auto complete
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("demo", "Place: " + place.getName());
                selectedName = (String) place.getName();
                addtrip.setEnabled(true);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("demo", "An error occurred: " + status);
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200){
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
                    String firepath="trips/"+ UUID.randomUUID()+".png";
                    StorageReference firememeref=storage.getReference(firepath);
                    StorageMetadata metadata=new StorageMetadata.Builder()
                            .setCustomMetadata("text","Amruth")
                            .build();
                    UploadTask uploadTask=firememeref.putBytes(data1,metadata);
                    uploadTask.addOnSuccessListener(CreateTrip.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri uri=taskSnapshot.getDownloadUrl();
                            Log.d("demo",uri+"");
                            //userReference.child("imageUrl").setValue(uri+"");
                            imageurl=uri+"";
                            Log.d("image",imageurl);

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
            }else if (requestCode == PLACE_CODE){
                if (resultCode == RESULT_OK){
                addtrip.setEnabled(true);
                Place place = PlacePicker.getPlace(CreateTrip.this, data);
                String address = (String) place.getAddress();
                selectedName = (String) place.getName();
                Log.d("demo",selectedName);
//                    location.setText(name);
                }
        }
    }
}
