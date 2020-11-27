package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ComputerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseAuth auth;
    DatabaseReference userQuestion,Root,Likesref;
    String currentUserID,recivedTopic;
    boolean Likechecker = false;
    int reportValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer);
        recivedTopic=getIntent().getExtras().get("Topic").toString();
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.navigation_toolbar_computer);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(recivedTopic);
        auth=FirebaseAuth.getInstance();
        currentUserID=auth.getCurrentUser().getUid();
        recyclerView=(RecyclerView)findViewById(R.id.Question_recycle_computer);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        userQuestion= FirebaseDatabase.getInstance().getReference().child("Questions");
        Root=FirebaseDatabase.getInstance().getReference().child("studentDetail");
        Likesref= FirebaseDatabase.getInstance().getReference().child("LikesC");


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<StudentQuestion> options=
                new FirebaseRecyclerOptions.Builder<StudentQuestion>()
                        .setQuery(userQuestion.orderByChild("Topic").equalTo(recivedTopic),StudentQuestion.class)
                        .build();



        FirebaseRecyclerAdapter<StudentQuestion, Activities.QuestionViewHolder> adapter=
                new FirebaseRecyclerAdapter<StudentQuestion, Activities.QuestionViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final Activities.QuestionViewHolder holder, int position, @NonNull final StudentQuestion model) {
                        holder.Questions.setText(model.getQuestionAsked());

                        if (model.getAnswer().equals("Not answered yet!!")) {
                            holder.up.setVisibility(View.INVISIBLE);
                            holder.up.setEnabled(false);
                            holder.Likes.setVisibility(View.INVISIBLE);
                        } else {
                            holder.up.setVisibility(View.VISIBLE);
                            holder.up.setEnabled(true);
                            holder.Likes.setVisibility(View.VISIBLE);
                        }

                        holder.aSwitch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (holder.aSwitch.isChecked()) {

                                    holder.Answers.setVisibility(View.VISIBLE);
                                    if (model.getAnswer().equals("Not answered yet!!")) {
                                        holder.up.setVisibility(View.INVISIBLE);
                                        holder.up.setEnabled(false);
                                        holder.Likes.setVisibility(View.INVISIBLE);
                                        holder.Answers.loadData("Not answered yet", "text/html", null);
                                    } else {
                                        //  holder.Answers.setText(model.getAnswer());
                                        holder.Answers.getSettings().setJavaScriptEnabled(true);
                                        holder.Answers.loadData(model.getAnswer(), "text/html", null);
                                        holder.up.setVisibility(View.VISIBLE);
                                        holder.up.setEnabled(true);
                                        holder.Likes.setVisibility(View.VISIBLE);
                                    }
                                }else {
                                    holder.Answers.loadData("","text/html",null);
                                    holder.Answers.setVisibility(View.INVISIBLE);
                                }
                            }
                        });


                        holder.Askername.setText(model.getAskerName());
                        holder.topic.setText(model.getTopic());
                        Picasso.get().load(model.getAskerImage()).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.AskerImage);
                        Picasso.get().load(model.getAnswererImage()).fit().noFade().placeholder(R.drawable.main_stud).into(holder.AnswererImage);
                        final String question_id=getRef(position).getKey();

                        holder.tdot.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PopupMenu popupMenu=new PopupMenu(getApplicationContext(),holder.tdot);
                                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        int id = menuItem.getItemId();
                                        if (id == R.id.qr) {


                                            final AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext(),R.style.Theme_AppCompat_Light_Dialog_MinWidth);
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
                                                        userQuestion.child(question_id).child("QreportReason").setValue(temp[0]);
                                                    }else {
                                                        userQuestion.child(question_id).child("QreportReason").setValue("Inappropriate");

                                                    }
                                                    userQuestion.child(question_id).child("Qreported").setValue("yes");
                                                    Toast.makeText(getApplicationContext(), "Reported successful", Toast.LENGTH_LONG).show();



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



                        userQuestion.child(question_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String Sfinal =String.valueOf(dataSnapshot.child("FinalAnswererId").getValue());
                                String Sreport =String.valueOf(dataSnapshot.child("reportedTimes").getValue());
                                String Ssreport =String.valueOf(dataSnapshot.child("reported").getValue());
                                String Sqreport =String.valueOf(dataSnapshot.child("Qreported").getValue());
                                holder.showReport.setVisibility(View.INVISIBLE);
                                holder.showReport.setEnabled(false);
                                holder.reports.setVisibility(View.INVISIBLE);
                                holder.Qreport.setVisibility(View.INVISIBLE);
                                holder.Qreport.setEnabled(false);

                                if(Sqreport.equals("yes")){
                                    holder.Qreport.setVisibility(View.VISIBLE);
                                    holder.Qreport.setEnabled(true);
                                }
                                if(Ssreport.equals("yes")){
                                    holder.showReport.setVisibility(View.VISIBLE);
                                    holder.showReport.setEnabled(true);
                                    holder.reports.setVisibility(View.VISIBLE);
                                    holder.reports.setText(Sreport);
                                }
                                if(Sfinal.equals(currentUserID)){
                                    holder.editAns.setVisibility(View.VISIBLE);
                                    holder.editAns.setEnabled(true);
                                    holder.editAns.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent profileIntent = new Intent(getApplicationContext(), answeringActivity.class);
                                            profileIntent.putExtra("question_id", question_id);
                                            profileIntent.putExtra("current_answerer_id", currentUserID);
                                            userQuestion.child(question_id).child("position").setValue("Live");
                                            startActivity(profileIntent);
                                        }
                                    });
                                }else{
                                    holder.editAns.setVisibility(View.INVISIBLE);
                                    holder.editAns.setEnabled(false);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        holder.Qreport.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                userQuestion.child(question_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String report = String.valueOf(dataSnapshot.child("QreportReason").getValue());
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext(), R.style.Theme_AppCompat_Light_Dialog_MinWidth);
                                        builder.setTitle("Reporting reason:-");
                                        builder.setMessage(report);
                                        builder.setNeutralButton("Un-report", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Root.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        String Madmod =String.valueOf(dataSnapshot.child("Level").getValue());
                                                        if(Madmod.equals("moderator")){
                                                            userQuestion.child(question_id).child("Qreported").setValue("no");
                                                            Toast.makeText(getApplicationContext(),"Un-reported successfully!!", Toast.LENGTH_SHORT).show();
                                                        }else {
                                                            Toast.makeText(getApplicationContext(),"Only moderator can make answer Un-reported!!", Toast.LENGTH_SHORT).show();

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
                                                Root.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        String Madmod =String.valueOf(dataSnapshot.child("Level").getValue());
                                                        if(Madmod.equals("moderator")){
                                                            Likesref.child(question_id).removeValue();
                                                            userQuestion.child(question_id).removeValue();
                                                            Toast.makeText(getApplicationContext(),"answer deleted successfully!!", Toast.LENGTH_SHORT).show();
                                                        }else {
                                                            Toast.makeText(getApplicationContext(),"Only moderator can delete answer!!", Toast.LENGTH_SHORT).show();

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
                        holder.Askername.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent profileIntent=new Intent(getApplicationContext(),Show_profile_Activity.class);
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
                                        Intent profileIntent=new Intent(getApplicationContext(),Show_profile_Activity.class);
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
                                Intent profileIntent=new Intent(getApplicationContext(),Show_profile_Activity.class);
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
                                            if(dataSnapshot.child(question_id).hasChild(currentUserID)){
                                                Likesref.child(question_id).child(currentUserID).removeValue();
                                                Likechecker = false;
                                            }else {
                                                Likesref.child(question_id).child(currentUserID).setValue(true);

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
                        holder.showReport.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                reportReason(question_id);
                            }
                        });
                        holder.Flagbtn.setText(model.getposition());
                        holder.Flagbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(model.getposition().equals("answer")){
                                    if(!model.getaskerUID().equals(currentUserID)){
                                        userQuestion.child(question_id).child("AnswererId").setValue(currentUserID);
                                        Intent profileIntent = new Intent(getApplicationContext(), answeringActivity.class);
                                        profileIntent.putExtra("question_id", question_id);
                                        profileIntent.putExtra("current_answerer_id", currentUserID);
                                        startActivity(profileIntent);
                                        userQuestion.child(question_id).child("position").setValue("Live");
                                    }else{
                                        Toast.makeText(getApplicationContext(), "you cant answer ur question", Toast.LENGTH_LONG).show();
                                    }


                                }else if(model.getposition().equals("Live")){

                                   /* root.child(currentUserId).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String Simg = String.valueOf(dataSnapshot.child("imageUrl").getValue());*/
                                    String jk=userQuestion.child(question_id).child("people").push().getKey();
                                    userQuestion.child(question_id).child("people").child(jk).child("peopleUID").setValue(currentUserID);

                                    Intent profileIntent = new Intent(getApplicationContext(), LiveActivity.class);
                                    profileIntent.putExtra("question_id", question_id);
                                    profileIntent.putExtra("pid",jk);
                                    startActivity(profileIntent);
                                    //  }

                                       /* @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });*/

                                }else if(model.getposition().equals("Report")){
                                    final AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext(),R.style.Theme_AppCompat_Light_Dialog_MinWidth);
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

                                            if(temp[0] != null){
                                                report( temp[0],question_id);
                                            }else {
                                                report( "Inappropriate",question_id);
                                            }

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


                                }else {
                                    reportReason(question_id);

                                }

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public Activities.QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.question_view,parent,false);
                        Activities.QuestionViewHolder viewHolder=new Activities.QuestionViewHolder(view);
                        return viewHolder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    private void report(String s, final String question_id) {
        userQuestion.child(question_id).child("reportReason").setValue(s);
        userQuestion.child(question_id).child("position").setValue("Reported");
        userQuestion.child(question_id).child("reported").setValue("yes");
        userQuestion.child(question_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Sfinal =String.valueOf(dataSnapshot.child("reportedTimes").getValue());
                reportValue=Integer.parseInt(Sfinal);
                reportValue++;
                userQuestion.child(question_id).child("reportedTimes").setValue(reportValue);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(getApplicationContext(), "Reported successful", Toast.LENGTH_LONG).show();
    }
    private void reportReason(final String question_id) {
        userQuestion.child(question_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String report = String.valueOf(dataSnapshot.child("reportReason").getValue());
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext(), R.style.Theme_AppCompat_Light_Dialog_MinWidth);
                builder.setTitle("Reporting reason:-");
                builder.setMessage(report);
                builder.setNeutralButton("Un-report", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Root.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String Madmod =String.valueOf(dataSnapshot.child("Level").getValue());
                                if(Madmod.equals("moderator")){
                                    userQuestion.child(question_id).child("position").setValue("Report");
                                    userQuestion.child(question_id).child("reported").setValue("no");
                                    Toast.makeText(getApplicationContext(),"Un-reported successfully!!", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(),"Only moderator can make answer Un-reported!!", Toast.LENGTH_SHORT).show();

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
                        Root.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String Madmod =String.valueOf(dataSnapshot.child("Level").getValue());
                                if(Madmod.equals("moderator")){
                                    userQuestion.child(question_id).child("Answer").setValue("Not answered yet!!");
                                    userQuestion.child(question_id).child("position").setValue("answer");
                                    userQuestion.child(question_id).child("AnswererImage").setValue("https://firebasestorage.googleapis.com/v0/b/mscii-8cb88.appspot.com/o/skull%20(1).png?alt=media&token=22a5e53b-4270-40b9-bbf2-41109c135557");
                                    userQuestion.child(question_id).child("AnswererId").setValue("Not yet");
                                    userQuestion.child(question_id).child("FinalAnswererId").setValue("Not yet");
                                    userQuestion.child(question_id).child("reportedTimes").setValue(0);
                                    userQuestion.child(question_id).child("reportReason").setValue("");
                                    userQuestion.child(question_id).child("reported").setValue("no");
                                    Likesref.child(question_id).removeValue();
                                    Toast.makeText(getApplicationContext(),"answer deleted successfully!!", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(),"Only moderator can delete answer!!", Toast.LENGTH_SHORT).show();

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
}
