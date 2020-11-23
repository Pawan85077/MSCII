package com.codinghelper.mscii;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Explore extends Fragment {
    private EditText SearchBar;
    private Button Askbtn;
    private DatabaseReference Root,userQuestion,Likesref;
    private FirebaseAuth auth;
    private String currentUserID;
    String[] selectTopic = {"--Select Topic--","Computer","MAths","physics"};
    String selectedTopic;
    ImageButton Computer,flyGame;
    long value;
    boolean Likechecker = false;
    Integer j;
    int reportValue;
    private RecyclerView recyclerView,recyclerView2,recyclerView3;







    public Explore() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_explore, container, false);

        SearchBar=(EditText)v.findViewById(R.id.search);
        Computer=(ImageButton)v.findViewById(R.id.ComputerBtn);
        recyclerView=(RecyclerView)v.findViewById(R.id.qqRecycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView2=(RecyclerView)v.findViewById(R.id.Lrecycle);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setReverseLayout(true);
        linearLayoutManager2.setStackFromEnd(true);
        recyclerView2.setLayoutManager(linearLayoutManager2);

       /* recyclerView3=(RecyclerView)v.findViewById(R.id.try_rec);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext());
        linearLayoutManager3.setReverseLayout(true);
        linearLayoutManager3.setStackFromEnd(true);
        recyclerView3.setLayoutManager(linearLayoutManager3);*/
        Askbtn=(Button)v.findViewById(R.id.buttonAsk);
        Likesref= FirebaseDatabase.getInstance().getReference().child("LikesC");
        flyGame=(ImageButton)v.findViewById(R.id.game);
        auth=FirebaseAuth.getInstance();
        currentUserID=auth.getCurrentUser().getUid();
        userQuestion=FirebaseDatabase.getInstance().getReference().child("Questions");
        Root=FirebaseDatabase.getInstance().getReference().child("studentDetail");
        Spinner spinTopic = (Spinner)v.findViewById(R.id.selectTopic);
        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, selectTopic);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTopic.setAdapter(courseAdapter);
        flyGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), splashGameScreen.class));
            }
        });
        SearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.INVISIBLE);
                recyclerView.setEnabled(false);
            }
        });

       // Root.child(currentUserID).child("countA").setValue(j);
        spinTopic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTopic = parent.getItemAtPosition(position).toString();
                if (selectedTopic!="--Select Topic--") {
                    Toast.makeText(parent.getContext(), selectedTopic, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ComputerActivity.class));

            }
        });

        //  questionRoot.child("Question_Asked").setValue("");




        Askbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String question = SearchBar.getText().toString();
                final String Topic = selectedTopic;
                if (TextUtils.isEmpty(question)){
                    SearchBar.setError("Ask your question!!");
                    SearchBar.setFocusable(true);
                }else if (Topic.equals("--Select Topic--")){
                    Toast.makeText(getActivity(), "select Topic related to your question!!", Toast.LENGTH_LONG).show();
                }else{
                     SearchBar.setText("");
                    recyclerView.setVisibility(View.INVISIBLE);
                    recyclerView.setEnabled(false);
                   //  userQuestion.push().child("QuestionAsked").setValue(question)




                   /* */




                    //unconfirmed change but working fine addvalueeentlistner
                       Root.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               if (dataSnapshot.exists()) {
                                   String key=userQuestion.push().getKey();
                                   String Simg =String.valueOf(dataSnapshot.child("imageUrl").getValue());
                                   String Sname =String.valueOf(dataSnapshot.child("username").getValue());
                                   value= (long)dataSnapshot.child("countQ").getValue();
                                   value=value+1;
                                   Root.child(currentUserID).child("countQ").setValue(value);
                                   HashMap rec=new HashMap();
                                   rec.put("QuestionAsked",question);
                                   rec.put("askerUID",currentUserID);
                                   rec.put("Answer","Not answered yet!!");
                                   rec.put("AskerImage",Simg);
                                   rec.put("Topic",Topic);
                                   rec.put("position","answer");
                                   rec.put("AnswererImage","https://firebasestorage.googleapis.com/v0/b/mscii-8cb88.appspot.com/o/skull%20(1).png?alt=media&token=22a5e53b-4270-40b9-bbf2-41109c135557");
                                   rec.put("AnswererId","Not yet");
                                   rec.put("FinalAnswererId","Not yet");
                                   rec.put("AskerName",Sname);
                                   rec.put("AnswererName","unknown");
                                   rec.put("reportedTimes",0);
                                   userQuestion.child(key).updateChildren(rec).addOnCompleteListener(new OnCompleteListener() {
                                       @Override
                                       public void onComplete(@NonNull Task task) {
                                           if(task.isSuccessful()){
                                               Toast.makeText(getActivity(), "Go to Activities <3", Toast.LENGTH_LONG).show();

                                           }
                                       }
                                   });


                               }
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });

                }
            }
        });

       SearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setEnabled(true);
                String question = editable.toString();
                processsearch(question);

            }

        });
       return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<StudentQuestion> options=
                    new FirebaseRecyclerOptions.Builder<StudentQuestion>()
                            .setQuery(userQuestion.orderByChild("position").equalTo("Live"),StudentQuestion.class)
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
                                    if(Sfinal.equals(currentUserID)){
                                        holder.editAns.setVisibility(View.VISIBLE);
                                        holder.editAns.setEnabled(true);
                                        holder.editAns.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent profileIntent = new Intent(getActivity(), answeringActivity.class);
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
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_MinWidth);
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
                                                    Root.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                            Intent profileIntent = new Intent(getActivity(), answeringActivity.class);
                                            profileIntent.putExtra("question_id", question_id);
                                            profileIntent.putExtra("current_answerer_id", currentUserID);
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
                                        userQuestion.child(question_id).child("people").child(jk).child("peopleUID").setValue(currentUserID);

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
                        public Activities.QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.question_view,parent,false);
                            Activities.QuestionViewHolder viewHolder=new Activities.QuestionViewHolder(view);
                            return viewHolder;
                        }
                    };

            recyclerView2.setAdapter(adapter);
            adapter.startListening();














    }

    private void processsearch(String question) {
        FirebaseRecyclerOptions<StudentQuestion> options=
                new FirebaseRecyclerOptions.Builder<StudentQuestion>()
                        .setQuery(userQuestion.orderByChild("QuestionAsked").startAt(question).endAt(question+"\uf8ff"),StudentQuestion.class)
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
                                if(Sfinal.equals(currentUserID)){
                                    holder.editAns.setVisibility(View.VISIBLE);
                                    holder.editAns.setEnabled(true);
                                    holder.editAns.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent profileIntent = new Intent(getActivity(), answeringActivity.class);
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
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_MinWidth);
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
                                                Root.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        Intent profileIntent = new Intent(getActivity(), answeringActivity.class);
                                        profileIntent.putExtra("question_id", question_id);
                                        profileIntent.putExtra("current_answerer_id", currentUserID);
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
                                    userQuestion.child(question_id).child("people").child(jk).child("peopleUID").setValue(currentUserID);

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
                        Root.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
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



   /* private void search(String editable) {
        Query query=questionRoot.orderByChild("Question Asked").startAt(editable).endAt(editable + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/


}
