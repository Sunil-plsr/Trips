package com.plsr.sunil.trips;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyTrips extends AppCompatActivity implements MytripAdapter.ClickHandler {
    FirebaseAuth auth;
    DatabaseReference databaseReference,tripref;
    ArrayList<Trip> trips=new ArrayList<Trip>();
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);
        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        listView= (ListView) findViewById(R.id.mytripslistview);
        tripref=databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("trips");
        tripref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Trip t=dataSnapshot1.getValue(Trip.class);
                    trips.add(t);
                    Log.d("trip",t.toString());
                }
                MytripAdapter adapter=new MytripAdapter(MyTrips.this,R.layout.list_mytrips,trips);
                listView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void senRequest(Trip t) {
        Log.d("trop",t.toString());
        databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("joinedtrips").child(t.getTripID()).setValue(t);
        databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("trips").child(t.getTripID()).removeValue();



    }
}
