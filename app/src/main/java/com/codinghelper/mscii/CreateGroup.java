package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class CreateGroup extends AppCompatActivity {

    CircularImageView Groupimage;
    EditText GroupTitle,GroupDescription;
    Button ConfirmGroup;
    Uri filePath,resultUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference Rootref;
    ProgressBar progressBar;
    String currentUserID;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        auth=FirebaseAuth.getInstance();
        currentUserID=auth.getCurrentUser().getUid();
        Groupimage=(CircularImageView)findViewById(R.id.create_group_image);
        GroupTitle=(EditText)findViewById(R.id.group_name);
        ConfirmGroup=(Button)findViewById(R.id.confirm_group);
        GroupDescription=(EditText)findViewById(R.id.group_description);
        Rootref= FirebaseDatabase.getInstance().getReference();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        Groupimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(
                        Intent.createChooser(
                                intent,"select Image from here..."),1);
            }
        });

        ConfirmGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                if(resultUri!=null){
                    final String groupTitle = GroupTitle.getText().toString();
                    final String groupDescription = GroupDescription.getText().toString();
                    if (TextUtils.isEmpty(groupTitle)) {
                        GroupTitle.setError("Enter Group title");
                        GroupTitle.setFocusable(true);
                        progressBar.setVisibility(View.INVISIBLE);
                        return;
                    }
                    if (TextUtils.isEmpty(groupDescription)) {
                        GroupDescription.setError("Enter Group description");
                        GroupDescription.setFocusable(true);
                        progressBar.setVisibility(View.INVISIBLE);
                        return;
                    }
                    final StorageReference ref=storageReference.child("images/"+ UUID.randomUUID().toString());
                    ref.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    HashMap rec=new HashMap();
                                    rec.put("GroupTitle",groupTitle);
                                    rec.put("GroupDescription",groupDescription);
                                    rec.put("GroupIcon",String.valueOf(uri));
                                    rec.put("GroupAdmin",currentUserID);
                                    final String GroupId=Rootref.child("Group").push().getKey();
                                    Rootref.child("Group").child(GroupId).updateChildren(rec).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Rootref.child("studentDetail").child(currentUserID).child("InGroup").push().child("saved").setValue(GroupId);
                                            Rootref.child("Group").child(GroupId).child("Participater").child(currentUserID).setValue("Admin")
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            CreateGroup.super.onBackPressed();
                                                            Toast.makeText(CreateGroup.this, "Group created!!", Toast.LENGTH_LONG).show();
                                                        }
                                                    });

                                        }
                                    });
                                }
                            });
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CreateGroup.this,"Failed!"+e.getMessage(),Toast.LENGTH_SHORT).show();

                                }
                            });
                }else {
                    final String groupTitle = GroupTitle.getText().toString();
                    final String groupDescription = GroupDescription.getText().toString();
                    if (TextUtils.isEmpty(groupTitle)) {
                        GroupTitle.setError("Enter Group title");
                        GroupTitle.setFocusable(true);
                        progressBar.setVisibility(View.INVISIBLE);
                        return;
                    }
                    if (TextUtils.isEmpty(groupDescription)) {
                        GroupDescription.setError("Enter Group description");
                        GroupDescription.setFocusable(true);
                        progressBar.setVisibility(View.INVISIBLE);
                        return;
                    }
                    HashMap rec=new HashMap();
                    rec.put("GroupTitle",groupTitle);
                    rec.put("GroupDescription",groupDescription);
                    rec.put("GroupAdmin",currentUserID);
                    final String GroupId=Rootref.child("Group").push().getKey();
                    Rootref.child("Group").child(GroupId).updateChildren(rec).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Rootref.child("studentDetail").child(currentUserID).child("InGroup").push().child("saved").setValue(GroupId);
                            Rootref.child("Group").child(GroupId).child("Participater").child(currentUserID).setValue("Admin")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            CreateGroup.super.onBackPressed();
                                            Toast.makeText(CreateGroup.this, "Group created!!", Toast.LENGTH_LONG).show();
                                        }
                                    });

                        }
                    });
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            filePath=data.getData();
            CropImage.activity(filePath)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK)
            {
                resultUri=result.getUri();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),resultUri);
                    Groupimage.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}