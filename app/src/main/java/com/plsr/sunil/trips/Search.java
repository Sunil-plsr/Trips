package com.plsr.sunil.trips;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Search extends AppCompatActivity implements SearchAdapter.ClickHandler {
    Button search;
    EditText nameorid;
    FirebaseAuth auth;
    DatabaseReference databaseReference,userReference;
    ArrayList<User> searchedUsers=new ArrayList<User>();
    ArrayList<String> reqsSentTo=new ArrayList<String>();
    ArrayList<String> friends=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        userReference=databaseReference.child("users");
        search= (Button) findViewById(R.id.searchbutton);
        nameorid= (EditText) findViewById(R.id.nameorid);
        userReference.child(auth.getCurrentUser().getUid()).child("sentrequests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String s=dataSnapshot1.getValue(String.class);
                    reqsSentTo.add(s);
                    Log.d("demooo",s);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userReference.child(auth.getCurrentUser().getUid()).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String s=dataSnapshot1.getValue(String.class);
                    friends.add(s);
                    Log.d("demooo",s);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchedUsers.size()!=0){
                    searchedUsers.clear();
                }
                final ListView listView;
                listView= (ListView) findViewById(R.id.listviewofsearchedusers);
                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            User user=dataSnapshot1.getValue(User.class);
                            String sentence = nameorid.getText().toString();
                            String search  = "keyword";
                            if ( user.getFirstName().toLowerCase().indexOf(nameorid.getText().toString().toLowerCase()) != -1 ||user.getEmail().toLowerCase().indexOf(nameorid.getText().toString().toLowerCase()) != -1){
                                Log.d("demoo",user.toString());
                                Boolean b=user.getEmail().toLowerCase().indexOf(nameorid.getText().toString().toLowerCase()) != -1;
                                Log.d("demoo","booolean     "+b);
                                if(!user.getUserID().equals(auth.getCurrentUser().getUid())){
                                    searchedUsers.add(user);
                                }


                            }else {
                                Log.d("demoo","not found");
                                Log.d("demoo",user.getFirstName().toLowerCase());
                                Log.d("demoo",nameorid.getText().toString().toLowerCase());
                            }

                        }
                        if(listView.getCount()!=0){

                        }


                        SearchAdapter adapter=new SearchAdapter(Search.this,R.layout.list_searchedusers,searchedUsers);
                        listView.setAdapter(adapter);




                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


    }

    @Override
    public void senRequest(User user) {
        Toast.makeText(Search.this,user.toString(),Toast.LENGTH_LONG).show();
        DatabaseReference friendreference=userReference.child(user.getUserID()).child("friendsrequests").child(auth.getCurrentUser().getUid());
        DatabaseReference sentRequests=userReference.child(auth.getCurrentUser().getUid()).child("sentrequests").child(user.getUserID());
        friendreference.setValue(auth.getCurrentUser().getUid());
        sentRequests.setValue(user.getUserID());
        userReference.child(auth.getCurrentUser().getUid()).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String s=dataSnapshot1.getValue(String.class);
                    friends.add(s);
                    Log.d("demooo",s);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userReference.child(auth.getCurrentUser().getUid()).child("sentrequests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String s=dataSnapshot1.getValue(String.class);
                    reqsSentTo.add(s);
                    Log.d("demooo",s);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public ArrayList<String> getSentRequesrs() {
        return reqsSentTo;

    }

    @Override
    public ArrayList<String> getSentFriends() {
        return friends;
    }
}
