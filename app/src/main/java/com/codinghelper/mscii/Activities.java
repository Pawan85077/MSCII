package com.codinghelper.mscii;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class Activities extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference userQuestion,Likesref,root;
    private FirebaseAuth mAuth;
    private String currentUserId;
    ProgressDialog progressDialog;
    boolean Likechecker = false;


    public Activities() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_activities, container, false);
        recyclerView=(RecyclerView)v.findViewById(R.id.Question_recycle);
        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        progressDialog = new ProgressDialog(getActivity(),R.style.AlertDialogTheme);
        progressDialog.setMessage("loading..");
        progressDialog.setCanceledOnTouchOutside(false);
        userQuestion= FirebaseDatabase.getInstance().getReference().child("Questions");
       // Watchref=FirebaseDatabase.getInstance().getReference().child("Questions");
        root= FirebaseDatabase.getInstance().getReference().child("studentDetail");
        Likesref= FirebaseDatabase.getInstance().getReference().child("LikesC");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        progressDialog.show();
        FirebaseRecyclerOptions<StudentQuestion> options=
                new FirebaseRecyclerOptions.Builder<StudentQuestion>()
                        .setQuery(userQuestion,StudentQuestion.class)
                        .build();
        FirebaseRecyclerAdapter<StudentQuestion,QuestionViewHolder> adapter=
                new FirebaseRecyclerAdapter<StudentQuestion, QuestionViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final QuestionViewHolder holder, int position, @NonNull final StudentQuestion model) {
                        holder.Questions.setText(model.getQuestionAsked());
                        if(model.getAnswer().equals("Not answered yet!!")){
                            holder.up.setVisibility(View.INVISIBLE);
                            holder.Likes.setVisibility(View.INVISIBLE);
                            holder.Answers.loadData("Not answered yet","text/html",null);
                        }else {
                          //  holder.Answers.setText(model.getAnswer());
                            holder.Answers.getSettings().setJavaScriptEnabled(true);
                            holder.Answers.loadData(model.getAnswer(),"text/html",null);
                            holder.up.setVisibility(View.VISIBLE);
                            holder.Likes.setVisibility(View.VISIBLE);
                        }
                        holder.Askername.setText(model.getAskerName());
                        holder.topic.setText(model.getTopic());
                        Picasso.get().load(model.getAskerImage()).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.AskerImage);
                        Picasso.get().load(model.getAnswererImage()).fit().noFade().placeholder(R.drawable.main_stud).into(holder.AnswererImage);
                        final String question_id=getRef(position).getKey();
                        holder.Askername.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent profileIntent=new Intent(getActivity(),Show_profile_Activity.class);
                                profileIntent.putExtra("visit_user_id",model.getaskerUID());
                                startActivity(profileIntent);
                            }
                        });
                        holder.AnswererImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                userQuestion.child(question_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String Sfinal =String.valueOf(dataSnapshot.child("FinalAnswererId").getValue());
                                        Intent profileIntent=new Intent(getActivity(),Show_profile_Activity.class);
                                        profileIntent.putExtra("visit_user_id",Sfinal);
                                        startActivity(profileIntent);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                        holder.AskerImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent profileIntent=new Intent(getActivity(),Show_profile_Activity.class);
                                profileIntent.putExtra("visit_user_id",model.getaskerUID());
                                startActivity(profileIntent);
                            }
                        });
                        holder.setLikesButtonStatus(question_id);

                        holder.up.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Likechecker =true;
                               Likesref.addValueEventListener(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                       if(Likechecker){
                                           if(dataSnapshot.child(question_id).hasChild(currentUserId)){
                                               Likesref.child(question_id).child(currentUserId).removeValue();
                                               Likechecker = false;
                                           }else {
                                               Likesref.child(question_id).child(currentUserId).setValue(true);

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

                        holder.Flagbtn.setText(model.getposition());
                        holder.Flagbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(!model.getposition().equals("Live")){
                                    if(!model.getaskerUID().equals(currentUserId)){
                                        userQuestion.child(question_id).child("AnswererId").setValue(currentUserId);
                                        Intent profileIntent = new Intent(getActivity(), answeringActivity.class);
                                        profileIntent.putExtra("question_id", question_id);
                                        profileIntent.putExtra("current_answerer_id", currentUserId);
                                        startActivity(profileIntent);
                                        userQuestion.child(question_id).child("position").setValue("Live");
                                    }else{
                                        Toast.makeText(getActivity(), "you cant answer ur question", Toast.LENGTH_LONG).show();
                                    }


                                }else {

                                   /* root.child(currentUserId).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String Simg = String.valueOf(dataSnapshot.child("imageUrl").getValue());*/
                                            String jk=userQuestion.child(question_id).child("people").push().getKey();
                                            userQuestion.child(question_id).child("people").child(jk).child("peopleUID").setValue(currentUserId);

                                            Intent profileIntent = new Intent(getActivity(), LiveActivity.class);
                                            profileIntent.putExtra("question_id", question_id);
                                            profileIntent.putExtra("pid",jk);
                                            startActivity(profileIntent);
                                      //  }

                                       /* @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });*/

                                }

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.question_view,parent,false);
                        QuestionViewHolder viewHolder=new QuestionViewHolder(view);
                        progressDialog.dismiss();
                        return viewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public static class QuestionViewHolder extends RecyclerView.ViewHolder{
        TextView Questions;
        WebView Answers;
        TextView       Likes;
        CircularImageView AskerImage;
        CircularImageView AnswererImage;
        Button Flagbtn,Askername,topic;
        ShineButton up,down;
        int countLikes;
        String currentUserId;
        DatabaseReference Likesref,root;
        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            Questions=itemView.findViewById(R.id.AllQuestion);
            Flagbtn=itemView.findViewById(R.id.flagBtn);
            root= FirebaseDatabase.getInstance().getReference();
            Answers=itemView.findViewById(R.id.Answer2ques);
            AnswererImage=itemView.findViewById(R.id.Answerer_profile_image);
            AskerImage=itemView.findViewById(R.id.question_asker_profile_image);
            Askername=itemView.findViewById(R.id.name);
            topic=itemView.findViewById(R.id.sub);
            up=itemView.findViewById(R.id.tup);
            Likesref= FirebaseDatabase.getInstance().getReference().child("LikesC");
            currentUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                Likes=itemView.findViewById(R.id.countLikes);
        }

        public void setLikesButtonStatus(final String questionID){
            Likesref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(questionID).hasChild(currentUserId)){
                        countLikes=(int)dataSnapshot.child(questionID).getChildrenCount();
                      //  up.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                        Likes.setText(Integer.toString(countLikes));


                    }else {
                        countLikes=(int)dataSnapshot.child(questionID).getChildrenCount();
                      //  up.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                        Likes.setText(Integer.toString(countLikes));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }



}
