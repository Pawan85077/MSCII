package com.codinghelper.mscii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class upload_status extends AppCompatActivity {

    private Button btn_choose;
    private Button btn_upload;
    private Button btn_choose1;
    private Button btn_upload1;
    private Button delStatus;
    private Button btnPre;
    ImageView show,formal;
    private Uri filePath,filePath2;
    private FirebaseStorage storage;
    private StorageReference storageReference,referencedel,referencet3;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference reference;
    ProgressBar progressBar;
    final static int PICK1=1;
    final static int PICK2=2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_status);
        Toast.makeText(upload_status.this,"upload status",Toast.LENGTH_SHORT).show();

        btn_choose=(Button) findViewById(R.id.chooseImg1);
        btn_upload=(Button) findViewById(R.id.uploadImg1);
        btn_choose1=(Button) findViewById(R.id.chooseImg2);
        btn_upload1=(Button) findViewById(R.id.uploadImg2);
        btnPre=(Button)findViewById(R.id.preview);
        show=(ImageView)findViewById(R.id.imgView1);
        formal=(ImageView)findViewById(R.id.formal);
        progressBar=(ProgressBar)findViewById(R.id.pbar);
        delStatus=(Button)findViewById(R.id.delstatus);

        user=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();



        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child("studentDetail").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String Simg1 =String.valueOf(dataSnapshot.child("states1").getValue());
                        String Simg2 =String.valueOf(dataSnapshot.child("states2").getValue());
                        if(Simg1.equals("no")&&Simg2.equals("no")){
                            Toast.makeText(upload_status.this,"Nothing to show",Toast.LENGTH_SHORT).show();

                        }else {
                            Intent profileIntent=new Intent(upload_status.this,Status_viewer.class);
                            profileIntent.putExtra("visit_user_id",user.getUid());
                            startActivity(profileIntent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


       delStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                reference.child("studentDetail").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                             String Simg1 =String.valueOf(dataSnapshot.child("states1").getValue());
                             String Simg2 =String.valueOf(dataSnapshot.child("states2").getValue());
                            if(Simg1.equals("no")&&Simg2.equals("no")){
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(upload_status.this,"Nothing to delete",Toast.LENGTH_SHORT).show();

                            }else{

                                if(!Simg1.equals("no")){
                                    referencedel=FirebaseStorage.getInstance().getReferenceFromUrl(Simg1);
                                    referencedel.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            reference.child("studentDetail").child(user.getUid()).child("states1").setValue("no");

                                        }
                                    });
                                }

                                if(!Simg2.equals("no")){
                                    referencedel=FirebaseStorage.getInstance().getReferenceFromUrl(Simg2);
                                    referencedel.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            reference.child("studentDetail").child(user.getUid()).child("states2").setValue("no");

                                        }
                                    });
                                }

                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(upload_status.this,"status deleted",Toast.LENGTH_SHORT).show();




                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

              /* reference.child("studentDetail").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   //     Toast.makeText(upload_status.this,"reference called",Toast.LENGTH_SHORT).show();

                        if(dataSnapshot.exists()){
                            String Status1 =String.valueOf(dataSnapshot.child("states1").getValue());
                            if(!Simg.equals("no")){
                                referencedel=FirebaseStorage.getInstance().getReferenceFromUrl(Status1);
                                referencedel.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(upload_status.this,"Status 1 deleted",Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }else{
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(upload_status.this,"Nothing to delete",Toast.LENGTH_SHORT).show();

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/



              //  reference.child("studentDetail").child(user.getUid()).child("states1").setValue("no");


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

        btn_choose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage1();
            }
        });
        btn_upload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadImage2();
            }
        });

    }

    private void uploadImage2() {
        if(filePath2!=null){
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref=storageReference.child("images/"+ UUID.randomUUID().toString());

            ref.putFile(filePath2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            reference.child("studentDetail").child(user.getUid()).child("states2").setValue(String.valueOf(uri))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            int saveCurrentTime=300;

                                            Intent i=new Intent(upload_status.this,Alarm.class);
                                            i.putExtra("visit_user_id",user.getUid());
                                            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),0,i,0);
                                            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                                            alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+saveCurrentTime*1000,pendingIntent);



                                            Toast.makeText(upload_status.this,"Finally completed!!",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(upload_status.this,"Failed!"+e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded"+(int)progress+"%");
                        }
                    });
        }else{
            Toast.makeText(upload_status.this,"Nothing selected",Toast.LENGTH_SHORT).show();

        }
    }

    private void uploadImage() {
        if(filePath!=null){
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref=storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            reference.child("studentDetail").child(user.getUid()).child("states1").setValue(String.valueOf(uri))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {


                                            int saveCurrentTime=300;

                                            Intent i=new Intent(upload_status.this,Alarm.class);
                                            i.putExtra("visit_user_id",user.getUid());
                                            PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),0,i,0);
                                            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                                            alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+saveCurrentTime*1000,pendingIntent);



                                            Toast.makeText(upload_status.this,"Finally completed!!",Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(upload_status.this,"Failed!"+e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded"+(int)progress+"%");
                        }
                    });
        }else{
            Toast.makeText(upload_status.this,"Nothing selected",Toast.LENGTH_SHORT).show();

        }
    }

    private void SelectImage() {
        filePath2=null;
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(
                Intent.createChooser(
                        intent,"select Image from here..."),PICK1);
    }
    private void SelectImage1() {
        filePath=null;
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(
                Intent.createChooser(
                        intent,"select Image from here..."),PICK2);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            formal.setVisibility(View.INVISIBLE);
            filePath = data.getData();

        Bitmap bitmap = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            show.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
     }
        else if (requestCode == PICK2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            formal.setVisibility(View.INVISIBLE);
            filePath2 = data.getData();

            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath2);
                show.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}