package com.codinghelper.mscii;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    private DatabaseReference Root,userQuestion;
    private FirebaseAuth auth;
    private String currentUserID;




    public Explore() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_explore, container, false);
        SearchBar=(EditText)v.findViewById(R.id.search);
        Askbtn=(Button)v.findViewById(R.id.buttonAsk);
        auth=FirebaseAuth.getInstance();
        currentUserID=auth.getCurrentUser().getUid();
        userQuestion=FirebaseDatabase.getInstance().getReference().child("Questions");
        Root=FirebaseDatabase.getInstance().getReference().child("studentDetail");
      //  questionRoot.child("Question_Asked").setValue("");



        Askbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String question = SearchBar.getText().toString();
                if (TextUtils.isEmpty(question)) {
                    SearchBar.setError("enter question");
                    SearchBar.setFocusable(true);
                } else{
                     SearchBar.setText("");

                   //  userQuestion.push().child("QuestionAsked").setValue(question)
                       Root.child(currentUserID).addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               if (dataSnapshot.exists()) {
                                   String key=userQuestion.push().getKey();
                                   String Simg =String.valueOf(dataSnapshot.child("imageUrl").getValue());
                                   HashMap rec=new HashMap();
                                   rec.put("QuestionAsked",question);
                                   rec.put("askerUID",currentUserID);
                                   rec.put("Answer","Not answered yet!!");
                                   rec.put("AskerImage",Simg);
                                   rec.put("AnswererImage","data:image/jpeg;base64,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");

                                   userQuestion.child(key).updateChildren(rec).addOnCompleteListener(new OnCompleteListener() {
                                       @Override
                                       public void onComplete(@NonNull Task task) {
                                           if(task.isSuccessful()){
                                               Toast.makeText(getActivity(), "your Question is available on Activities page", Toast.LENGTH_LONG).show();
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

      /*  SearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
            if(!editable.toString().isEmpty()){
                search(editable.toString());
            }else {
                if(editable.toString().isEmpty()) {
                    Askbtn.setVisibility(View.VISIBLE);
                    Askbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String question = editable.toString();
                            questionRoot.child("Question Asked").setValue(question);
                        }
                    });
                    Askbtn.setVisibility(View.INVISIBLE);
                }
         //   }
            }
        });*/
        return v;
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
