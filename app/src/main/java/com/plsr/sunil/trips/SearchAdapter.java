package com.plsr.sunil.trips;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class SearchAdapter extends ArrayAdapter<User> {
    List<User> myData;
    Context myContext;
    int myResource;
    ClickHandler clickHandler;
//    FirebaseAuth auth;
//    DatabaseReference databaseReference,sentreqref;
    public SearchAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
        this.myContext=context;
        this.myData=objects;
        this.myResource=resource;
        clickHandler= (ClickHandler) context;
//        databaseReference= FirebaseDatabase.getInstance().getReference();
//        auth=FirebaseAuth.getInstance();
//        sentreqref=databaseReference.child("users").child(auth.getCurrentUser().getUid()).child("sentrequests");

    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= inflater.inflate(myResource,parent,false);
        }
        final User user=myData.get(position);
        ImageView imageView= (ImageView) convertView.findViewById(R.id.listviewuserimage);
        TextView email= (TextView) convertView.findViewById(R.id.listviewemailid);
        TextView name= (TextView) convertView.findViewById(R.id.listviewusername);
        final Button sendRequest= (Button) convertView.findViewById(R.id.listviewsendrequest);
        email.setText("Email: "+user.getEmail());
        name.setText("Name: "+user.getFirstName()+" "+user.getLastName());
        if(user.getImageUrl()!=null){
            Picasso.with(myContext).load(user.getImageUrl()).into(imageView);
        }
        ArrayList<String> reqs=clickHandler.getSentRequesrs();
        ArrayList<String> fre=clickHandler.getSentFriends();
//        Log.d("adapter",reqs.get(1));
        for(int i=0;i<reqs.size();i++){

            if(reqs.get(i).equals(user.getUserID())){
                Log.d("adapter",reqs.get(i));
                sendRequest.setText("friend request sent");
                sendRequest.setEnabled(false);

            }
        }
        for(int i=0;i<fre.size();i++){

            if(fre.get(i).equals(user.getUserID())){
                Log.d("adapter",fre.get(i));
                sendRequest.setText("friends");
                sendRequest.setEnabled(false);

            }
        }
//        sentreqref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                    String s=dataSnapshot1.getValue(String.class);
//                    if(s==user.getUserID()){
//                        sendRequest.setText("friend request sent");
//                        sendRequest.setEnabled(false);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler.senRequest(user);
            }
        });
        return convertView;

    }
    public interface ClickHandler {
        void senRequest(User user);
        ArrayList<String> getSentRequesrs();
        ArrayList<String> getSentFriends();
    }
}
