package com.plsr.sunil.trips;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class TripChat extends AppCompatActivity implements ChatAdapter.ClickHandler {
    Trip trip;
    TextView title,location;
    ImageView imageView;
    ArrayList<Chat> chats=new ArrayList<Chat>();
    ListView listView;
    EditText message;
    Button sendmsg,sendimage;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    FirebaseStorage storage=FirebaseStorage.getInstance();
    Uri path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_chat);
        trip= (Trip) getIntent().getSerializableExtra("tripchat");
        Log.d("tripchat",trip.toString());
        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        title= (TextView) findViewById(R.id.listviewemailid5);
        listView= (ListView) findViewById(R.id.chatlistv);
        message= (EditText) findViewById(R.id.editText3);
        sendmsg= (Button) findViewById(R.id.button5);
        sendimage= (Button) findViewById(R.id.button6);
        location= (TextView) findViewById(R.id.listviewusername5);
        imageView= (ImageView) findViewById(R.id.listviewuserimage5);
        if(trip.getImageurl()!=null){
            Picasso.with(TripChat.this).load(trip.getImageurl()).into(imageView);
        }
        title.setText(trip.getTitle());
        location.setText(trip.getLocation());
        databaseReference.child("chats").child(trip.getTripID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Chat c=dataSnapshot1.getValue(Chat.class);
                    chats.add(c);
                }
                Log.d("chats", chats.toString());
                ChatAdapter adapter=new ChatAdapter(TripChat.this,R.layout.list_chat,chats);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!message.getText().toString().trim().equals("")){
                    final Chat c=new Chat();

                    DatabaseReference ref=databaseReference.child("users").child(auth.getCurrentUser().getUid());
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User u=dataSnapshot.getValue(User.class);
                            c.setUserid(auth.getCurrentUser().getUid());
                            c.setUsername(u.getFirstName()+","+u.getLastName());
                            c.setMessage(message.getText().toString());
                            chats.add(c);
                            databaseReference.child("chats").child(trip.getTripID()).setValue(chats);
                            ChatAdapter adapter=new ChatAdapter(TripChat.this,R.layout.list_chat,chats);
                            listView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else {
                    Toast.makeText(TripChat.this,"please enter a message",Toast.LENGTH_LONG).show();
                }




            }


        });
        sendimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!message.getText().toString().trim().equals("")){
                    Intent intent=new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"select the image"),200);
                }else {
                    Toast.makeText(TripChat.this,"please enter a message",Toast.LENGTH_LONG).show();
                }

            }
        });




        findViewById(R.id.BplaceToVisit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TripChat.this, AdditionalPlaces.class);
                i.putExtra("trip", trip);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200){
            if(resultCode==RESULT_OK){
                path=data.getData();
                Log.d("demo",path+"     "+(new File(String.valueOf(Uri.parse(data.getDataString())))).getName());
                try {
                    final Chat c=new Chat();
                    Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                    //imageView.setImageBitmap(bitmap);
                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                    //imageView.setDrawingCacheEnabled(false);
                    byte[] data1=baos.toByteArray();
                    String firepath="chats/"+ UUID.randomUUID()+".png";
                    StorageReference firememeref=storage.getReference(firepath);
                    StorageMetadata metadata=new StorageMetadata.Builder()
                            .setCustomMetadata("text","Amruth")
                            .build();
                    UploadTask uploadTask=firememeref.putBytes(data1,metadata);
                    uploadTask.addOnSuccessListener(TripChat.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            DatabaseReference ref=databaseReference.child("users").child(auth.getCurrentUser().getUid());
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User u=dataSnapshot.getValue(User.class);
                                    c.setUserid(auth.getCurrentUser().getUid());
                                    c.setUsername(u.getFirstName()+","+u.getLastName());
                                    c.setMessage(message.getText().toString());
                                    c.setImageurl(taskSnapshot.getDownloadUrl()+"");
                                    Log.d("imagetest",taskSnapshot.getDownloadUrl()+"");
                                    Log.d("imagetest","dfdkjfbhkdsfb");
                                    chats.add(c);
                                    databaseReference.child("chats").child(trip.getTripID()).setValue(chats);
                                    ChatAdapter adapter=new ChatAdapter(TripChat.this,R.layout.list_chat,chats);
                                    listView.setAdapter(adapter);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

//                            Uri uri=taskSnapshot.getDownloadUrl();
//                            Log.d("demo",uri+"");
//
//                            //userReference.child("imageUrl").setValue(uri+"");
//                            imageurl=uri+"";
//                            Log.d("image",imageurl);

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    @Override
    public void senRequest(int i) {
        //Log.d("demr",c.toString());
//        chats.remove(i);
        if (chats.get(i).deletedFor == null)
            chats.get(i).deletedFor = new ArrayList<>();
        chats.get(i).deletedFor.add(auth.getCurrentUser().getUid());
        databaseReference.child("chats").child(trip.getTripID()).setValue(chats);
        ChatAdapter adapter=new ChatAdapter(TripChat.this,R.layout.list_chat,chats);
        listView.setAdapter(adapter);
        Toast.makeText(TripChat.this,"deleted",Toast.LENGTH_LONG).show();
    }
}
