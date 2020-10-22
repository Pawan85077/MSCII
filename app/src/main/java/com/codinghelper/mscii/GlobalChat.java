package com.codinghelper.mscii;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SimpleTimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class GlobalChat extends Fragment {
  //  private ListView list_view;
  //  private ArrayAdapter<String> arrayAdapter;
   // private ArrayList<String> list_of_groups=new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    private DatabaseReference GroupRef,PGroupsref,GroupsRef;
    private RecyclerView myGroups;
    private String currentUserID;



    public GlobalChat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseAuth=FirebaseAuth.getInstance();
        GroupsRef=FirebaseDatabase.getInstance().getReference().child("Group");
        currentUserID=firebaseAuth.getCurrentUser().getUid();
        PGroupsref=FirebaseDatabase.getInstance().getReference().child("studentDetail").child(currentUserID).child("InGroup");
       /* GroupRef=FirebaseDatabase.getInstance().getReference().child("Groups");
        RetriveAndDisplyGroups();*/

        View v= inflater.inflate(R.layout.fragment_global_chat, container, false);
        myGroups=(RecyclerView)v.findViewById(R.id.group_recycle);
        myGroups.setLayoutManager(new LinearLayoutManager(getContext()));


       /* list_view=(ListView)v.findViewById(R.id.list_view);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String currentGroupName=adapterView.getItemAtPosition(position).toString();
                Intent groupChatIntent=new Intent(getActivity(),GroupChatActivity.class);
                groupChatIntent.putExtra("groupName",currentGroupName);
                startActivity(groupChatIntent);
            }
        });
        arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list_of_groups);
        list_view.setAdapter(arrayAdapter);*/
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options=
                new FirebaseRecyclerOptions.Builder<ModelGroup>()
                        .setQuery(PGroupsref,ModelGroup.class)
                        .build();
        FirebaseRecyclerAdapter<ModelGroup, GlobalChat.GroupssViewHolder> adapter
                = new FirebaseRecyclerAdapter<ModelGroup, GlobalChat.GroupssViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final GlobalChat.GroupssViewHolder holder, int position, @NonNull final ModelGroup model) {
               // String groupIDs=getRef(position).getKey();

                GroupsRef.child(model.getSaved()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String groupTitle=String.valueOf(dataSnapshot.child("GroupTitle").getValue());
                        holder.groupTitle.setText(groupTitle);
                        String groupDescription =String.valueOf(dataSnapshot.child("GroupDescription").getValue());
                        holder.groupDescription.setText(groupDescription);
                        String groupImage =String.valueOf(dataSnapshot.child("GroupIcon").getValue());
                        Picasso.get().load(groupImage).fit().centerCrop().noFade().placeholder(R.drawable.guest2).into(holder.GroupprofileImage);

                        // myGroups.removeAllViewsInLayout();
                      //  myGroups.setAdapter(null);
                       /* if(dataSnapshot.child(currentUserID).exists()){
                            holder.groupTitle.setText(model.getGroupTitle());
                            holder.groupDescription.setText(model.getGroupDescription());
                            Picasso.get().load(model.getGroupIcon()).fit().centerCrop().noFade().placeholder(R.drawable.guest2).into(holder.GroupprofileImage);

                        }*/
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent=new Intent(getActivity(),GroupChatActivity.class);
                        profileIntent.putExtra("group_id",model.getSaved());
                        startActivity(profileIntent);
                    }
                });
            }

            @NonNull
            @Override
            public GlobalChat.GroupssViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.group_custom_layout,parent,false);
                GlobalChat.GroupssViewHolder viewHolder=new GlobalChat.GroupssViewHolder(view);
                return viewHolder;
            }
        };
        myGroups.setAdapter(adapter);
        adapter.startListening();
    }
    public static class GroupssViewHolder extends RecyclerView.ViewHolder{
        TextView groupTitle, groupDescription;
        CircularImageView GroupprofileImage;
        public GroupssViewHolder(@NonNull View itemView) {
            super(itemView);
            groupTitle=itemView.findViewById(R.id.custom_group_title);
            groupDescription=itemView.findViewById(R.id.custom_group_Description);
            GroupprofileImage=itemView.findViewById(R.id.custom_group_image);


        }
    }

   /* private void RetriveAndDisplyGroups() {
        GroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set=new HashSet<>();
                Iterator iterator=dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }
                list_of_groups.clear();
                list_of_groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/
}
