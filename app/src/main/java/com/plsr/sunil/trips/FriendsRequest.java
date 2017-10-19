package com.plsr.sunil.trips;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FriendsRequest extends AppCompatActivity implements FriensRequestAdapter.ClickHandler {
    ArrayList<User> friendReqs=new ArrayList<User>();
    ListView listView;
    FirebaseAuth auth;
    DatabaseReference databaseReference,userReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_request);
        listView= (ListView) findViewById(R.id.listview1);
        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        userReference=databaseReference.child("users");

        friendReqs= (ArrayList<User>) getIntent().getSerializableExtra("friendsreqs");
        FriensRequestAdapter adapter=new FriensRequestAdapter(FriendsRequest.this,R.layout.list_friendreq,friendReqs);
        Log.d("thisis",friendReqs.toString());
        listView.setAdapter(adapter);


    }

    @Override
    public void senRequest(User user) {
        Log.d("buttontest",user.toString());
        userReference.child(auth.getCurrentUser().getUid()).child("friendsrequests").child(user.getUserID()).removeValue();
        DatabaseReference ref1=userReference.child(auth.getCurrentUser().getUid()).child("friends").child(user.getUserID());
        ref1.setValue(user.getUserID());
        DatabaseReference ref2=userReference.child(user.getUserID()).child("friends").child(auth.getCurrentUser().getUid());
        ref2.setValue(auth.getCurrentUser().getUid());


    }

    @Override
    public ArrayList<String> getSentRequesrs() {
        return null;
    }
}
