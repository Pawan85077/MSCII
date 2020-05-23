package com.codinghelper.mscii;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class FriendChatActivity extends AppCompatActivity {
    private  String messageReceiverID,messageReceiverName,messageReceiverImage;
    private TextView userName,userLastSeen;
    private CircularImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_chat);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.navigation_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater layoutInflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView=layoutInflater.inflate(R.layout.custom_chat_bar,null);
        actionBar.setCustomView(actionBarView);
        messageReceiverID=getIntent().getExtras().get("visit_user_id").toString();
        messageReceiverName=getIntent().getExtras().get("visit_user_name").toString();
        messageReceiverImage=getIntent().getExtras().get("visit_user_image").toString();
        userName=(TextView)findViewById(R.id.custom_profile_name);
        userLastSeen=(TextView)findViewById(R.id.custom_last_seen);
        userImage=(CircularImageView)findViewById(R.id.custom_profile_image);
        userName.setText(messageReceiverName);
        Picasso.get().load(messageReceiverImage).fit().centerCrop().noFade().placeholder(R.drawable.main_stud).into(userImage);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
