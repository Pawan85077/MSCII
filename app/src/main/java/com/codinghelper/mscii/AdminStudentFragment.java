package com.codinghelper.mscii;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminStudentFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    String currentUserID;

    ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Students");
        View v=inflater.inflate(R.layout.admin_fragment_student, container, false);
        setHasOptionsMenu(true);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID=firebaseAuth.getCurrentUser().getUid();
        recyclerView=(RecyclerView)v.findViewById(R.id.Admin_student_recycle);
        reference= FirebaseDatabase.getInstance().getReference().child("studentDetail");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressDialog = new ProgressDialog(getContext(),R.style.AlertDialogTheme);
        progressDialog.setMessage("loading..");
        progressDialog.setCanceledOnTouchOutside(false);
        return v;

    }


    @Override
    public void onStart() {
        super.onStart();
        progressDialog.show();
        FirebaseRecyclerOptions<Friends> options=
                new FirebaseRecyclerOptions.Builder<Friends>()
                        .setQuery(reference,Friends.class)
                        .build();

        FirebaseRecyclerAdapter<Friends, Find_friend_activity.FindFriendViewHolder> adapter=
                new FirebaseRecyclerAdapter<Friends, Find_friend_activity.FindFriendViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final Find_friend_activity.FindFriendViewHolder holder, final int position, @NonNull Friends model) {
                        String v_user_id=getRef(position).getKey();

                        holder.userName.setText(model.getUsername());
                        holder.userStatus.setText(model.getUserstatus());
                        Picasso.get().load(model.getImageUrl()).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.ProfileImage);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String visit_user_id=getRef(position).getKey();
                                Intent profileIntent=new Intent(getContext(),Show_profile_Activity.class);
                                profileIntent.putExtra("visit_user_id",visit_user_id);
                                startActivity(profileIntent);
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public Find_friend_activity.FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout,parent,false);
                        Find_friend_activity.FindFriendViewHolder viewHolder=new Find_friend_activity.FindFriendViewHolder(view);
                        progressDialog.dismiss();
                        return viewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_qn,menu);
        MenuItem item=menu.findItem(R.id.search_question);
        SearchView searchView=(SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void processsearch(String s) {
        progressDialog.show();
        FirebaseRecyclerOptions<Friends> options=
                new FirebaseRecyclerOptions.Builder<Friends>()
                        .setQuery(reference.orderByChild("username").startAt(s).endAt(s+"\uf8ff"),Friends.class)
                        .build();

        FirebaseRecyclerAdapter<Friends, Find_friend_activity.FindFriendViewHolder> adapter=
                new FirebaseRecyclerAdapter<Friends, Find_friend_activity.FindFriendViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final Find_friend_activity.FindFriendViewHolder holder, final int position, @NonNull Friends model) {
                        String v_user_id=getRef(position).getKey();

                        holder.userName.setText(model.getUsername());
                        holder.userStatus.setText(model.getUserstatus());
                        Picasso.get().load(model.getImageUrl()).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.ProfileImage);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String visit_user_id=getRef(position).getKey();
                                Intent profileIntent=new Intent(getContext(),Show_profile_Activity.class);
                                profileIntent.putExtra("visit_user_id",visit_user_id);
                                startActivity(profileIntent);
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public Find_friend_activity.FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout,parent,false);
                        Find_friend_activity.FindFriendViewHolder viewHolder=new Find_friend_activity.FindFriendViewHolder(view);
                        progressDialog.dismiss();
                        return viewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


}
