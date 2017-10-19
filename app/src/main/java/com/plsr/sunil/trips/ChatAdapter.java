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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ChatAdapter extends ArrayAdapter<Chat> {
    List<Chat> myData;
    Context myContext;
    int myResource;
    ClickHandler clickHandler;
    FirebaseAuth auth;
    public ChatAdapter(Context context, int resource, List<Chat> objects) {
        super(context, resource, objects);
        this.myContext=context;
        this.myData=objects;
        this.myResource=resource;
        clickHandler= (ClickHandler) context;
        auth=FirebaseAuth.getInstance();

    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= inflater.inflate(myResource,parent,false);
        }
        final Chat c=myData.get(position);
        final TextView message= (TextView) convertView.findViewById(R.id.messagechat);
        TextView username= (TextView) convertView.findViewById(R.id.usernamechat);
        ImageView imageView= (ImageView) convertView.findViewById(R.id.imageView3);
        Button delete= (Button) convertView.findViewById(R.id.button7);
        View view=convertView.findViewById(R.id.chatcon);
//        LinearLayout row = (LinearLayout) convertView.findViewById(R.id.mainThing);
        LinearLayout row = (LinearLayout) convertView.findViewById(R.id.chatcon);
        LinearLayout subRow = (LinearLayout) convertView.findViewById(R.id.mainThing);
        LinearLayout subSubRow = (LinearLayout) convertView.findViewById(R.id.subThing);


        //view.setPadding(25,25,25,25);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) message.getLayoutParams();
        LinearLayout.LayoutParams params1= (LinearLayout.LayoutParams) username.getLayoutParams();
        LinearLayout.LayoutParams params2= (LinearLayout.LayoutParams) imageView.getLayoutParams();

        Log.d("whatis",c.getUserid()+auth.getCurrentUser().getUid());
        if(c.getUserid().equals(auth.getCurrentUser().getUid())){

            if (c.deletedFor!=null && c.deletedFor.contains(auth.getCurrentUser().getUid())){
                //to not display
//                convertView.setVisibility(View.GONE);
                row.setVisibility(View.GONE);
                subRow.setVisibility(View.GONE);
                subSubRow.setVisibility(View.GONE);
                message.setVisibility(View.GONE);
                username.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);


            } else {
                //to display

                Log.d("whatis","true");
                params.setMargins(200,0,0,0);
                params1.setMargins(200,0,0,0);
                params2.setMargins(0,0,0,0);
                delete.setVisibility(View.VISIBLE);


                message.setLayoutParams(params);
                username.setLayoutParams(params1);
                imageView.setLayoutParams(params2);

            }


        }else {


            if (c.deletedFor!=null && c.deletedFor.contains(auth.getCurrentUser().getUid())){
                //to not display
//                convertView.setVisibility(View.GONE);
                row.setVisibility(View.GONE);
                convertView.setVisibility(View.GONE);
                row.setVisibility(View.GONE);
                subRow.setVisibility(View.GONE);
                subSubRow.setVisibility(View.GONE);
                message.setVisibility(View.GONE);
                username.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);

            } else {
                //to display

                params.setMargins(0,0,0,0);
                params1.setMargins(0,0,0,0);
                params2.setMargins(0,0,200,0);
                delete.setVisibility(View.VISIBLE);


                message.setLayoutParams(params);
                username.setLayoutParams(params1);
                imageView.setLayoutParams(params2);

            }

        }







        message.setText(c.getMessage());
        username.setText(c.getUsername());
        if(c.getImageurl()!=null){
            imageView.setVisibility(View.VISIBLE);
            Picasso.with(myContext).load(c.getImageurl()).into(imageView);
        }else {
            imageView.setVisibility(View.GONE);
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler.senRequest(position);
            }
        });


        return convertView;
    }

    public interface ClickHandler {
        void senRequest(int i);
        //ArrayList<String> getSentRequesrs();
        //ArrayList<String> getSentFriends();
    }
}
