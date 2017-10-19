package com.plsr.sunil.trips;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class AdditionalPlaces extends AppCompatActivity {

    private Trip trip;
    ListView places;
    int PLACE_CODE = 12;


    FirebaseAuth auth;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();;
    FirebaseStorage storage=FirebaseStorage.getInstance();
    DatabaseReference  userReference=databaseReference.child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_places);


        trip= (Trip) getIntent().getSerializableExtra("trip");
        places = (ListView) findViewById(R.id.LVplacesToVisit);


        if (trip.wayPoints!=null && trip.wayPoints.size()>0){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, trip.wayPoints);
            places.setAdapter(adapter);

        } else {
            ArrayList<String> def = new ArrayList<>();
            def.add("There are no additional places to visit.");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, def);
            places.setAdapter(adapter);
        }


        places.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                Log.d("demo", "Before Deleting: "+trip.wayPoints.toString());
                trip.wayPoints.remove(position);
                Log.d("demo", "After Deleting: "+trip.wayPoints.toString());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdditionalPlaces.this, android.R.layout.simple_list_item_1, android.R.id.text1, trip.wayPoints);
                places.setAdapter(adapter);


                //updating in firebase

                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            User user=dataSnapshot1.getValue(User.class);
                            DatabaseReference uref=userReference.child(user.getUserID());
                            final DatabaseReference joinedtripref=uref.child("joinedtrips");
                            joinedtripref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(trip.getTripID())){
                                        Log.d("triptest","true");
                                        DatabaseReference thistripref=joinedtripref.child(trip.getTripID());
//                                        thistripref.child("wayPoints").child(name).setValue(name);
                                        joinedtripref.child(trip.tripID).setValue(trip);


                                        //dataSnapshot.child(trip.getTripID())
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            //if(joinedtripref.hasChild())
                            //DatabaseReference tripref= joinedtripref.child(trip.getTripID());






                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                return false;
            }
        });



        findViewById(R.id.BaddPlaces).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                Intent intent ;
                try {
                    intent  = builder.build(AdditionalPlaces.this);
                    startActivityForResult(intent, PLACE_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


        findViewById(R.id.BroundTrip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdditionalPlaces.this, MapsActivity.class);
                i.putExtra("trip", trip);
                startActivity(i);
            }
        });

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_CODE){
            if (resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(AdditionalPlaces.this, data);
                String address = (String) place.getAddress();
                final String name = (String) place.getName();
                Log.d("demo", "Selected place is:"+name);
                if (trip.wayPoints == null)
                    trip.wayPoints = new ArrayList<>();

                trip.wayPoints.add(name);
                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            User user=dataSnapshot1.getValue(User.class);
                            DatabaseReference uref=userReference.child(user.getUserID());
                            final DatabaseReference joinedtripref=uref.child("joinedtrips");
                            joinedtripref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(trip.getTripID())){
                                        Log.d("triptest","true");
                                        DatabaseReference thistripref=joinedtripref.child(trip.getTripID());
//                                        thistripref.child("wayPoints").child(name).setValue(name);
                                        joinedtripref.child(trip.tripID).setValue(trip);


                                        //dataSnapshot.child(trip.getTripID())
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            //if(joinedtripref.hasChild())
                            //DatabaseReference tripref= joinedtripref.child(trip.getTripID());






                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, trip.wayPoints);
                places.setAdapter(adapter);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//    TODO    trip.

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
