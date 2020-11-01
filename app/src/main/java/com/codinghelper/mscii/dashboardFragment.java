package com.codinghelper.mscii;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

    StorageReference storageUtility,storageTenth,storageTwelfth,storageGraduation;
    FirebaseUser currentUser;
    DatabaseReference reference;
    private StorageReference storageReference;

    //Upload Buttons
    private Button resumeUpload;
    private Button thumbImpressionUpload;
    private Button signatureUpload;
    private Button casteCertificateUpload;
    private Button aadharUpload;


    //ImageViews
    private ImageView resumeImageView;
    private ImageView thumbImpressionImageView;
    private ImageView signatureImageView;
    private ImageView casteCertificateImageView;
    private ImageView aadharImageView;


    //ProgressBars
    private ProgressBar resumeProgress;
    private ProgressBar thumbImpressionProgress;
    private ProgressBar signatureProgress;
    private ProgressBar casteCertificateProgress;
    private ProgressBar aadharProgress;



    //Bottom Sheet utilities
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;


    //pick variable
    private static final int pick = 2;

    //Doc_Category and doc_Type
    private static String docCategory = "";
    private static String docType = "";


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


        bottom_sheet = rootView.findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);

        //Defining upload buttons
        resumeUpload = (Button) rootView.findViewById(R.id.resumeUpload);
        thumbImpressionUpload = (Button) rootView.findViewById(R.id.thumbImpressionUpload);
        signatureUpload = (Button) rootView.findViewById(R.id.signatureUpload);
        casteCertificateUpload = (Button) rootView.findViewById(R.id.casteCertificateUpload);
        aadharUpload = (Button) rootView.findViewById(R.id.aadharUpload);

        //Defining Image Views
        resumeImageView = (ImageView)rootView.findViewById(R.id.resumeImageView);
        thumbImpressionImageView = (ImageView)rootView.findViewById(R.id.thumbImpressionImageView);
        signatureImageView = (ImageView)rootView.findViewById(R.id.signatureImageView);
        casteCertificateImageView = (ImageView)rootView.findViewById(R.id.casteCertificateImageView);
        aadharImageView = (ImageView)rootView.findViewById(R.id.aadharImageView);

        //Handling Expand and Collapse
        RelativeLayout UtilityDocHeader = (RelativeLayout) rootView.findViewById(R.id.utilityDocHeader);
        final HorizontalScrollView UtilityDocScroll = (HorizontalScrollView) rootView.findViewById(R.id.utilityDocCardScroll);
        final ImageView UtilityExpandCollapse = (ImageView) rootView.findViewById(R.id.utilityExpandCollapse);
        UtilityDocHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UtilityDocScroll.getVisibility() == View.GONE){
                    UtilityDocScroll.setVisibility(View.VISIBLE);
                    docCategory = "utilityDocuments";
                    //docType = "resume";
                    fetchUtilityDocumentsStatus(docCategory);
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

        //Image Views OnclickListener
        resumeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "resume";
                showBottomSheetDialog(docCategory,docType);
            }
        });

        thumbImpressionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "thumbImpression";
                showBottomSheetDialog(docCategory,docType);
            }
        });

        signatureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "signature";
                showBottomSheetDialog(docCategory,docType);
            }
        });

        casteCertificateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "casteCertificate";
                showBottomSheetDialog(docCategory,docType);
            }
        });

        aadharImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "aadharCard";
                showBottomSheetDialog(docCategory,docType);
            }
        });

        //Upload Buttons OnclickListener
        resumeUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "resume";
                openGallery();
            }
        });

        thumbImpressionUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "thumbImpression";
                openGallery();
            }
        });

        signatureUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "signature";
                openGallery();
            }
        });

        casteCertificateUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "casteCertificate";
                openGallery();
            }
        });

        aadharUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "aadharCard";
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

    //Called automatically when image is selected
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

    //fetching Utility documents status
    private void fetchUtilityDocumentsStatus(String docCategoryx) {

        //URLs of images
        final String[] resumeURL = new String[1];
        final String[] thumbImpressionURL = new String[1];
        final String[] signatureURL = new String[1];
        final String[] casteCertificateURL = new String[1];
        final String[] aadharCardURL = new String[1];


        // defining ProgressBars
        resumeProgress = (ProgressBar)getView().findViewById(R.id.resumeProgress);
        thumbImpressionProgress = (ProgressBar)getView().findViewById(R.id.thumbImpressionProgress);
        signatureProgress = (ProgressBar)getView().findViewById(R.id.signatureProgress);
        casteCertificateProgress = (ProgressBar)getView().findViewById(R.id.casteCertificateProgress);
        aadharProgress = (ProgressBar)getView().findViewById(R.id.aadharProgress);

        DatabaseReference ref = reference.child("personalDocuments").child(docCategoryx);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //for Resume
                resumeURL[0] = String.valueOf(dataSnapshot.child("resume").getValue());
                if(URLUtil.isValidUrl(resumeURL[0]))
                {
                    resumeImageView.setVisibility(View.VISIBLE);
                    resumeProgress.setVisibility(View.VISIBLE);
                    resumeUpload.setVisibility(View.INVISIBLE);
                    if (resumeImageView !=null)
                    {
                        Glide.with(getContext().getApplicationContext())
                                .load(resumeURL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        resumeProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        resumeProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(resumeImageView);
                    }

                }else{
                    resumeImageView.setVisibility(View.INVISIBLE);
                    resumeProgress.setVisibility(View.INVISIBLE);
                    resumeUpload.setVisibility(View.VISIBLE);
                }

                //for thumb Impression
                thumbImpressionURL[0] = String.valueOf(dataSnapshot.child("thumbImpression").getValue());
                if(URLUtil.isValidUrl(thumbImpressionURL[0]))
                {
                    thumbImpressionImageView.setVisibility(View.VISIBLE);
                    thumbImpressionProgress.setVisibility(View.VISIBLE);
                    thumbImpressionUpload.setVisibility(View.INVISIBLE);
                    if (thumbImpressionImageView !=null)
                    {
                        Glide.with(getContext().getApplicationContext())
                                .load(thumbImpressionURL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        thumbImpressionProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        thumbImpressionProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(thumbImpressionImageView);
                    }

                }else{
                    thumbImpressionImageView.setVisibility(View.INVISIBLE);
                    thumbImpressionProgress.setVisibility(View.INVISIBLE);
                    thumbImpressionUpload.setVisibility(View.VISIBLE);
                }

                //for Signature
                signatureURL[0] = String.valueOf(dataSnapshot.child("signature").getValue());
                if(URLUtil.isValidUrl(signatureURL[0]))
                {
                    signatureImageView.setVisibility(View.VISIBLE);
                    signatureProgress.setVisibility(View.VISIBLE);
                    signatureUpload.setVisibility(View.INVISIBLE);
                    if (signatureImageView !=null)
                    {
                        Glide.with(getContext().getApplicationContext())
                                .load(signatureURL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        signatureProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        signatureProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(signatureImageView);
                    }

                }else{
                    signatureImageView.setVisibility(View.INVISIBLE);
                    signatureProgress.setVisibility(View.INVISIBLE);
                    signatureUpload.setVisibility(View.VISIBLE);
                }

                //for Caste certificate
                casteCertificateURL[0] = String.valueOf(dataSnapshot.child("casteCertificate").getValue());
                if(URLUtil.isValidUrl(casteCertificateURL[0]))
                {
                    casteCertificateImageView.setVisibility(View.VISIBLE);
                    casteCertificateProgress.setVisibility(View.VISIBLE);
                    casteCertificateUpload.setVisibility(View.INVISIBLE);
                    if (casteCertificateImageView !=null)
                    {
                        Glide.with(getContext().getApplicationContext())
                                .load(casteCertificateURL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        casteCertificateProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        casteCertificateProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(casteCertificateImageView);
                    }

                }else{
                    casteCertificateImageView.setVisibility(View.INVISIBLE);
                    casteCertificateProgress.setVisibility(View.INVISIBLE);
                    casteCertificateUpload.setVisibility(View.VISIBLE);
                }

                //for Aadhar Card
                aadharCardURL[0] = String.valueOf(dataSnapshot.child("aadharCard").getValue());
                if(URLUtil.isValidUrl(aadharCardURL[0]))
                {
                    aadharImageView.setVisibility(View.VISIBLE);
                    aadharProgress.setVisibility(View.VISIBLE);
                    aadharUpload.setVisibility(View.INVISIBLE);
                    if (aadharImageView !=null)
                    {
                        Glide.with(getContext().getApplicationContext())
                                .load(aadharCardURL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        aadharProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        aadharProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(aadharImageView);
                    }

                }else{
                    aadharImageView.setVisibility(View.INVISIBLE);
                    aadharProgress.setVisibility(View.INVISIBLE);
                    aadharUpload.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Show Bottom sheet dialogue
    private void showBottomSheetDialog(final String docCategoryy, final String docTypey) {

        final String[] imageUrl = new String[1];
        final DatabaseReference ref = reference.child("personalDocuments").child(docCategoryy);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageUrl[0] = String.valueOf(dataSnapshot.child(docTypey).getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View view = getLayoutInflater().inflate(R.layout.bottom_sheet_delete, null);

        ((View) view.findViewById(R.id.viewFullScreenImage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext().getApplicationContext(), "Showing Document in Full-Screen Mode", Toast.LENGTH_SHORT).show();
                if (URLUtil.isValidUrl(imageUrl[0])){
                    Intent fullScreenDocIntent = new Intent(getContext().getApplicationContext(),UserDocumentView.class);
                    fullScreenDocIntent.putExtra("docCategory",docCategoryy);
                    fullScreenDocIntent.putExtra("docType",docTypey);
                    startActivity(fullScreenDocIntent);
                }
                mBottomSheetDialog.dismiss();
            }
        });

        ((View) view.findViewById(R.id.shareImage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (URLUtil.isValidUrl(imageUrl[0]))
                {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Here is a link of my uploaded document from MCR Infotech mobile App\n\nClick on the Link below to view\n\n";
                    String shareSub = "MCR Infotech";
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                    sharingIntent.putExtra(Intent.EXTRA_TEXT,shareBody+imageUrl[0]);
                    startActivity(Intent.createChooser(sharingIntent,"Share using"));
                }
                mBottomSheetDialog.dismiss();
            }
        });

        ((View) view.findViewById(R.id.deleteImage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (URLUtil.isValidUrl(imageUrl[0]))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogTheme);
                    builder.setTitle("Delete Confirmation");
                    builder.setIcon(R.drawable.ic_warning);
                    builder.setMessage("Are you sure ?\nDo you really wanna delete selected document ?");
                    builder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            storageReference=FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl[0]);
                            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext().getApplicationContext(),"Document deleted successfully",Toast.LENGTH_SHORT).show();
                                    ref.child(docTypey).removeValue();
                                    Log.e("onSuccess:", " deleted file");

                                    //calling all categories status
                                    //later have to put switch cases for early loading
                                    fetchUtilityDocumentsStatus(docCategoryy);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext().getApplicationContext(),"SERVER ERROR...!!!\nCould not delete document",Toast.LENGTH_SHORT).show();
                                    Log.e("onFailure:", " could not delete file");
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getContext().getApplicationContext(),"You calcelled deletion",Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();

                }
                mBottomSheetDialog.dismiss();
            }
        });


        mBottomSheetDialog = new BottomSheetDialog(getContext());
        mBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }


}
