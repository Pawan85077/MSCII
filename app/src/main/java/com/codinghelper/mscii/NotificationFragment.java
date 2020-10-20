package com.codinghelper.mscii;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    private View v;
    private RecyclerView myRequestsList;
    private DatabaseReference FriendRequestsRef,UserRef,FriendsRef;
    private FirebaseAuth auth;
    private String currentUserId;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_notification, container, false);
        FriendRequestsRef= FirebaseDatabase.getInstance().getReference().child("Add Friend Request");
        FriendsRef=FirebaseDatabase.getInstance().getReference().child("Friend list");
        UserRef=FirebaseDatabase.getInstance().getReference().child("studentDetail");
        auth=FirebaseAuth.getInstance();
        currentUserId=auth.getCurrentUser().getUid();
        myRequestsList=(RecyclerView)v.findViewById(R.id.notification);
        myRequestsList.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Friends> options=
                new FirebaseRecyclerOptions.Builder<Friends>()
                .setQuery(FriendRequestsRef.child(currentUserId),Friends.class)
                .build();
        FirebaseRecyclerAdapter<Friends,RequestViewHolder> adapter=
                new FirebaseRecyclerAdapter<Friends, RequestViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final RequestViewHolder holder, int position, @NonNull Friends model) {
                        holder.itemView.findViewById(R.id.confirm_friend).setVisibility(View.VISIBLE);
                        holder.itemView.findViewById(R.id.remove).setVisibility(View.VISIBLE);
                        final String list_user_id=getRef(position).getKey();
                        DatabaseReference getTyperef=getRef(position).child("Request_type");
                        getTyperef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              if(dataSnapshot.exists()){
                                  String type = String.valueOf(dataSnapshot.getValue());
                                  if(type.equals("received")){
                                      UserRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                              if(dataSnapshot.hasChild("imageUrl")) {
                                                  final String Simg = String.valueOf(dataSnapshot.child("imageUrl").getValue());
                                                  final String Sname = String.valueOf(dataSnapshot.child("username").getValue());
                                                  final String Sstatus = String.valueOf(dataSnapshot.child("userstatus").getValue());
                                                  holder.userName.setText(Sname);
                                                  holder.userStatus.setText(Sstatus);

                                                  Picasso.get().load(Simg).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.profileImage);
                                                  holder.confirm.setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View view) {
                                                       /*   CharSequence options[] = new CharSequence[]{
                                                                  "Confirm",
                                                                  "Remove"
                                                          };*/
                                                     //     AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                     //     builder.setTitle(Sname+" wants to be your Friend!!");
                                                     //     builder.setItems(options, new DialogInterface.OnClickListener() {
                                                     //         @Override
                                                     //         public void onClick(DialogInterface dialogInterface, int i) {
                                                         //         if(i==0){
                                                                    FriendsRef.child(currentUserId).child(list_user_id).child("Friends")
                                                                            .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                FriendsRef.child(list_user_id).child(currentUserId).child("Friends")
                                                                                        .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if(task.isSuccessful()){
                                                                                            FriendRequestsRef.child(currentUserId).child(list_user_id)
                                                                                                    .removeValue()
                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            if(task.isSuccessful()){
                                                                                                                FriendRequestsRef.child(list_user_id).child(currentUserId)
                                                                                                                        .removeValue()
                                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                            @Override
                                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                if(task.isSuccessful()){
                                                                                                                                    Toast.makeText(getContext(),"New Friend Added",Toast.LENGTH_SHORT).show();
                                                                                                                                }
                                                                                                                            }
                                                                                                                        });

                                                                                                            }
                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                    });
                                                      }
                                                  });

                                                            //      }
                                                             //     if(i==1){
                                                  holder.remove.setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View view) {
                                                          FriendRequestsRef.child(currentUserId).child(list_user_id)
                                                                  .removeValue()
                                                                  .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                      @Override
                                                                      public void onComplete(@NonNull Task<Void> task) {
                                                                          if(task.isSuccessful()){
                                                                              FriendRequestsRef.child(list_user_id).child(currentUserId)
                                                                                      .removeValue()
                                                                                      .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                          @Override
                                                                                          public void onComplete(@NonNull Task<Void> task) {
                                                                                              if(task.isSuccessful()){
                                                                                                  Toast.makeText(getContext(),"Request Removed",Toast.LENGTH_SHORT).show();
                                                                                              }
                                                                                          }
                                                                                      });

                                                                          }
                                                                      }
                                                                  });
                                                      }
                                                  });


                                                              //    }
                                                            //  }
                                                         // });
                                                         // builder.show();
                                               //       }
                                               //   });
                                              }
                                              }

                                          @Override
                                          public void onCancelled(@NonNull DatabaseError databaseError) {

                                          }
                                      });
                                  }else if(type.equals("sent")){
                                      final Button request_sent_button=holder.itemView.findViewById(R.id.confirm_friend);
                                      request_sent_button.setText("Request sent");
                                      holder.itemView.findViewById(R.id.remove).setVisibility(View.INVISIBLE);
                                      UserRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                              if(dataSnapshot.hasChild("imageUrl")) {
                                                  final String Simg = String.valueOf(dataSnapshot.child("imageUrl").getValue());
                                                  final String Sname = String.valueOf(dataSnapshot.child("username").getValue());
                                                  final String Sstatus = String.valueOf(dataSnapshot.child("userstatus").getValue());
                                                  holder.userName.setText(Sname);
                                                  holder.userStatus.setText(Sstatus);

                                                  Picasso.get().load(Simg).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.profileImage);

                                                  //      }
                                                  //     if(i==1){
                                                  holder.confirm.setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View view) {

                                                  CharSequence options[]=new CharSequence[]{
                                                    "Continue",
                                                    "Cancel"
                                                  };
                                                          AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
                                                          builder.setTitle("Are you sure want to Cancel sent request?");
                                                  builder.setItems(options, new DialogInterface.OnClickListener() {
                                                      @Override
                                                      public void onClick(DialogInterface dialogInterface, int i) {
                                                          if(i==0){
                                                              FriendRequestsRef.child(currentUserId).child(list_user_id)
                                                                      .removeValue()
                                                                      .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                          @Override
                                                                          public void onComplete(@NonNull Task<Void> task) {
                                                                              if(task.isSuccessful()){
                                                                                  FriendRequestsRef.child(list_user_id).child(currentUserId)
                                                                                          .removeValue()
                                                                                          .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                              @Override
                                                                                              public void onComplete(@NonNull Task<Void> task) {
                                                                                                  if(task.isSuccessful()){
                                                                                                      Toast.makeText(getContext(),"Request sent canceled",Toast.LENGTH_SHORT).show();
                                                                                                  }
                                                                                              }
                                                                                          });

                                                                              }
                                                                          }
                                                                      });
                                                          }
                                                          if(i==1){
                                                              Toast.makeText(getContext(),"Canceled",Toast.LENGTH_SHORT).show();

                                                          }
                                                      }
                                                  });
                                                  builder.show();
                                              }
                                          });



                                                  //    }
                                                  //  }
                                                  // });
                                                  // builder.show();
                                                  //       }
                                                  //   });
                                              }
                                          }

                                          @Override
                                          public void onCancelled(@NonNull DatabaseError databaseError) {

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

                    @NonNull
                    @Override
                    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout,parent,false);
                        RequestViewHolder viewHolder=new RequestViewHolder(view);
                        return viewHolder;
                    }
                };
        myRequestsList.setAdapter(adapter);
        adapter.startListening();
    }
    public static class RequestViewHolder extends RecyclerView.ViewHolder{
        TextView userName, userStatus;
        CircularImageView profileImage;
        Button confirm,remove;
        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.user_profile_name);
            userStatus=itemView.findViewById(R.id.user_profile_status);
            profileImage=itemView.findViewById(R.id.user_profile_image);
            confirm=itemView.findViewById(R.id.confirm_friend);
            remove=itemView.findViewById(R.id.remove);

        }
    }

}
