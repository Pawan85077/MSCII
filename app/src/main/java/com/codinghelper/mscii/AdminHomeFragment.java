package com.codinghelper.mscii;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.content.res.Resources;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class AdminHomeFragment extends Fragment {

    public AdminHomeFragment(){
        //Public home fragment
    }

    RecyclerView AdminRecyclerView;
    //Admin_Recycler_Adapter admin_recycler_adapter;

    DatabaseReference Rootref;
    private DatabaseReference adminToAdminMessage;
    FirebaseAuth firebaseAuth;
    String currentUserID;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.admin_fragment_home, container, false);
        View view = inflater.inflate(R.layout.admin_fragment_home, container, false);

        final ExtendedFloatingActionButton composeMessageFAB = view.findViewById(R.id.composeMessageFAB);
        composeMessageFAB.extend();

        Rootref= FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID=firebaseAuth.getCurrentUser().getUid();
        adminToAdminMessage = FirebaseDatabase.getInstance().getReference().child("adminToAdminMessage");
        AdminRecyclerView = view.findViewById(R.id.admin_home_RecyclerView);
        AdminRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //admin_recycler_adapter = new Admin_Recycler_Adapter();
        //AdminRecyclerView.setAdapter(admin_recycler_adapter);
        //recyclerView.setHasFixedSize(true);
        progressDialog = new ProgressDialog(getContext(),R.style.AlertDialogTheme);
        progressDialog.setMessage("loading..");

        //FAB click listener
        composeMessageFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent composeMessageIntent = new Intent(getContext(),AdminAddRecepients.class);
                startActivity(composeMessageIntent);
            }
        });

        AdminRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy > 0) {
                    //scroll Down
                    if (composeMessageFAB.isExtended()) composeMessageFAB.shrink();
                }else if (dy <0){
                    //scroll up
                    if(!composeMessageFAB.isExtended()) composeMessageFAB.extend();
                }
            }
        });
        getActivity().setTitle("Home");
        return view;
    }






    //Options menu
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        progressDialog.show();
        FirebaseRecyclerOptions<Admin_Home_Getter_Setter> options=
                new FirebaseRecyclerOptions.Builder<Admin_Home_Getter_Setter>()
                        .setQuery(adminToAdminMessage,Admin_Home_Getter_Setter.class)
                        .build();

        FirebaseRecyclerAdapter<Admin_Home_Getter_Setter, AdminHomeFragment.AdminMessageViewHolder> adapter=
                new FirebaseRecyclerAdapter<Admin_Home_Getter_Setter, AdminHomeFragment.AdminMessageViewHolder>(options) {

                    @Override
                    protected void onBindViewHolder(@NonNull AdminMessageViewHolder holder, int position, @NonNull Admin_Home_Getter_Setter model) {
                        holder.senderDepartmentName.setText(model.getSenderDepartmentName());
                        holder.receiverName.setText(model.getReceiverName());
                        holder.messageTitle.setText(model.getMessageTitle());
                        holder.messageBody.setText(model.getMessageBody());
                        holder.likeCount.setText(model.getLikeCount());
                        holder.dateOfPost.setText(model.getDateOfPost());
                        holder.timeOfPost.setText(model.getTimeOfPost());
                        Picasso.get().load(model.getSenderImage()).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(holder.senderImage);
                        holder.likes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(view.getContext(), "Like Clicked", Toast.LENGTH_SHORT).show();
                            }
                        });
                        holder.discussionLinearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(view.getContext(), "Discussion Tab Clicked", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminHomeFragment.AdminMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_home_row_item,parent,false);
                        AdminHomeFragment.AdminMessageViewHolder viewHolder=new AdminHomeFragment.AdminMessageViewHolder(view);
                        progressDialog.dismiss();
                        return viewHolder;
                    }
                };
        AdminRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public static class AdminMessageViewHolder extends RecyclerView.ViewHolder {

        ImageView senderImage,likes;
        TextView senderDepartmentName, receiverName, messageTitle, messageBody, likeCount, timeOfPost, dateOfPost;
        LinearLayout discussionLinearLayout;

        public AdminMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            senderImage = itemView.findViewById(R.id.senderProfilePic);
            likes = itemView.findViewById(R.id.likeImage);
            senderDepartmentName = itemView.findViewById(R.id.senderDepartmentName);
            receiverName = itemView.findViewById(R.id.receiverName);
            messageTitle = itemView.findViewById(R.id.messageTitle);
            messageBody = itemView.findViewById(R.id.messageBody);
            likeCount = itemView.findViewById(R.id.like_Count);
            discussionLinearLayout = itemView.findViewById(R.id.message_Discussion);
            dateOfPost = itemView.findViewById(R.id.dateOFPost);
            timeOfPost = itemView.findViewById(R.id.timeOfPost);



        }
    }


}

