package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class editProfile extends AppCompatActivity {
    private Button btn_choose;
    private Button btn_upload;
    private Button btn_chooseb;
    private Button btn_uploadb,delpro,btn_uploadSong;

    private Button mp;             // For music player
    private EditText status;
    private ImageButton status_btn;
    private ImageView imageView;
    private Uri filePath,resultUri;
    private Uri filePathb,resultUrib;
    private final int PICK_IMAGE_REQUEST=22;
    private FirebaseStorage storage;
    private StorageReference storageReference,referencetTo;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    Spinner spinCourse;
    String selectedSem;
    String selectedsong;
    String songUrl;
    LinearLayout layout;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        btn_uploadSong=(Button)findViewById(R.id.upload_song);
        btn_chooseb=findViewById(R.id.chooseImgb);
        btn_uploadb=findViewById(R.id.uploadImgb);
        delpro=findViewById(R.id.delpropic);
        btn_choose=findViewById(R.id.chooseImg);
        layout=(LinearLayout)findViewById(R.id.ll5);

        mp=findViewById(R.id.musicPlayer);                           // For music player
        user=FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference();
        btn_upload=findViewById(R.id.uploadImg);
        imageView=findViewById(R.id.imgView);
        spinCourse = (Spinner) findViewById(R.id.studsem);
        status=findViewById(R.id.edit_status);
        status_btn=findViewById(R.id.status_confirm_bt);
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        String[] studentSem = {"--Select semister--","I","II","III","IV","V","VI"};
        String[] studentSong = {"--Select song--","Rainbow","Despacito","let me love you","Tum he Ana","teddy","Tujhe kitna Chahne Lage"};
        firebaseAuth = FirebaseAuth.getInstance();

        mp.setOnClickListener(new View.OnClickListener() {                  // For music player
            @Override
            public void onClick(View view) {
                startActivity(new Intent(editProfile.this, Tunes.class));
            }
        });

        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadImage();
            }
        });
        btn_chooseb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImageb();
            }
        });
        btn_uploadb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImageb();
            }
        });
        btn_uploadSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(
                        Intent.createChooser(
                                intent,"select Audio from here..."),2);
            }
        });
        delpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference.child("studentDetail").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(user.getUid())){
                            String picUrl= String.valueOf(dataSnapshot.child("imageUrl").getValue());
                            referencetTo=FirebaseStorage.getInstance().getReferenceFromUrl(picUrl);
                            referencetTo.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(editProfile.this,"Pic deleted",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }else {
                            reference.child("adminDetail").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String picUrl= String.valueOf(dataSnapshot.child("imageUrl").getValue());
                                    referencetTo=FirebaseStorage.getInstance().getReferenceFromUrl(picUrl);
                                    referencetTo.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(editProfile.this,"Pic deleted",Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
        status_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String updatestatus=status.getText().toString();
                if (TextUtils.isEmpty(updatestatus)) {
                    status.setError("enter new status");
                    status.setFocusable(true);
                    return;
                }else{


                    reference.child("studentDetail").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(user.getUid())){
                                reference.child("studentDetail").child(user.getUid()).child("userstatus").setValue(updatestatus)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    String id=reference.child("PublicView").push().getKey();
                                                    reference.child("PublicView").child(id).child("peopleUID").setValue(user.getUid());
                                                    reference.child("PublicView").child(id).child("publicBio").setValue(updatestatus);
                                                    Toast.makeText(editProfile.this,"Status Uploded!!",Toast.LENGTH_SHORT).show();
                                                }else {
                                                    String message=task.getException().getMessage();
                                                    Toast.makeText(editProfile.this,"Error:"+message,Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }else {
                                reference.child("adminDetail").child(user.getUid()).child("userstatus").setValue(updatestatus)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(editProfile.this,"Status Uploded!!",Toast.LENGTH_SHORT).show();
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
        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, studentSem);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCourse.setAdapter(courseAdapter);
        spinCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSem = parent.getItemAtPosition(position).toString();
                if (selectedSem!="--Select semister--") {
                    Toast.makeText(parent.getContext(), selectedSem, Toast.LENGTH_SHORT).show();
                    reference.child("studentDetail").child(user.getUid()).child("userSem").setValue(selectedSem)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(editProfile.this,"semister uploaded",Toast.LENGTH_SHORT).show();
                                    }else {
                                        String message=task.getException().getMessage();
                                        Toast.makeText(editProfile.this,"Error:"+message,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }


    @Override
    protected void onStart() {
        super.onStart();
        spinCourse.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        btn_uploadSong.setVisibility(View.GONE);
        reference.child("studentDetail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user.getUid())){
                    spinCourse.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.VISIBLE);
                    btn_uploadSong.setVisibility(View.VISIBLE);
                }else {
                    spinCourse.setVisibility(View.GONE);
                    layout.setVisibility(View.GONE);
                    btn_uploadSong.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SelectImage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(
                    Intent.createChooser(
                            intent,"select Image from here..."),1);

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
           /* Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }*/

        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK)
            {
                resultUri=result.getUri();
                File file=new File(resultUri.getPath());
                int file_size=Integer.parseInt(String.valueOf(file.length()/1024));
                Toast.makeText(editProfile.this,String.valueOf(file_size)+"kb",Toast.LENGTH_SHORT).show();
                Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),resultUri);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
            }
        }



        if(requestCode==2 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            filePath=data.getData();
            MediaPlayer mediaPlayer=MediaPlayer.create(this,filePath);
            int Second=(mediaPlayer.getDuration()/1000);
            if(Second>30){
                Toast.makeText(editProfile.this,"Audio must be less than equal to 30 second",Toast.LENGTH_LONG).show();
            }else {
                if(filePath!=null){
                    final ProgressDialog progressDialog=new ProgressDialog(this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();
                    final StorageReference ref=storageReference.child("audio/"+ UUID.randomUUID().toString());
                    ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    reference.child("studentDetail").child(user.getUid()).child("audioUrl").setValue(String.valueOf(uri))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    String id=reference.child("PublicView").push().getKey();
                                                    reference.child("PublicView").child(id).child("peopleUID").setValue(user.getUid());
                                                    reference.child("PublicView").child(id).child("publicSong").setValue(String.valueOf(uri));
                                                    Toast.makeText(editProfile.this,"Audio uploaded successfully!!",Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            });
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(editProfile.this,"Failed!"+e.getMessage(),Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded"+(int)progress+"%");
                                }
                            });
                }
            }
        }





    }
    private void  uploadImage(){
        if(resultUri!=null){
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref=storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(editProfile.this,"Image Uploaded!!",Toast.LENGTH_SHORT).show();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(final Uri uri) {
                      //      HashMap<String,String> hashMap=new HashMap<>();
                      //      hashMap.put("imageUrl",String.valueOf(uri));
                            reference.child("studentDetail").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(user.getUid())){
                                        reference.child("studentDetail").child(user.getUid()).child("imageUrl").setValue(String.valueOf(uri))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        String id=reference.child("PublicView").push().getKey();
                                                        reference.child("PublicView").child(id).child("peopleUID").setValue(user.getUid());
                                                        reference.child("PublicView").child(id).child("publicImage").setValue(String.valueOf(uri));
                                                        Toast.makeText(editProfile.this,"Finally completed!!",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }else {
                                        reference.child("adminDetail").child(user.getUid()).child("imageUrl").setValue(String.valueOf(uri))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(editProfile.this,"Finally completed!!",Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(editProfile.this,"Failed!"+e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded"+(int)progress+"%");
                        }
                    });
        }
    }


    private void SelectImageb(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(
                Intent.createChooser(
                        intent,"select Image from here..."),1);

    }
    private void  uploadImageb(){
        if(resultUri!=null){
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref=storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(editProfile.this,"Image Uploaded!!",Toast.LENGTH_SHORT).show();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //      HashMap<String,String> hashMap=new HashMap<>();
                            //      hashMap.put("imageUrl",String.valueOf(uri));
                            reference.child("studentDetail").child(user.getUid()).child("imageUrlBackground").setValue(String.valueOf(uri))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(editProfile.this,"Finally completed!!",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(editProfile.this,"Failed!"+e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded"+(int)progress+"%");
                        }
                    });
        }
    }
}
