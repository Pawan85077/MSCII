package com.codinghelper.mscii;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class dashboardFragment extends Fragment {

    private Uri filePathx,resultUrix;
    StorageReference storageUtility,storageTenth,storageTwelfth,storageGraduation;
    FirebaseAuth firebaseAuthx;
    FirebaseUser currentUser;
    DatabaseReference reference;


    //Upload Buttons
    private Button resume;
    private Button thumbImpression;

    //Documents cards Views
    private CardView resumeCard;

    //pick variable
    private static final int pick = 2;

    //Doc_Category and doc_Type
    static String docCategory = "";
    static String docType = "";

    static String transferrableImageURL;


    public dashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("studentDetail").child(currentUser.getUid());
        storageUtility = FirebaseStorage.getInstance().getReference().child("utility_Documents/");
        storageTenth = FirebaseStorage.getInstance().getReference().child("tenth_Documents/");
        storageTwelfth = FirebaseStorage.getInstance().getReference().child("twelfth_Documents/");
        storageGraduation = FirebaseStorage.getInstance().getReference().child("graduation_Documents/");


        //Defining upload buttons
        resume = (Button) rootView.findViewById(R.id.resume);
        thumbImpression = (Button) rootView.findViewById(R.id.thumbImpression);

        //Defining cardViews
        resumeCard = (CardView)rootView.findViewById(R.id.resumeCard);

        //Handling Expand and Collapse
        RelativeLayout UtilityDocHeader = (RelativeLayout) rootView.findViewById(R.id.utilityDocHeader);
        final HorizontalScrollView UtilityDocScroll = (HorizontalScrollView) rootView.findViewById(R.id.utilityDocCardScroll);
        final ImageView UtilityExpandCollapse = (ImageView) rootView.findViewById(R.id.utilityExpandCollapse);
        UtilityDocHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UtilityDocScroll.getVisibility() == View.GONE){
                    UtilityDocScroll.setVisibility(View.VISIBLE);
                    UtilityExpandCollapse.setBackgroundResource(R.drawable.ic_colapse);
                }else {
                    UtilityDocScroll.setVisibility(View.GONE);
                    UtilityExpandCollapse.setBackgroundResource(R.drawable.ic_expand);
                }
            }
        });

        RelativeLayout TenthDocHeader = (RelativeLayout) rootView.findViewById(R.id.tenthDocHeader);
        final HorizontalScrollView TenthDocScroll = (HorizontalScrollView) rootView.findViewById(R.id.tenthDocCardScroll);
        final ImageView TenthExpandCollapse = (ImageView) rootView.findViewById(R.id.tenthExpandCollapse);
        TenthDocHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TenthDocScroll.getVisibility() == View.GONE){
                    TenthDocScroll.setVisibility(View.VISIBLE);
                    TenthExpandCollapse.setBackgroundResource(R.drawable.ic_colapse);
                }else {
                    TenthDocScroll.setVisibility(View.GONE);
                    TenthExpandCollapse.setBackgroundResource(R.drawable.ic_expand);
                }
            }
        });

        RelativeLayout TwelfthDocHeader = (RelativeLayout) rootView.findViewById(R.id.twelfthDocHeader);
        final HorizontalScrollView TwelfthDocScroll = (HorizontalScrollView) rootView.findViewById(R.id.twelfthDocCardScroll);
        final ImageView TwelfthExpandCollapse = (ImageView) rootView.findViewById(R.id.twelfthExpandCollapse);
        TwelfthDocHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TwelfthDocScroll.getVisibility() == View.GONE){
                    TwelfthDocScroll.setVisibility(View.VISIBLE);
                    TwelfthExpandCollapse.setBackgroundResource(R.drawable.ic_colapse);
                }else {
                    TwelfthDocScroll.setVisibility(View.GONE);
                    TwelfthExpandCollapse.setBackgroundResource(R.drawable.ic_expand);
                }
            }
        });

        RelativeLayout GraduationDocHeader = (RelativeLayout) rootView.findViewById(R.id.graduationDocHeader);
        final HorizontalScrollView GraduationDocScroll = (HorizontalScrollView) rootView.findViewById(R.id.graduationDocCardScroll);
        final ImageView GraduationExpandCollapse = (ImageView) rootView.findViewById(R.id.graduationExpandCollapse);
        GraduationDocHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(GraduationDocScroll.getVisibility() == View.GONE){
                    GraduationDocScroll.setVisibility(View.VISIBLE);
                    GraduationExpandCollapse.setBackgroundResource(R.drawable.ic_colapse);
                }else {
                    GraduationDocScroll.setVisibility(View.GONE);
                    GraduationExpandCollapse.setBackgroundResource(R.drawable.ic_expand);
                }
            }
        });

        //Card Views click action
        resumeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "resume";
                final String s = "Resume";
                getImageAndOpenIt(docCategory,docType,s);
            }
        });

        //Upload Buttons Action
        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "resume";
                openGallery();
            }
        });

        thumbImpression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "thumbImpression";
                openGallery();
            }
        });



        //initToolbar();
        getActivity().setTitle("Dashboard");
        return rootView;
    }

    //function for opening Gallery
    private void openGallery() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gallery,pick);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==pick && resultCode== Activity.RESULT_OK && data!=null){
            Uri image = data.getData();
            CropImage.activity(image)
                    .start(getContext(), this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();

                //uploading on cropping
                if(resultUri!=null){
                    final ProgressDialog progressDialog=new ProgressDialog(getContext());
                    progressDialog.setTitle("Uploading your Document...");
                    progressDialog.show();
                    final StorageReference ref=storageUtility.child(currentUser.getUid() + docType);
                    ref.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext().getApplicationContext(),"Document Uploaded",Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    reference.child("personalDocuments").child(docCategory).child(docType).setValue(String.valueOf(uri))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getContext().getApplicationContext(),"Database updated Successfully...",Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getContext().getApplicationContext(),"Failed!"+e.getMessage(),Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                }
                            });
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    //snack bar when image is not uploaded
    private void notUploadedSnackBar(String s) {
        Snackbar snackbar = Snackbar.make(getView(), "Opps.. Seems like your\n" + s +" is not uploaded yet...!!!", Snackbar.LENGTH_LONG)
                .setAction("UPLOAD", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openGallery();
                    }
                });
        snackbar.show();
    }

    //function to get image url and open in another layout..
    private void getImageAndOpenIt(String docCategory,String docType,String s){
        final String docTypex = docType;
        final String sx = s;
        DatabaseReference ref = reference.child("personalDocuments").child(docCategory);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                transferrableImageURL = String.valueOf(dataSnapshot.child(docTypex).getValue());
                if (transferrableImageURL.isEmpty()){
                    notUploadedSnackBar(sx);
                }else
                    Toast.makeText(getContext().getApplicationContext(), ""+transferrableImageURL, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //    private void initToolbar() {
//        Toolbar toolbar = (Toolbar)getView().findViewById(R.id.toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        Tools.setSystemBarColor(this);
//    }

}
