package com.plsr.sunil.trips;

import android.content.Context;
import android.support.annotation.NonNull;
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


public class FriensRequestAdapter extends ArrayAdapter<User> {
    List<User> myData;
    Context myContext;
    int myResource;
    ClickHandler clickHandler;
    public FriensRequestAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
        this.myContext=context;
        this.myData=objects;
        this.myResource=resource;
        clickHandler= (ClickHandler) context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= inflater.inflate(myResource,parent,false);
        }
        final User user=myData.get(position);
        ImageView imageView= (ImageView) convertView.findViewById(R.id.listviewuserimage1);
        TextView email= (TextView) convertView.findViewById(R.id.listviewemailid1);
        TextView name= (TextView) convertView.findViewById(R.id.listviewusername1);
        final Button sendRequest= (Button) convertView.findViewById(R.id.listviewsendrequest1);
        email.setText("Email: "+user.getEmail());
        name.setText("Name: "+user.getFirstName()+" "+user.getLastName());
        if(user.getImageUrl()!=null){
            Picasso.with(myContext).load(user.getImageUrl()).into(imageView);
        }
//        ArrayList<String> reqs=clickHandler.getSentRequesrs();
////        Log.d("adapter",reqs.get(1));
//        for(int i=0;i<reqs.size();i++){
//
//            if(reqs.get(i).equals(user.getUserID())){
//                Log.d("adapter",reqs.get(i));
//                sendRequest.setText("friend request sent");
//                sendRequest.setEnabled(false);
//
//            }
//        }
//
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler.senRequest(user);
                sendRequest.setEnabled(false);
            }
        });
        return convertView;
    }

    public interface ClickHandler {
        void senRequest(User user);
        ArrayList<String> getSentRequesrs();
    }
}
