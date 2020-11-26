package com.codinghelper.mscii;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference root,Likesref,Pokesref;
    private FirebaseAuth mAuth;
    private String currentUserId;
    ProgressDialog progressDialog;
    boolean Likechecker = false;
    boolean Pokechecker=false;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView=(RecyclerView)v.findViewById(R.id.user_update);
        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        progressDialog = new ProgressDialog(getActivity(),R.style.AlertDialogTheme);
        progressDialog.setMessage("loading..");
        progressDialog.setCanceledOnTouchOutside(true);
        root= FirebaseDatabase.getInstance().getReference();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        Likesref= FirebaseDatabase.getInstance().getReference().child("LikesC");
        Pokesref= FirebaseDatabase.getInstance().getReference().child("PokesC");




        return v;
    }

    @Override
    public void onStart(){
        super.onStart();
        progressDialog.show();
        FirebaseRecyclerOptions<watch> options=
                new FirebaseRecyclerOptions.Builder<watch>()
                        .setQuery(root.child("PublicView"),watch.class)
                        .build();
        FirebaseRecyclerAdapter<watch, HomeFragment.QuestionViewHolder> adapter=
                new FirebaseRecyclerAdapter<watch, HomeFragment.QuestionViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final HomeFragment.QuestionViewHolder holder, int position, @NonNull final watch model) {

                        final String public_id=getRef(position).getKey();

                      holder.userBigImage.setVisibility(View.GONE);
                      holder.userBio.setVisibility(View.GONE);
                      holder.UserSong.setVisibility(View.GONE);
                      holder.report.setVisibility(View.GONE);

                      root.child("studentDetail").child(model.getpeopleUID()).addValueEventListener(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              String Simg =String.valueOf(dataSnapshot.child("imageUrl").getValue());
                              String Sname =String.valueOf(dataSnapshot.child("username").getValue());
                              String Sgender =String.valueOf(dataSnapshot.child("gender").getValue());

                              Picasso.get().load(Simg).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.userImage);
                              holder.username.setText(Sname);
                              if(Sgender.equals("Male")){
                                  holder.gender.setText(" his");
                              }else {
                                  holder.gender.setText(" her");
                              }
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError databaseError) {

                          }
                      });

                      root.child("PublicView").child(public_id).addValueEventListener(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              String Feedreport =String.valueOf(dataSnapshot.child("Feedreported").getValue());

                                  if(Feedreport.equals("yes")){
                                      holder.report.setVisibility(View.VISIBLE);
                                  }


                                  if(dataSnapshot.child("publicImage").exists()){
                                  holder.updated.setText("Profile pic");
                                  holder.userBigImage.setVisibility(View.VISIBLE);
                                  String Simg =String.valueOf(dataSnapshot.child("publicImage").getValue());
                                  Picasso.get().load(Simg).fit().noFade().placeholder(R.drawable.error_img).into(holder.userBigImage);

                              }
                              if(dataSnapshot.child("publicBio").exists()){
                                  holder.updated.setText("Profile bio");
                                  holder.userBio.setVisibility(View.VISIBLE);
                                  String SBio =String.valueOf(dataSnapshot.child("publicBio").getValue());
                                  holder.userBio.setText(SBio);
                              }
                              if(dataSnapshot.child("publicSong").exists()){
                                  holder.updated.setText("Profile song");
                                  holder.UserSong.setVisibility(View.VISIBLE);
                                 // String Ssong =String.valueOf(dataSnapshot.child("publicSong").getValue());

                                  holder.UserSong.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {


                                          Intent profileIntent=new Intent(getActivity(),Show_profile_Activity.class);
                                          profileIntent.putExtra("visit_user_id",model.getpeopleUID());
                                          startActivity(profileIntent);

                                      }
                                  });

                              }
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError databaseError) {

                          }
                      });

                      holder.report.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {



                              root.child("PublicView").child(public_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                  @Override
                                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                      String report = String.valueOf(dataSnapshot.child("FeedreportReason").getValue());
                                      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_MinWidth);
                                      builder.setTitle("Reporting reason:-");
                                      builder.setMessage(report);
                                      builder.setNeutralButton("Un-report", new DialogInterface.OnClickListener() {
                                          @Override
                                          public void onClick(DialogInterface dialogInterface, int i) {
                                              root.child("studentDetail").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                  @Override
                                                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                      String Madmod =String.valueOf(dataSnapshot.child("Level").getValue());
                                                      if(Madmod.equals("moderator")){
                                                          root.child("PublicView").child(public_id).child("Feedreported").setValue("no");
                                                          Toast.makeText(getActivity(),"Un-reported successfully!!", Toast.LENGTH_SHORT).show();
                                                      }else {

                                                          Toast.makeText(getActivity(),"Only moderator can Un-report !!", Toast.LENGTH_SHORT).show();

                                                      }
                                                  }

                                                  @Override
                                                  public void onCancelled(@NonNull DatabaseError databaseError) {

                                                  }
                                              });



                                          }
                                      });
                                      builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                          @Override
                                          public void onClick(DialogInterface dialogInterface, int i) {

                                              root.child("studentDetail").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                  @Override
                                                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                      String Madmod =String.valueOf(dataSnapshot.child("Level").getValue());
                                                      if(Madmod.equals("moderator")||currentUserId.equals(model.getpeopleUID())){
                                                          root.child("PublicView").child(public_id).removeValue();
                                                          Toast.makeText(getActivity(),"Post deleted successfully!!", Toast.LENGTH_SHORT).show();
                                                      }else {

                                                          root.child("studentDetail").child(model.getpeopleUID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                              @Override
                                                              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                  String musername =String.valueOf(dataSnapshot.child("username").getValue());
                                                                  Toast.makeText(getActivity(),"Only moderator/"+musername+" can delete this post!!", Toast.LENGTH_SHORT).show();

                                                              }

                                                              @Override
                                                              public void onCancelled(@NonNull DatabaseError databaseError) {

                                                              }
                                                          });
                                                      }
                                                  }

                                                  @Override
                                                  public void onCancelled(@NonNull DatabaseError databaseError) {

                                                  }
                                              });
                                          }
                                      });
                                      builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                          @Override
                                          public void onClick(DialogInterface dialogInterface, int i) {
                                              dialogInterface.cancel();

                                          }
                                      });
                                      builder.show();

                                  }
                                  @Override
                                  public void onCancelled(@NonNull DatabaseError databaseError) {

                                  }
                              });





                          }
                      });
                        holder.setPokessButtonStatus(public_id);
                        holder.poke.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              Pokechecker =true;
                              Pokesref.addValueEventListener(new ValueEventListener() {
                                  @Override
                                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                      if(Pokechecker){
                                          if(dataSnapshot.child(public_id).hasChild(currentUserId)){
                                              Pokesref.child(public_id).child(currentUserId).removeValue();
                                              Pokechecker = false;
                                          }else {
                                              Pokesref.child(public_id).child(currentUserId).setValue(true);

                                              Pokechecker = false;


                                          }
                                      }
                                  }

                                  @Override
                                  public void onCancelled(@NonNull DatabaseError databaseError) {

                                  }
                              });
                          }
                      });
                        holder.setLikesButtonStatus(public_id);
                        holder.heart.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {

                              Likechecker =true;
                              Likesref.addValueEventListener(new ValueEventListener() {
                                  @Override
                                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                      if(Likechecker){
                                          if(dataSnapshot.child(public_id).hasChild(currentUserId)){
                                              Likesref.child(public_id).child(currentUserId).removeValue();
                                              Likechecker = false;
                                          }else {
                                              Likesref.child(public_id).child(currentUserId).setValue(true);

                                              Likechecker = false;


                                          }
                                      }
                                  }

                                  @Override
                                  public void onCancelled(@NonNull DatabaseError databaseError) {

                                  }
                              });

                          }
                      });

                      holder.threedot.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              PopupMenu popupMenu=new PopupMenu(getActivity(),holder.threedot);
                              popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                              popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                  @Override
                                  public boolean onMenuItemClick(MenuItem menuItem) {
                                      int id = menuItem.getItemId();
                                      if (id == R.id.qr) {

                                              AlertDialog.Builder builder=new AlertDialog.Builder(getActivity(),R.style.Theme_AppCompat_Light_Dialog_MinWidth);
                                              builder.setTitle("Why to report?");
                                              String[] items={"Inappropriate","Invalid","Irrevalent","Stupidity"};
                                              int checkeditem=0;
                                              final String[] temp = new String[1];
                                              builder.setSingleChoiceItems(items, checkeditem, new DialogInterface.OnClickListener() {
                                                  @Override
                                                  public void onClick(DialogInterface dialogInterface, int i) {
                                                      switch (i){
                                                          case 0:
                                                              temp[0] ="Inappropriate";
                                                              break;
                                                          case 1:
                                                              temp[0] ="Invalid";
                                                              break;
                                                          case 2:
                                                              temp[0] ="Irrevalent";
                                                              break;
                                                          case 3:
                                                              temp[0] ="Stupidity";
                                                              break;
                                                      }
                                                  }


                                              });
                                              builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                                                  @Override
                                                  public void onClick(DialogInterface dialogInterface, int i) {
                                                      if(temp[0]!=null){
                                                          root.child("PublicView").child(public_id).child("FeedreportReason").setValue(temp[0]);
                                                      }else {
                                                          root.child("PublicView").child(public_id).child("FeedreportReason").setValue("Inappropriate");

                                                      }
                                                      root.child("PublicView").child(public_id).child("Feedreported").setValue("yes");
                                                      Toast.makeText(getActivity(), "Reported successful", Toast.LENGTH_LONG).show();



                                                  }
                                              });
                                              builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                                  @Override
                                                  public void onClick(DialogInterface dialogInterface, int i) {
                                                      dialogInterface.cancel();
                                                  }
                                              });
                                              AlertDialog dialog =builder.create();
                                              dialog.setCanceledOnTouchOutside(true);
                                              dialog.show();
                                      }


                                      return true;
                                  }
                              });
                              popupMenu.show();
                          }
                      });

                    }

                    @NonNull
                    @Override
                    public HomeFragment.QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_home_feed_view,parent,false);
                        HomeFragment.QuestionViewHolder viewHolder=new HomeFragment.QuestionViewHolder(view);
                        progressDialog.dismiss();
                        return viewHolder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder{
        TextView username,gender,updated,like_count;
        TextView userBio,poked;
        ImageView userBigImage,UserSong,threedot,heart,poke;
        CircularImageView userImage;
        ImageButton report;
        DatabaseReference Likesref,Pokesref;
        String currentUserId;
        int countLikes;
        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.user_updated_name);
            userBio=itemView.findViewById(R.id.user_updated_bio);
            userImage=itemView.findViewById(R.id.user_updated_image);
            userBigImage=itemView.findViewById(R.id.user_updated_big_image);
            UserSong=itemView.findViewById(R.id.user_updated_song);
            gender=itemView.findViewById(R.id.gender);
            updated=itemView.findViewById(R.id.updated_thing);
            threedot=itemView.findViewById(R.id.home_threedot);
            report=itemView.findViewById(R.id.home_error_btn);
            poked=itemView.findViewById(R.id.home_poked);
            heart=itemView.findViewById(R.id.home_heart);
            poke=itemView.findViewById(R.id.home_poke);
            like_count=itemView.findViewById(R.id.home_like_count);
            Likesref= FirebaseDatabase.getInstance().getReference().child("LikesC");
            Pokesref= FirebaseDatabase.getInstance().getReference().child("PokesC");
            currentUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();



        }

        public void setLikesButtonStatus(final String public_id) {
            Likesref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(public_id).hasChild(currentUserId)){
                        countLikes=(int)dataSnapshot.child(public_id).getChildrenCount();
                          heart.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                        like_count.setText(Integer.toString(countLikes));


                    }else {
                        countLikes=(int)dataSnapshot.child(public_id).getChildrenCount();
                          heart.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                        like_count.setText(Integer.toString(countLikes));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void setPokessButtonStatus(final String public_id) {
            Pokesref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(public_id).hasChild(currentUserId)){
                        poke.setBackgroundResource(R.drawable.poked);
                        poked.setText("poked");


                    }else {
                        poke.setBackgroundResource(R.drawable.poke);
                        poked.setText("poke");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
