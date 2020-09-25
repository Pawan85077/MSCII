package com.codinghelper.mscii;

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
import android.widget.Button;
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
import com.squareup.picasso.Picasso;

public class Activities extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference userQuestion;
    private FirebaseAuth mAuth;
    private String currentUserId,askerID,CurrentPos;


    public Activities() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_activities, container, false);
        recyclerView=(RecyclerView)v.findViewById(R.id.Question_recycle);
        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        userQuestion= FirebaseDatabase.getInstance().getReference().child("Questions");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<StudentQuestion> options=
                new FirebaseRecyclerOptions.Builder<StudentQuestion>()
                        .setQuery(userQuestion,StudentQuestion.class)
                        .build();
        FirebaseRecyclerAdapter<StudentQuestion,QuestionViewHolder> adapter=
                new FirebaseRecyclerAdapter<StudentQuestion, QuestionViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull QuestionViewHolder holder, int position, @NonNull StudentQuestion model) {
                        holder.Questions.setText(model.getQuestionAsked());
                        if(model.getAnswer().equals("Not answered yet!!")){
                            holder.Answers.setTextColor(Color.parseColor("#FF8000"));
                            holder.Answers.setText(model.getAnswer());
                        }else {
                            holder.Answers.setText(model.getAnswer());
                        }
                        holder.Askername.setText(model.getAskerName());
                        holder.topic.setText(model.getTopic());
                        Picasso.get().load(model.getAskerImage()).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.AskerImage);
                        Picasso.get().load(model.getAnswererImage()).fit().noFade().into(holder.AnswererImage);
                        final String question_id=getRef(position).getKey();
                        userQuestion.child(question_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                 askerID=String.valueOf(dataSnapshot.child("askerUID").getValue());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        holder.Flagbtn.setText(model.getposition());
                        holder.Flagbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                userQuestion.child(question_id).child("AnswererId").setValue(currentUserId);
                                Intent profileIntent = new Intent(getActivity(), answeringActivity.class);
                                profileIntent.putExtra("question_id", question_id);
                                profileIntent.putExtra("current_answerer_id", currentUserId);
                                startActivity(profileIntent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.question_view,parent,false);
                        QuestionViewHolder viewHolder=new QuestionViewHolder(view);
                        return viewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public static class QuestionViewHolder extends RecyclerView.ViewHolder{
        TextView Questions;
        TextView Answers;
        CircularImageView AskerImage;
        CircularImageView AnswererImage;
        Button Flagbtn,Askername,topic;
        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            Questions=itemView.findViewById(R.id.AllQuestion);
            Flagbtn=itemView.findViewById(R.id.flagBtn);
            Answers=itemView.findViewById(R.id.Answer2ques);
            AnswererImage=itemView.findViewById(R.id.Answerer_profile_image);
            AskerImage=itemView.findViewById(R.id.question_asker_profile_image);
            Askername=itemView.findViewById(R.id.name);
            topic=itemView.findViewById(R.id.sub);
        }
    }
}
