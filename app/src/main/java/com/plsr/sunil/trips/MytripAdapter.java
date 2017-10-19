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

import java.util.List;


public class MytripAdapter extends ArrayAdapter<Trip> {
    List<Trip> myData;
    Context myContext;
    int myResource;
    ClickHandler clickHandler;
    public MytripAdapter(Context context, int resource, List<Trip> objects) {
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
        final Trip t=myData.get(position);
        ImageView imageView= (ImageView) convertView.findViewById(R.id.listviewuserimage2);
        TextView email= (TextView) convertView.findViewById(R.id.listviewemailid2);
        TextView name= (TextView) convertView.findViewById(R.id.listviewusername2);
        final Button sendRequest= (Button) convertView.findViewById(R.id.listviewsendrequest2);
        if(t.getImageurl()!=null){
            Picasso.with(myContext).load(t.getImageurl()).into(imageView);
        }
        email.setText(t.getTitle());
        name.setText(t.getLocation());
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler.senRequest(t);
                sendRequest.setEnabled(false);
            }
        });
        return convertView;
    }

    public interface ClickHandler {
        void senRequest(Trip trip);
        //ArrayList<String> getSentRequesrs();
        //ArrayList<String> getSentFriends();
    }
}
