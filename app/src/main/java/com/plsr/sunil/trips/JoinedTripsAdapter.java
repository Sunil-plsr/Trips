package com.plsr.sunil.trips;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class JoinedTripsAdapter extends ArrayAdapter<Trip> {
    List<Trip> myData;
    Context myContext;
    int myResource;
    ClickHandler clickHandler;
    public JoinedTripsAdapter(Context context, int resource, List<Trip> objects) {
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
        ImageView imageView= (ImageView) convertView.findViewById(R.id.listviewuserimage3);
        TextView email= (TextView) convertView.findViewById(R.id.listviewemailid3);
        TextView name= (TextView) convertView.findViewById(R.id.listviewusername3);
        View con=convertView.findViewById(R.id.container3);

        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler.senRequest(t);
            }
        });
        if(t.getImageurl()!=null){
            Picasso.with(myContext).load(t.getImageurl()).into(imageView);
        }
        //final Button sendRequest= (Button) convertView.findViewById(R.id.listviewsendrequest3);
        email.setText(t.getTitle());
        name.setText(t.getLocation());
//        sendRequest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clickHandler.senRequest(t);
//                sendRequest.setEnabled(false);
//            }
//        });
        return convertView;
    }

    public interface ClickHandler {
        void senRequest(Trip trip);
        //ArrayList<String> getSentRequesrs();
        //ArrayList<String> getSentFriends();
    }
}
