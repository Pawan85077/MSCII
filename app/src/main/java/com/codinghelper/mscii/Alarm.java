package com.codinghelper.mscii;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Alarm extends BroadcastReceiver {

    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String receiverUserID;
    StorageReference referencedel;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(final Context context, Intent intent) {
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference();
        receiverUserID =intent.getExtras().get("visit_user_id").toString();
        reference.child("studentDetail").child(receiverUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String Simg1 =String.valueOf(dataSnapshot.child("states1").getValue());
                    String Simg2 =String.valueOf(dataSnapshot.child("states2").getValue());

                    if(!Simg1.equals("no")){
                            referencedel= FirebaseStorage.getInstance().getReferenceFromUrl(Simg1);
                            referencedel.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    reference.child("studentDetail").child(receiverUserID).child("states1").setValue("no");
                                    NotificationHelper notificationHelper = new NotificationHelper(context);
                                    NotificationCompat.Builder nb = notificationHelper.getChannelNotification("Aleart","your status update deleted");
                                    notificationHelper.getManager().notify(1,nb.build());


                                }
                            });
                        }

                        if(!Simg2.equals("no")){
                            referencedel=FirebaseStorage.getInstance().getReferenceFromUrl(Simg2);
                            referencedel.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    reference.child("studentDetail").child(receiverUserID).child("states2").setValue("no");
                                    NotificationHelper notificationHelper = new NotificationHelper(context);
                                    NotificationCompat.Builder nb = notificationHelper.getChannelNotification("Aleart","your status update deleted");
                                    notificationHelper.getManager().notify(1,nb.build());
                                }
                            });
                        }








                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
