package com.plsr.sunil.trips;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference databaseReference,userReference,friendsRequestsReference;
    TextView email,name,gender;
    User user;
    Button friendRequests,createtrip,mytrips,joinedtrips;
    ArrayList<User> friendReqs=new ArrayList<User>();
    ImageView propic;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("sam","create");
        i=1;
        Log.d("numb",i+"    create i");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("profile");
        //getSupportActionBar().setIcon("https://firebasestorage.googleapis.com/v0/b/homework9-31b3b.appspot.com/o/firememes%2Fb2b130b1-44ed-43dc-b567-7cd513de8f60.png?alt=media&token=f66d4463-d67e-4786-a686-387311e99e78");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        auth=FirebaseAuth.getInstance();
        friendRequests= (Button) findViewById(R.id.seefriendsrequests);
        mytrips= (Button) findViewById(R.id.button4);
        createtrip= (Button) findViewById(R.id.createtripp);
        propic= (ImageView) findViewById(R.id.propic);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        email= (TextView) findViewById(R.id.proemail);
        name= (TextView) findViewById(R.id.proname);
        gender= (TextView) findViewById(R.id.progender);
        joinedtrips= (Button) findViewById(R.id.button43);
        userReference=databaseReference.child("users").child(auth.getCurrentUser().getUid());
        friendsRequestsReference=userReference.child("friendsrequests");
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user=dataSnapshot.getValue(User.class);
                Log.d("pro",user.toString());
                if(user.getImageUrl()!=null){
                    Picasso.with(HomeActivity.this).load(user.getImageUrl()).into(propic);
                }
                email.setText(user.getEmail());
                name.setText(user.getFirstName()+","+user.getLastName());
                gender.setText(user.getGender());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                user=dataSnapshot.getValue(User.class);
//                //firstname.setText("First Name: "+user.getFirstName());
//                //lastname.setText("Last Name: "+user.getLastName());
//                //gender.setText("Gender: "+user.gender);
//                //emailid.setText("Email id: "+user.getEmail());
//                //Log.d("demoo",user.toString());
//                setTitle("Profile");
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        friendsRequestsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String user=dataSnapshot1.getValue(String.class);
                    DatabaseReference ref=databaseReference.child("users").child(user);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            GenericTypeIndicator<Map<String, User>> t = new GenericTypeIndicator<Map<String, User>>() {};
//                            Map<String, User> map = dataSnapshot.getValue(t);
                            User u=dataSnapshot.getValue(User.class);
                            Log.d("testt",u.toString());
                            friendReqs.add(u);
                            friendRequests.setText("friend requests"+"("+friendReqs.size()+")");

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        friendRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("testt",friendReqs.size()+"");
                Intent intent=new Intent(HomeActivity.this,FriendsRequest.class);
                intent.putExtra("friendsreqs",friendReqs);
                startActivity(intent);


            }
        });
        createtrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s= String.valueOf(UUID.randomUUID());
                Intent intent=new Intent(HomeActivity.this,CreateTrip.class);
                startActivity(intent);
                Log.d("uuid",s);
                Boolean b="8e7c4aca-2a0a-4442-b789-ac80e6684390"=="";
                Log.d("uuid","");
            }
        });
        mytrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,MyTrips.class);
                startActivity(intent);
            }
        });
        joinedtrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,JoinedTrips.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user=dataSnapshot.getValue(User.class);
                Log.d("pro",user.toString());
                if(user.getImageUrl()!=null){
                    Picasso.with(HomeActivity.this).load(user.getImageUrl()).into(propic);
                }
                email.setText(user.getEmail());
                name.setText(user.getFirstName()+","+user.getLastName());
                gender.setText(user.getGender());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d("sam","resume");
        if(i==1){
            i=2;
            Log.d("numb",i+"   else resume  i");
        }else if(i==2){
            Log.d("numb",i+"  else if resume  i");
            friendsRequestsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(friendReqs.size()!=0){
                        friendReqs.clear();
                        Log.d("numb","clearing");
                        //friendRequests.setText("friend requests"+"("+friendReqs.size()+")");
                    }
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        String user=dataSnapshot1.getValue(String.class);
                        DatabaseReference ref=databaseReference.child("users").child(user);
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
//                            GenericTypeIndicator<Map<String, User>> t = new GenericTypeIndicator<Map<String, User>>() {};
//                            Map<String, User> map = dataSnapshot.getValue(t);
                                User u=dataSnapshot.getValue(User.class);
                                Log.d("numb",u.toString());
                                friendReqs.add(u);
                                friendRequests.setText("friend requests"+"("+friendReqs.size()+")");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    friendRequests.setText("friend requests"+"("+friendReqs.size()+")");


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Toast.makeText(HomeActivity.this,"settings",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(HomeActivity.this,Settings.class);
            startActivity(intent);

        }else if(item.getItemId()==R.id.search){
            Intent intent=new Intent(HomeActivity.this,Search.class);
            startActivity(intent);
            
        } else if (item.getItemId()==R.id.logout){
            auth.getInstance().signOut();
            Intent i = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
