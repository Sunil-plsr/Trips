package com.plsr.sunil.trips;

import android.content.Intent;
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

public class JoinedTrips extends AppCompatActivity implements JoinedTripsAdapter.ClickHandler {
    FirebaseAuth auth;
    DatabaseReference databaseReference,tripref;
    ArrayList<Trip> trips=new ArrayList<Trip>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_trips);
        listView= (ListView) findViewById(R.id.joinedtripslv);
        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        tripref=databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("joinedtrips");
        tripref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Trip t=dataSnapshot1.getValue(Trip.class);
                    trips.add(t);
                    Log.d("trip1",t.toString());
                }
                JoinedTripsAdapter adapter=new JoinedTripsAdapter(JoinedTrips.this,R.layout.list_joinedtrips,trips);
                listView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void senRequest(Trip trip) {
        Log.d("joinedd",trip.toString());
        Intent intent=new Intent(JoinedTrips.this,TripChat.class);
        intent.putExtra("tripchat",trip);
        startActivity(intent);

    }
}
