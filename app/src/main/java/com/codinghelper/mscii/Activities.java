package com.codinghelper.mscii;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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
    int reportValue;


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
                                PopupMenu popupMenu=new PopupMenu(getActivity(),holder.tdot);
                                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        int id = menuItem.getItemId();
                                        if (id == R.id.qr) {


                                            final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity(),R.style.Theme_AppCompat_Light_Dialog_MinWidth);
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
                                if(Sfinal.equals(currentUserId)){
                                    holder.editAns.setVisibility(View.VISIBLE);
                                    holder.editAns.setEnabled(true);
                                    holder.editAns.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent profileIntent = new Intent(getActivity(), answeringActivity.class);
                                            profileIntent.putExtra("question_id", question_id);
                                            profileIntent.putExtra("current_answerer_id", currentUserId);
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
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_MinWidth);
                                        builder.setTitle("Reporting reason:-");
                                        builder.setMessage(report);
                                        builder.setNeutralButton("Un-report", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                root.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        String Madmod =String.valueOf(dataSnapshot.child("Level").getValue());
                                                        if(Madmod.equals("moderator")){
                                                            userQuestion.child(question_id).child("Qreported").setValue("no");
                                                            Toast.makeText(getActivity(),"Un-reported successfully!!", Toast.LENGTH_SHORT).show();
                                                        }else {
                                                            Toast.makeText(getActivity(),"Only moderator can make answer Un-reported!!", Toast.LENGTH_SHORT).show();

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
                                                root.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        String Madmod =String.valueOf(dataSnapshot.child("Level").getValue());
                                                        if(Madmod.equals("moderator")){
                                                            Likesref.child(question_id).removeValue();
                                                            userQuestion.child(question_id).removeValue();
                                                            Toast.makeText(getActivity(),"answer deleted successfully!!", Toast.LENGTH_SHORT).show();
                                                        }else {
                                                            Toast.makeText(getActivity(),"Only moderator can delete answer!!", Toast.LENGTH_SHORT).show();

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


                                }else if(model.getposition().equals("Live")){

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

                                }else if(model.getposition().equals("Report")){
                                    final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity(),R.style.Theme_AppCompat_Light_Dialog_MinWidth);
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

        Toast.makeText(getActivity(), "Reported successful", Toast.LENGTH_LONG).show();
    }

    private void reportReason(final String question_id) {
        userQuestion.child(question_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String report = String.valueOf(dataSnapshot.child("reportReason").getValue());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_MinWidth);
                builder.setTitle("Reporting reason:-");
                builder.setMessage(report);
                builder.setNeutralButton("Un-report", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        root.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String Madmod =String.valueOf(dataSnapshot.child("Level").getValue());
                                if(Madmod.equals("moderator")){
                                    userQuestion.child(question_id).child("position").setValue("Report");
                                    userQuestion.child(question_id).child("reported").setValue("no");
                                    Toast.makeText(getActivity(),"Un-reported successfully!!", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getActivity(),"Only moderator can make answer Un-reported!!", Toast.LENGTH_SHORT).show();

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
                        root.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                    Toast.makeText(getActivity(),"answer deleted successfully!!", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getActivity(),"Only moderator can delete answer!!", Toast.LENGTH_SHORT).show();

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

    public static class QuestionViewHolder extends RecyclerView.ViewHolder{
        TextView Questions;
        WebView Answers;
        TextView       Likes,reports;
        ImageButton showReport,tdot,Qreport;
        CircularImageView AskerImage;
        CircularImageView AnswererImage;
        Button Flagbtn,Askername,topic,editAns;
        ShineButton up,down;
        SwitchCompat aSwitch;
        int countLikes;
        String currentUserId;
        DatabaseReference Likesref,root;
        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            reports=itemView.findViewById(R.id.countreports);
            tdot=itemView.findViewById(R.id.threeDot);
            Qreport=itemView.findViewById(R.id.qreport);
            showReport=itemView.findViewById(R.id.showReportStatus);
            Questions=itemView.findViewById(R.id.AllQuestion);
            Flagbtn=itemView.findViewById(R.id.flagBtn);
            editAns=itemView.findViewById(R.id.editAns);
            aSwitch=itemView.findViewById(R.id.switchAns);
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
